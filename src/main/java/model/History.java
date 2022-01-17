package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.data.HistoryItem;
import model.database.HistoryDatabaseProvider;

public class History {
    private final ObservableList<HistoryItem> items = FXCollections.observableArrayList();
    private final ObservableList<HistoryItem> itemsRo = FXCollections.unmodifiableObservableList(items);
    private final IntegerProperty maxItems = new SimpleIntegerProperty(10);//negative = infinity;
    private HistoryDatabaseProvider database = null;

    public ObservableList<HistoryItem> getItems() {
        return itemsRo;
    }

    public IntegerProperty maxItemsProperty(){
        return maxItems;
    }

    public void addItem(HistoryItem item){
        items.add(item);
        if(maxItems.get() >= 0 && items.size() > maxItems.get()){
            items.remove(0);
        }
    }

    public HistoryItem getLast(){
        if(items.isEmpty()){
            return null;
        }
        return items.get(items.size() - 1);
    }

    public History setDatabase(HistoryDatabaseProvider database){
        if(this.database != null){
            this.database.setItems(null);
        }
        this.database = database;
        if(database != null){
            database.setItems(items);
            database.load();
        }
        return this;
    }

    public void clearItems() {
        items.clear();
    }
}
