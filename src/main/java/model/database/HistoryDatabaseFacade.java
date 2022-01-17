package model.database;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.data.HistoryItem;
import model.database.dao.HistoryItemDAO;
import model.helpers.FinalizePool;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryDatabaseFacade implements Closeable {
    private final LongProperty userId = new SimpleLongProperty(-1);//-1 = no user;
    private ObservableList<HistoryItem> items;
    private boolean processLoad = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ListChangeListener<HistoryItem> listener = new ListChangeListener<HistoryItem>() {
        @Override
        public void onChanged(Change<? extends HistoryItem> c) {
            if(processLoad){
                return;
            }
            ChangedTask task = new ChangedTask(userId.get());
            while (c.next()){
                if (c.wasPermutated()) {
                    //permutate
                } else if (c.wasUpdated()) {
                    task.changed.addAll(items.subList(c.getFrom(), c.getTo()));
                } else {
                    task.removed.addAll(c.getRemoved());
                    task.added.addAll(c.getAddedSubList());
                }
            }
            if(!task.isEmpty()){
                executor.submit(task);
            }
        }
    };

    public HistoryDatabaseFacade() {
        FinalizePool.getInstance().addFinalizer(this);
        userId.addListener((observable, oldValue, newValue) -> executor.submit(new LoadTask(newValue.longValue())));
    }

    public void load(){
        executor.submit(new LoadTask(userId.get()));
    }

    public LongProperty userIdProperty() {
        return userId;
    }

    public void setItems(ObservableList<HistoryItem> items) {
        if(this.items != null){
            this.items.removeListener(listener);
        }
        this.items = items;
        if(items != null){
            items.addListener(listener);
        }
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    private class LoadTask implements Runnable{
        private final long userId;

        public LoadTask(long userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            try {
                Collection<HistoryItem> newItems = DAOFactory.getInstance().getHistoryItemDAO().getByUserId(userId);
                processLoad = true;
                try {
                    items.clear();
                    items.addAll(newItems);//TODO merge values?
                } finally {
                    processLoad = false;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static class ChangedTask implements Runnable {
        private final List<HistoryItem> changed = new ArrayList<>();
        private final List<HistoryItem> added = new ArrayList<>();
        private final List<HistoryItem> removed = new ArrayList<>();

        private final long userId;

        public ChangedTask(long userId) {
            this.userId = userId;
        }

        public boolean isEmpty(){
            return changed.isEmpty() && added.isEmpty() && removed.isEmpty();
        }

        @Override
        public void run() {
            try {
                HistoryItemDAO dao = DAOFactory.getInstance().getHistoryItemDAO();
                if(!changed.isEmpty()){
                    dao.update(changed);
                }
                if(!added.isEmpty()){
                    for (HistoryItem item: added){//set user id to all new items
                        item.setUserId(userId);
                    }
                    dao.add(added);
                }
                if(!removed.isEmpty()){
                    dao.delete(removed);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
