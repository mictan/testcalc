package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.actions.*;
import model.data.Value;

import java.net.URL;
import java.util.ResourceBundle;

public class Calculator implements Initializable {
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

    private model.Calculator calculatorModel = null;
    private StringBinding leftArgAdapter = null;
    private StringBinding rightArgAdapter = null;
    private StringBinding mathExpressionAdapter = null;
    private StringBinding resultAdapter = null;
    private final SimpleStringProperty input = new SimpleStringProperty();
    private final BooleanProperty displayInput = new SimpleBooleanProperty(false);
    private StringBinding output = createOutputStringBinding(input, null, displayInput);

    private BooleanProperty openHistory;
    private ReadOnlyBooleanProperty historyOpened;

    public void historyControl(BooleanProperty openHistory, ReadOnlyBooleanProperty historyOpened){
        this.openHistory = openHistory;
        this.historyOpened = historyOpened;
        if(buttonToggleHistory != null){
            initButtonToggleHistory();
        }
    }

    public void setCalculatorModel(model.Calculator calculatorModel){
        if(leftArgAdapter != null){
            leftArgAdapter.dispose();
        }
        if(rightArgAdapter != null){
            rightArgAdapter.dispose();
        }
        if(mathExpressionAdapter != null){
            mathExpressionAdapter.dispose();
        }
        if(resultAdapter != null){
            resultAdapter.dispose();
        }
        output.dispose();
        this.calculatorModel = calculatorModel;
        if(calculatorModel != null) {
            leftArgAdapter = createStringBinding(calculatorModel.leftArgProperty());
            rightArgAdapter = createStringBinding(calculatorModel.rightArgProperty());
            mathExpressionAdapter = createMathExpressionStringBinding(leftArgAdapter, calculatorModel.actionProperty(), rightArgAdapter);
            resultAdapter = createStringBinding(calculatorModel.resultProperty());
            output = createOutputStringBinding(input, resultAdapter, displayInput);
            if (outputCalcState != null) {
                outputCalcState.textProperty().bind(mathExpressionAdapter);
            }
            if (outputCalcNum != null) {
                outputCalcNum.textProperty().bind(output);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        outputCalcNum.textProperty().bind(output);
        if(mathExpressionAdapter != null){
            outputCalcState.textProperty().bind(mathExpressionAdapter);
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
        displayInput.set(true);
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "";
        }
        input.setValue(value + text);
    }

    private void onDotButtonClick(){
        displayInput.set(true);
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "0.";
        } else if(value.indexOf('.') == -1){
            value = value + '.';
        }
        input.setValue(value);
    }

    private void onBackspaceButtonClick(){
        displayInput.set(true);
        String value = input.getValue();
        if(value == null || value.isEmpty()){
            value = "";
        } else {
            value = value.substring(0, value.length() - 1);
            if(value.equals("-")){
                value = "";
            }
        }
        input.setValue(value);
    }

    private void onSignButtonClick(){
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = output.getValue();//send result to input
        }
        if(value == null || value.isEmpty() || value.equals("0")){
            return;
        }
        displayInput.set(true);
        if(value.charAt(0) == '-'){
            value = value.substring(1);
        } else {
            value = "-" + value;
        }
        input.setValue(value);
    }

    private void onActionButtonClick(String actionName){
        if(calculatorModel == null){
            return;
        }
        if(sendInputAndCalculateResultIfComplete(true)){
            calculatorModel.actionProperty().set(AAction.createByName(actionName));
        }
        //calculatorModel.calculateResultIfComplete();//TODO unary operation?
    }

    private void onInvertButtonClick(){
        if(calculatorModel == null){
            return;
        }
        if(calculatorModel.resultProperty().get() == null && calculatorModel.leftArgProperty().get() != null){
            model.Calculator.State state = calculatorModel.saveState();
            calculatorModel.reset();
            if(sendInput(calculatorModel.rightArgProperty())){
                calculatorModel.leftArgProperty().set(new Value(1));
                calculatorModel.actionProperty().set(AAction.createByName(ActionDiv.NAME));
                if(calculatorModel.calculateResultIfComplete()){
                    input.setValue(calculatorModel.resultProperty().get().toString());
                    displayInput.set(true);
                }
            }
            calculatorModel.loadState(state);
        } else {
            if(displayInput.get()){
                calculatorModel.reset();
                sendInput(calculatorModel.rightArgProperty());
            } else {
                calculatorModel.next();
                calculatorModel.rightArgProperty().set(calculatorModel.leftArgProperty().get());
            }
            calculatorModel.leftArgProperty().set(new Value(1));
            calculatorModel.actionProperty().set(AAction.createByName(ActionDiv.NAME));
            calculatorModel.calculateResultIfComplete();
        }
    }

    private void onCClick(){
        if(calculatorModel == null){
            return;
        }
        input.setValue("");
        calculatorModel.reset();
    }

    private void onCEClick(){
        if(!input.getValue().isEmpty()){
            input.setValue("");
        } else {
            calculatorModel.reset();
        }
    }

    private void onEqButtonClick(){
        if(calculatorModel == null){
            return;
        }
        sendInputAndCalculateResultIfComplete(false);
    }

    private boolean sendInputAndCalculateResultIfComplete(boolean next){
        if(calculatorModel == null){
            return false;
        }
        if(!resultAdapter.get().isEmpty() || calculatorModel.calculateResultIfComplete()){
            if(next){
                if(displayInput.get()){//Если мы начали печатать поверх старого результата то сбрасываем его.
                    calculatorModel.reset();
                    return sendInput(calculatorModel.leftArgProperty());
                } else {
                    calculatorModel.next();
                }
            }
            return true;
        }
        if(leftArgAdapter.get().isEmpty()){
            return sendInput(calculatorModel.leftArgProperty());
        } else if(rightArgAdapter.get().isEmpty()){
            if(sendInput(calculatorModel.rightArgProperty()) && calculatorModel.calculateResultIfComplete() && next){
                calculatorModel.next();
            }
        }
        return true;
    }

    private boolean sendInput(ObjectPropertyBase<Value> target){
        String value = input.getValue();
        if(value == null || value.isEmpty()){
            return false;
        }
        try{
            target.set(new Value(value));
            input.setValue("");
            displayInput.set(false);
            return true;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }

    private StringBinding createStringBinding(ReadOnlyObjectProperty<Value> property){
        return Bindings.createStringBinding(() -> {
            Value left = property.get();
            if(left != null){
                return left.toString();
            } else {
                return "";
            }
        }, property);
    }

    private StringBinding createMathExpressionStringBinding(StringBinding left, ReadOnlyObjectProperty<AAction> action, StringBinding right){
        return Bindings.createStringBinding(() -> {
            if(action.get() == null){
                return left.get();
            } else {
                return action.get().buildMathExpression(left.get(), right.get());
            }
        }, left, action, right);
    }

    private StringBinding createOutputStringBinding(ReadOnlyStringProperty input, StringBinding result, BooleanProperty displayInput){
        if(result == null){
            return Bindings.createStringBinding(input::get, input);
        } else {
            return Bindings.createStringBinding(() -> {
                if(displayInput.get() || result.get() == null) {
                    return input.get();
                } else {
                    return result.get();
                }
            }, input, result, displayInput);
        }
    }

    private void initButtonToggleHistory(){
        historyOpened.addListener((observable, oldValue, newValue) -> buttonToggleHistory.textProperty().set(newValue ? ">>" : "<<" ));
        buttonToggleHistory.setOnAction((event) -> openHistory.set(!historyOpened.get()));
        buttonToggleHistory.textProperty().set(historyOpened.get() ? ">>" : "<<");
    }
}
