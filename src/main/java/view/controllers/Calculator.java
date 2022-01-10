package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.actions.*;

import java.math.BigDecimal;
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

    private model.Calculator calculatorModel = null;
    private StringBinding leftArgAdapter = null;
    private StringBinding rightArgAdapter = null;
    private StringBinding mathExpressionAdapter = null;
    private StringBinding resultAdapter = null;
    private final SimpleStringProperty input = new SimpleStringProperty();
    private StringBinding output = createOutputStringBinding(input, null);

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
            output = createOutputStringBinding(input, resultAdapter);
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
    }

    private void onNumberButtonClick(String text){
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "";
        }
        input.setValue(value + text);
    }

    private void onDotButtonClick(){
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "0.";
        } else if(value.indexOf('.') == -1){
            value = value + '.';
        }
        input.setValue(value);
    }

    private void onBackspaceButtonClick(){
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
            return;
        }
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
        if(!resultAdapter.get().isEmpty() || calculatorModel.calculateResultIfComplete()){
            calculatorModel.next();
        }
        if(leftArgAdapter.get().isEmpty()){
            if(!sendInput(calculatorModel.leftArgProperty())) {
                return;
            }
        } else if(rightArgAdapter.get().isEmpty()){
            if(sendInput(calculatorModel.rightArgProperty()) && calculatorModel.calculateResultIfComplete()){
                calculatorModel.next();
            }
        }
        calculatorModel.actionProperty().set(AAction.createByName(actionName));
        //calculatorModel.calculateResultIfComplete();//TODO unary operation?
    }

    private boolean sendInput(ObjectPropertyBase<BigDecimal> target){
        String value = input.getValue();
        if(value == null || value.isEmpty()){
            return false;
        }
        try{
            target.set(new BigDecimal(value));
            input.setValue("");
            return true;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }

    private StringBinding createStringBinding(ReadOnlyObjectProperty<BigDecimal> property){
        return Bindings.createStringBinding(() -> {
            BigDecimal left = property.get();
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

    private StringBinding createOutputStringBinding(ReadOnlyStringProperty input, StringBinding result){
        if(result == null){
            return Bindings.createStringBinding(input::get, input);
        } else {
            return Bindings.createStringBinding(() -> {
                if(result.get() != null && !result.get().isEmpty()) {
                    return result.get();
                } else {
                    return input.get();
                }
            }, input, result);
        }
    }
}
