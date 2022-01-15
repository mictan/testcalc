package model.database;

import model.database.dao.HistoryItemDAO;
import model.database.dao.HistoryItemDAOImpl;

public class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();
    public static DAOFactory getInstance(){
        return instance;
    }

    private HistoryItemDAO historyItemDAO = null;

    private DAOFactory(){}

    public HistoryItemDAO getHistoryItemDAO(){
        if(historyItemDAO == null){
            historyItemDAO = new HistoryItemDAOImpl();
        }
        return historyItemDAO;
    }
}
