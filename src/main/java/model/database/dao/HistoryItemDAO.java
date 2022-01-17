package model.database.dao;

import model.data.HistoryItem;

import java.util.Collection;

public interface HistoryItemDAO {
    void add(Collection<HistoryItem> data);
    void update(Collection<HistoryItem> data);
    Collection<HistoryItem> getAll();
    Collection<HistoryItem> getByUserId(long userId);
    void delete(Collection<HistoryItem> data);
}
