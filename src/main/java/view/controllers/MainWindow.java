package view.controllers;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panelsRoot.prefWidthProperty().bind(root.widthProperty());
        panelsRoot.prefHeightProperty().bind(root.heightProperty());
        FXMLLoader calcLoader = new FXMLLoader(getClass().getResource("../Calculator.fxml"));
        try {
            Node calcNode = calcLoader.load();
            calculatorController = calcLoader.getController();
            calculatorController.setCalculatorModel(calculatorModel);
            panelsRoot.getItems().add(calcNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCalculatorModel(model.Calculator model){
        calculatorModel = model;
        if(calculatorController != null){
            calculatorController.setCalculatorModel(calculatorModel);
        }
    }
}
