package model.database.dao;

import model.data.HistoryItem;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class HistoryItemDAOImpl extends ADAO implements HistoryItemDAO{
    @Override
    public void add(Collection<HistoryItem> data) {
        withTransaction(session -> {
            for (HistoryItem item: data){
                session.save(item);
            }
        });
    }

    @Override
    public void update(Collection<HistoryItem> data) {
        withTransaction(session -> {
            for (HistoryItem item: data){
                session.saveOrUpdate(item);
            }
        });
    }

    @Override
    public Collection<HistoryItem> getAll() {
        return openSession(session -> {
            CriteriaQuery<HistoryItem> query = session.getCriteriaBuilder().createQuery(HistoryItem.class);
            query.select(query.from(HistoryItem.class));
            return session.createQuery(query).list();
        });
    }

    @Override
    public Collection<HistoryItem> getByUserId(long userId) {
        return openSession(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<HistoryItem> query = builder.createQuery(HistoryItem.class);
            Root<HistoryItem> from = query.from(HistoryItem.class);
            query.select(from).where(builder.equal(from.get("userId"), userId));
            return session.createQuery(query).list();
        });
    }

    @Override
    public void delete(Collection<HistoryItem> data) {
        withTransaction(session -> {
            for (HistoryItem item: data){
                session.delete(item);
            }
        });
    }
}
