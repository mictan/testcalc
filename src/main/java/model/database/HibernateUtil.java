package model.database;

import model.data.HistoryItem;
import model.helpers.FinalizePool;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static final Runnable finalizer = HibernateUtil::shutdown;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class){
                if(sessionFactory == null){
                    try {
                        FinalizePool.getInstance().addFinalizer(finalizer);
                        // Create registry
                        registry = new StandardServiceRegistryBuilder()
                                .configure("model/hibernate.cfg.xml")
                                .build();

                        // Create MetadataSources
                        MetadataSources sources = new MetadataSources(registry)
                                .addAnnotatedClass(HistoryItem.class);

                        // Create Metadata
                        Metadata metadata = sources.getMetadataBuilder().build();

                        // Create SessionFactory
                        sessionFactory = metadata.getSessionFactoryBuilder().build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (registry != null) {
                            StandardServiceRegistryBuilder.destroy(registry);
                            registry = null;
                        }
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
            registry = null;
        }
        /*if(sessionFactory != null){
            sessionFactory.close();//org.hibernate.service.UnknownServiceException: Unknown service requested [org.hibernate.engine.jdbc.connections.spi.ConnectionProvider]
            sessionFactory = null;
        }*/
    }
}
