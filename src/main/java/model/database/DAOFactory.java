package model.database;

import model.database.dao.HistoryItemDAO;
import model.database.dao.HistoryItemDAOImpl;
import model.database.dao.UserDAO;
import model.database.dao.UserDAOImpl;

public class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();
    public static DAOFactory getInstance(){
        return instance;
    }

    private HistoryItemDAO historyItemDAO = null;
    private UserDAO userDAO = null;

    private DAOFactory(){}

    public HistoryItemDAO getHistoryItemDAO(){
        if(historyItemDAO == null){
            historyItemDAO = new HistoryItemDAOImpl();
        }
        return historyItemDAO;
    }

    public UserDAO getUserDAO() {
        if(userDAO == null){
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }
}
