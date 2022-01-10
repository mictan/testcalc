package view.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    @FXML private AnchorPane root;
    @FXML private SplitPane panelsRoot;

    private model.Calculator calculatorModel = null;
    private Calculator calculatorController = null;

    private final SimpleObjectProperty<model.History> historyModel = new SimpleObjectProperty<>();
    private History historyController = null;
    private Node historyNode = null;
    private final ReadOnlyBooleanWrapper historyOpened = new ReadOnlyBooleanWrapper(false);
    private final BooleanProperty openHistory = new SimpleBooleanProperty(false);
    private final ChangeListener<model.History> historyChangeListener = (observable, oldValue, newValue) -> {
        if(historyController != null){
            historyController.setHistoryModel(newValue);
        }
    };
    private final ChangeListener<Boolean> openHistoryChangeListener = (observable, oldValue, newValue) -> {
        if(newValue){
            openHistory();
        } else {
            closeHistory();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panelsRoot.prefWidthProperty().bind(root.widthProperty());
        panelsRoot.prefHeightProperty().bind(root.heightProperty());
        FXMLLoader calcLoader = new FXMLLoader(getClass().getResource("../Calculator.fxml"));
        try {
            Node calcNode = calcLoader.load();
            calculatorController = calcLoader.getController();
            calculatorController.setCalculatorModel(calculatorModel);
            calculatorController.historyControl(openHistory, historyOpened.getReadOnlyProperty());
            panelsRoot.getItems().add(calcNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        historyModel.addListener(historyChangeListener);
        openHistory.addListener(openHistoryChangeListener);
    }

    public void setCalculatorModel(model.Calculator model){
        calculatorModel = model;
        if(calculatorController != null){
            calculatorController.setCalculatorModel(calculatorModel);
        }
        if(model != null){
            historyModel.bind(model.historyProperty());
        } else {
            historyModel.unbind();
            historyModel.set(null);
        }
    }

    public void openHistory(){
        if(historyNode != null || initHistoryNode()){
            panelsRoot.getItems().add(historyNode);
            historyOpened.set(true);
        }
    }

    public void closeHistory(){
        if(historyNode != null){
            panelsRoot.getItems().remove(historyNode);
            historyOpened.set(false);
        }
    }

    private boolean initHistoryNode(){
        FXMLLoader historyLoader = new FXMLLoader(getClass().getResource("../History.fxml"));
        try{
            historyNode = historyLoader.load();
            historyController = historyLoader.getController();
            historyController.setHistoryModel(historyModel.get());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
