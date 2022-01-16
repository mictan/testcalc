package model.database.dao;

import model.database.HibernateUtil;
import org.hibernate.Session;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ADAO {
    protected  <R> R openSession(Function<Session, R> callable){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return callable.apply(session);
        }
    }

    protected boolean withTransaction(Consumer<Session> callable){
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
