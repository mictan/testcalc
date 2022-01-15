package model.database;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.data.HistoryItem;
import model.database.dao.HistoryItemDAO;
import model.helpers.FinalizePool;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryDatabaseFacade implements Closeable {
    private ObservableList<HistoryItem> items;
    private boolean processLoad = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ListChangeListener<HistoryItem> listener = new ListChangeListener<HistoryItem>() {
        @Override
        public void onChanged(Change<? extends HistoryItem> c) {
            if(processLoad){
                return;
            }
            ChangedTask task = new ChangedTask();
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
    }

    public void load(){
        Collection<HistoryItem> items;
        try {
            items = executor.submit(new LoadTask()).get();
            processLoad = true;
            try {
                this.items.addAll(items);//TODO merge values?
            } finally {
                processLoad = false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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

    private static class LoadTask implements Callable<Collection<HistoryItem>>{

        @Override
        public Collection<HistoryItem> call() {
            return DAOFactory.getInstance().getHistoryItemDAO().getAll();
        }
    }

    private static class ChangedTask implements Runnable {
        private final List<HistoryItem> changed = new ArrayList<>();
        private final List<HistoryItem> added = new ArrayList<>();
        private final List<HistoryItem> removed = new ArrayList<>();

        public boolean isEmpty(){
            return changed.isEmpty() && added.isEmpty() && removed.isEmpty();
        }

        @Override
        public void run() {
            HistoryItemDAO dao = DAOFactory.getInstance().getHistoryItemDAO();
            if(!changed.isEmpty()){
                dao.update(changed);
            }
            if(!added.isEmpty()){
                dao.add(added);
            }
            if(!removed.isEmpty()){
                dao.delete(removed);
            }
        }
    }
}
