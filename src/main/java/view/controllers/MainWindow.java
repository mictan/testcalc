package view.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import model.ICalculatorInput;
import model.SimpleCalculatorInputAdapter;
import view.helpers.SPSaveWidthHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    @FXML private AnchorPane root;
    @FXML private SplitPane panelsRoot;

    private model.Calculator calculatorModel = null;
    private ICalculatorInput calculatorInput = null;
    private Calculator calculatorController = null;
    private Region calculatorNode = null;

    private final SimpleObjectProperty<model.History> historyModel = new SimpleObjectProperty<>();
    private History historyController = null;
    private Region historyNode = null;
    private SPSaveWidthHelper historyWidthHelper = null;
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
            calculatorNode = calcLoader.load();
            calculatorController = calcLoader.getController();
            calculatorController.setCalculator(calculatorModel, calculatorInput);
            calculatorController.historyControl(openHistory, historyOpened.getReadOnlyProperty());
            panelsRoot.getItems().add(calculatorNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        historyModel.addListener(historyChangeListener);
        openHistory.addListener(openHistoryChangeListener);
    }

    public void setCalculatorModel(model.Calculator model){
        calculatorModel = model;
        if(model != null){
            calculatorInput = new SimpleCalculatorInputAdapter(model);
            historyModel.bind(model.historyProperty());
        } else {
            calculatorInput = null;
            historyModel.unbind();
            historyModel.set(null);
        }
        if(calculatorController != null){
            calculatorController.setCalculator(calculatorModel, calculatorInput);
        }
    }

    public void openHistory(){
        if(historyNode != null || initHistoryNode()){
            panelsRoot.getItems().add(historyNode);
            historyWidthHelper.afterAddRegion(1);
            historyOpened.set(true);
        }
    }

    public void closeHistory(){
        if(historyNode != null){
            historyWidthHelper.beforeRemoveRegion();
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
            historyWidthHelper = new SPSaveWidthHelper(panelsRoot, historyNode, historyNode.minWidth(panelsRoot.getHeight()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
