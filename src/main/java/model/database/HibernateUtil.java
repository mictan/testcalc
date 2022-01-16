package model.database;

import model.data.HistoryItem;
import model.data.User;
import model.helpers.FinalizePool;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.util.Properties;

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
                        Properties props = loadDbProperties();
                        registry = new StandardServiceRegistryBuilder()
                                .configure("model/hibernate.cfg.xml")
                                .applySettings(props)
                                .build();

                        // Create MetadataSources
                        MetadataSources sources = new MetadataSources(registry)
                                .addAnnotatedClass(HistoryItem.class)
                                .addAnnotatedClass(User.class);

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

    private static Properties loadDbProperties() {
        Properties dbConnectionProperties = new Properties();
        try {
            dbConnectionProperties.load(Object.class.getResourceAsStream("/model/dbConnection.properties"));
            dbConnectionProperties.setProperty("hibernate.hbm2ddl.auto", "none");
            String url = dbConnectionProperties.getProperty("hibernate.connection.url");
            if(url != null && url.startsWith("jdbc:h2:file:")){
                File file = new File(url.substring("jdbc:h2:file:".length()) + ".h2.db");//check first start
                if(!file.exists()){
                    dbConnectionProperties.setProperty("hibernate.hbm2ddl.auto", "create");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConnectionProperties;
    }
}
