package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import model.actions.AAction;
import model.actions.ActionDiv;
import model.data.Value;

public class SimpleCalculatorInputAdapter implements ICalculatorInput {

    private final model.Calculator calculatorModel;
    private final SimpleStringProperty input = new SimpleStringProperty();
    private final BooleanProperty displayInput = new SimpleBooleanProperty(false);

    public SimpleCalculatorInputAdapter(Calculator calculatorModel) {
        this.calculatorModel = calculatorModel;
    }

    public SimpleStringProperty inputProperty() {
        return input;
    }

    public BooleanProperty displayInputProperty() {
        return displayInput;
    }

    @Override
    public void onNumberClick(String text){
        displayInput.set(true);
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "";
        }
        input.setValue(value + text);
    }

    @Override
    public void onDotClick(){
        displayInput.set(true);
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            value = "0.";
        } else if(value.indexOf('.') == -1){
            value = value + '.';
        }
        input.setValue(value);
    }

    @Override
    public void onBackspaceClick(){
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

    @Override
    public void onSignClick(){
        String value = input.getValue();
        if(value == null || value.isEmpty() || value.equals("0")){
            Value lastResult = calculatorModel.resultProperty().get();//send result to input
            if(lastResult != null){
                value = lastResult.toString();//send result to input
            }
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

    @Override
    public void onActionClick(String actionName){
        if(calculatorModel == null){
            return;
        }
        if(sendInputAndCalculateResultIfComplete(true)){
            calculatorModel.actionProperty().set(AAction.createByName(actionName));
        }
        //calculatorModel.calculateResultIfComplete();//TODO unary operation?
    }

    @Override
    public void onInvertClick(){
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

    @Override
    public void onCClick(){
        if(calculatorModel == null){
            return;
        }
        input.setValue("");
        calculatorModel.reset();
    }

    @Override
    public void onCEClick(){
        if(!input.getValue().isEmpty()){
            input.setValue("");
        } else {
            calculatorModel.reset();
        }
    }

    @Override
    public void onEqClick(){
        if(calculatorModel == null){
            return;
        }
        sendInputAndCalculateResultIfComplete(false);
    }

    private boolean sendInputAndCalculateResultIfComplete(boolean next){
        if(calculatorModel == null){
            return false;
        }
        if(calculatorModel.resultProperty().get() != null || calculatorModel.calculateResultIfComplete()){
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
        if(calculatorModel.leftArgProperty().get() == null){
            return sendInput(calculatorModel.leftArgProperty());
        } else if(calculatorModel.rightArgProperty().get() == null){
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
}
