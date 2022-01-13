package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import model.ICalculatorInput;
import model.SimpleCalculatorOutputWrapper;
import model.actions.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Calculator implements Initializable {
    @FXML private Region calculatorRoot;
    @FXML private TextField outputCalcState;
    @FXML private TextField outputCalcNum;
    @FXML private GridPane buttons;
    @FXML private Button buttonAdd;
    @FXML private Button buttonSub;
    @FXML private Button buttonMul;
    @FXML private Button buttonDiv;
    @FXML private Button buttonInvert;
    @FXML private Button buttonRoot;
    @FXML private Button buttonPow;
    @FXML private Button buttonEq;
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Button button0;
    @FXML private Button buttonSign;
    @FXML private Button buttonDot;
    @FXML private Button buttonC;
    @FXML private Button buttonCE;
    @FXML private Button buttonBackspace;
    @FXML private Button buttonToggleHistory;

    private ICalculatorInput inputAdapter = null;
    private SimpleCalculatorOutputWrapper outputAdapter = null;
    private StringBinding output = null;

    private BooleanProperty openHistory;
    private ReadOnlyBooleanProperty historyOpened;

    public void historyControl(BooleanProperty openHistory, ReadOnlyBooleanProperty historyOpened){
        this.openHistory = openHistory;
        this.historyOpened = historyOpened;
        if(buttonToggleHistory != null){
            initButtonToggleHistory();
        }
    }

    public void setCalculator(model.Calculator calculator, ICalculatorInput inputAdapter){
        if(output != null){
            output.dispose();
        }
        outputCalcState.textProperty().unbind();
        this.inputAdapter = inputAdapter;
        if(inputAdapter != null && calculator != null){
            this.outputAdapter = new SimpleCalculatorOutputWrapper(calculator);
            if (outputCalcState != null) {
                outputCalcState.textProperty().bind(outputAdapter.mathExpressionProperty());
            }
            output = createOutputStringBinding(inputAdapter.inputProperty(), outputAdapter.resultProperty(), inputAdapter.displayInputProperty());
            if (outputCalcNum != null) {
                outputCalcNum.textProperty().bind(output);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(output != null){
            outputCalcNum.textProperty().bind(output);
        }
        if(outputAdapter != null){
            outputCalcState.textProperty().bind(outputAdapter.mathExpressionProperty());
        }
        button1.setOnAction(event -> onNumberButtonClick("1"));
        button2.setOnAction(event -> onNumberButtonClick("2"));
        button3.setOnAction(event -> onNumberButtonClick("3"));
        button4.setOnAction(event -> onNumberButtonClick("4"));
        button5.setOnAction(event -> onNumberButtonClick("5"));
        button6.setOnAction(event -> onNumberButtonClick("6"));
        button7.setOnAction(event -> onNumberButtonClick("7"));
        button8.setOnAction(event -> onNumberButtonClick("8"));
        button9.setOnAction(event -> onNumberButtonClick("9"));
        button0.setOnAction(event -> onNumberButtonClick("0"));
        buttonSign.setOnAction(event -> onSignButtonClick());
        buttonDot.setOnAction(event -> onDotButtonClick());
        buttonBackspace.setOnAction(event -> onBackspaceButtonClick());
        buttonAdd.setOnAction(event -> onActionButtonClick(ActionAdd.NAME));
        buttonSub.setOnAction(event -> onActionButtonClick(ActionSub.NAME));
        buttonMul.setOnAction(event -> onActionButtonClick(ActionMul.NAME));
        buttonDiv.setOnAction(event -> onActionButtonClick(ActionDiv.NAME));
        buttonInvert.setOnAction(event -> onInvertButtonClick());
        buttonRoot.setOnAction(event -> onActionButtonClick(ActionRoot.NAME));
        buttonPow.setOnAction(event -> onActionButtonClick(ActionPow.NAME));
        buttonEq.setOnAction(event -> onEqButtonClick());
        buttonC.setOnAction(event -> onCClick());
        buttonCE.setOnAction(event -> onCEClick());
        if(openHistory != null && historyOpened != null){
            initButtonToggleHistory();
        }
    }

    private void onNumberButtonClick(String text){
        inputAdapter.onNumberClick(text);
    }

    private void onDotButtonClick(){
        inputAdapter.onDotClick();
    }

    private void onBackspaceButtonClick(){
        inputAdapter.onBackspaceClick();
    }

    private void onSignButtonClick(){
        inputAdapter.onSignClick();
    }

    private void onActionButtonClick(String actionName){
        inputAdapter.onActionClick(actionName);
    }

    private void onInvertButtonClick(){
        inputAdapter.onInvertClick();
    }

    private void onCClick(){
        inputAdapter.onCClick();
    }

    private void onCEClick(){
        inputAdapter.onCEClick();
    }

    private void onEqButtonClick(){
        inputAdapter.onEqClick();
    }

    private StringBinding createOutputStringBinding(ReadOnlyStringProperty input, StringBinding result, ReadOnlyBooleanProperty displayInput){
        return Bindings.createStringBinding(() -> {
            if(displayInput.get() || result.get() == null) {
                return input.get();
            } else {
                return result.get();
            }
        }, input, result, displayInput);
    }

    private void initButtonToggleHistory(){
        historyOpened.addListener((observable, oldValue, newValue) -> buttonToggleHistory.textProperty().set(newValue ? ">>" : "<<" ));
        buttonToggleHistory.setOnAction((event) -> openHistory.set(!historyOpened.get()));
        buttonToggleHistory.textProperty().set(historyOpened.get() ? ">>" : "<<");
    }
}
