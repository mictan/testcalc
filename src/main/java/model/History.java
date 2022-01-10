package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.data.HistoryItem;

public class History {
    private final ObservableList<HistoryItem> items = FXCollections.observableArrayList();
    private final ObservableList<HistoryItem> itemsRo = FXCollections.unmodifiableObservableList(items);
    private final IntegerProperty maxItems = new SimpleIntegerProperty(10);//negative = infinity;

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
}
