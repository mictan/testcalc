package model.database.dao;

import model.data.HistoryItem;
import model.database.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class HistoryItemDAOImpl implements HistoryItemDAO{
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
    public void delete(Collection<HistoryItem> data) {
        withTransaction(session -> {
            for (HistoryItem item: data){
                session.delete(item);
            }
        });
    }

    private <R> R openSession(Function<Session, R> callable){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return callable.apply(session);
        }
    }

    private boolean withTransaction(Consumer<Session> callable){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                callable.accept(session);
                session.getTransaction().commit();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
                return false;
            }
        }
    }
}
