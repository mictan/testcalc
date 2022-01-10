package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.data.HistoryItem;

public class History {
    private final ObservableList<HistoryItem> items = FXCollections.observableArrayList();
    private final ObservableList<HistoryItem> itemsRo = FXCollections.unmodifiableObservableList(items);

    public ObservableList<HistoryItem> getItems() {
        return itemsRo;
    }

    public void addItem(HistoryItem item){
        items.add(item);
    }

    public HistoryItem getLast(){
        if(items.isEmpty()){
            return null;
        }
        return items.get(items.size() - 1);
    }
}
