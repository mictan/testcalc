package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.actions.AAction;
import model.actions.ActionAdd;
import model.actions.ActionSub;
import model.data.HistoryItem;
import model.database.DAOFactory;
import model.database.HistoryDatabaseFacade;
import model.database.dao.HistoryItemDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

public class DatabaseTest {
    @Test
    public void HistoryDAOTest(){
        AAction.init();
        HistoryItemDAO historyItemDAO = DAOFactory.getInstance().getHistoryItemDAO();;
        List<HistoryItem> items = Arrays.asList(
                new HistoryItem(1., AAction.createByName(ActionAdd.NAME), 2., 3.)
                , new HistoryItem(1., AAction.createByName(ActionAdd.NAME), 2., 3.)
        );
        for (int i = 0; i < items.size(); i++){
            items.get(i).setTime(new Timestamp(i * 10000L));
            items.get(i).setUserId(-20L);
        }
        historyItemDAO.add(items);
        Collection<HistoryItem> all = historyItemDAO.getByUserId(-20L);
        try{
            Assertions.assertIterableEquals(items, all);
        } finally {
            historyItemDAO.delete(all);
        }
    }

    @Test
    public void HistoryFacadeTest() throws InterruptedException {
        AAction.init();
        HistoryItemDAO historyItemDAO = DAOFactory.getInstance().getHistoryItemDAO();
        List<HistoryItem> items = Arrays.asList(
                new HistoryItem(1., AAction.createByName(ActionAdd.NAME), 2., 3.)
                , new HistoryItem(1., AAction.createByName(ActionAdd.NAME), 2., 3.)
        );
        for (int i = 0; i < items.size(); i++){
            items.get(i).setTime(new Timestamp(i * 10000L));
            items.get(i).setUserId(-22L);
        }
        historyItemDAO.add(items);
        HistoryDatabaseFacade facade = new HistoryDatabaseFacade();
        ObservableList<HistoryItem> oItems = FXCollections.observableArrayList();
        facade.setItems(oItems);
        facade.userIdProperty().setValue(-22);
        Thread.sleep(100);
        oItems.add(new HistoryItem(1., AAction.createByName(ActionAdd.NAME), 2., 3.));
        Thread.sleep(100);
        oItems.add(new HistoryItem(10., AAction.createByName(ActionSub.NAME), 2., 8.));
        Thread.sleep(100);
        oItems.add(new HistoryItem(20., AAction.createByName(ActionSub.NAME), 2., 18.));
        Thread.sleep(100);
        oItems.add(new HistoryItem(30., AAction.createByName(ActionSub.NAME), 2., 28.));
        Thread.sleep(100);
        oItems.add(new HistoryItem(40., AAction.createByName(ActionSub.NAME), 2., 38.));
        Thread.sleep(100);
        oItems.add(new HistoryItem(50., AAction.createByName(ActionSub.NAME), 2., 48.));
        oItems.remove(0);
        Thread.sleep(100);
        Collection<HistoryItem> all = historyItemDAO.getByUserId(-22);
        /*for (HistoryItem item : all){
            System.out.println(item);
        }*/
        try{
            Assertions.assertEquals(all, oItems);
        } finally {
            oItems.clear();
        }
    }
}
