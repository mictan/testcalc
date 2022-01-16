package model.database.dao;

import model.data.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class UserDAOImpl extends ADAO implements UserDAO{
    @Override
    public void add(User user) {
        withTransaction(session -> {
            session.save(user);
        });
    }

    @Override
    public void update(User user) {
        withTransaction(session -> {
            session.saveOrUpdate(user);
        });
    }

    @Override
    public void delete(User user) {
        withTransaction(session -> {
            session.delete(user);
        });
    }

    @Override
    public User getByName(String userName) {
        return openSession(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> from = query.from(User.class);
            query.select(from).where(builder.equal(from.get("name"), userName));
            return session.createQuery(query).getSingleResult();
        });
    }

    @Override
    public Collection<User> getAll() {
        return openSession(session -> {
            CriteriaQuery<User> query = session.getCriteriaBuilder().createQuery(User.class);
            query.select(query.from(User.class));
            return session.createQuery(query).list();
        });
    }
}
