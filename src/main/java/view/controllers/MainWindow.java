package view.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import model.ICalculatorInput;
import model.SimpleCalculatorInputAdapter;
import view.UserMediator;
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

    private MainMenu menuController = null;
    private UserMediator userMediator = null;

    private final SimpleObjectProperty<model.History> historyModel = new SimpleObjectProperty<>();
    private History historyController = null;
    private SPSaveWidthHelper historyWidthHelper = null;
    private final ReadOnlyBooleanWrapper historyOpened = new ReadOnlyBooleanWrapper(false);
    private final BooleanProperty openHistory = new SimpleBooleanProperty(false);

    public MainWindow() {
        historyModel.addListener((observable, oldValue, newValue) -> {
            if(historyController != null){
                historyController.setHistoryModel(newValue);
            }
        });
        openHistory.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                openHistory();
            } else {
                closeHistory();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMainMenuNode();
        initCalculatorNode();
    }

    private void initCalculatorNode(){
        FXMLLoader calcLoader = new FXMLLoader(getClass().getResource("/view/Calculator.fxml"));
        try {
            Node calculatorNode = calcLoader.load();
            calculatorController = calcLoader.getController();
            calculatorController.setCalculator(calculatorModel, calculatorInput);
            calculatorController.historyControl(openHistory, historyOpened.getReadOnlyProperty());
            panelsRoot.getItems().add(calculatorNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMainMenuNode(){
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        try {
            Region menuNode = menuLoader.load();
            menuController = menuLoader.getController();
            if(userMediator != null){
                menuController.setUserMediator(userMediator);
            }
            menuNode.heightProperty().addListener((observable, oldValue, newValue) -> {
                AnchorPane.setTopAnchor(panelsRoot, menuNode.getHeight());
            });
            root.getChildren().add(menuNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserMediator(UserMediator userMediator){
        this.userMediator = userMediator;
        if(menuController != null){
            menuController.setUserMediator(userMediator);
        }
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
        if(historyController != null || initHistoryNode()){
            panelsRoot.getItems().add(historyController.getHistoryRoot());
            historyWidthHelper.afterAddRegion(1);
            historyOpened.set(true);
        }
    }

    public void closeHistory(){
        if(historyController != null){
            historyWidthHelper.beforeRemoveRegion();
            panelsRoot.getItems().remove(historyController.getHistoryRoot());
            historyOpened.set(false);
        }
    }

    private boolean initHistoryNode(){
        FXMLLoader historyLoader = new FXMLLoader(getClass().getResource("/view/History.fxml"));
        try{
            Region historyNode = historyLoader.load();
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
