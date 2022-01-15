package model.database.dao;

import model.data.HistoryItem;

import java.util.Collection;

public interface HistoryItemDAO {
    void add(Collection<HistoryItem> data);
    void update(Collection<HistoryItem> data);
    Collection<HistoryItem> getAll();
    void delete(Collection<HistoryItem> data);
}
