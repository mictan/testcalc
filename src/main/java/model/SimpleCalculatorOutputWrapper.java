package model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import model.actions.AAction;

import java.io.Closeable;

public class SimpleCalculatorOutputWrapper implements Closeable {

    private final StringBinding leftArgAdapter;
    private final StringBinding rightArgAdapter;
    private final StringBinding mathExpressionAdapter;
    private final StringBinding resultAdapter;

    public SimpleCalculatorOutputWrapper(Calculator calculatorModel) {
        leftArgAdapter = createStringBinding(calculatorModel.leftArgProperty());
        rightArgAdapter = createStringBinding(calculatorModel.rightArgProperty());
        mathExpressionAdapter = createMathExpressionStringBinding(leftArgAdapter, calculatorModel.actionProperty(), rightArgAdapter);
        resultAdapter = createStringBinding(calculatorModel.resultProperty());
    }

    public StringBinding mathExpressionProperty() {
        return mathExpressionAdapter;
    }

    public StringBinding resultProperty() {
        return resultAdapter;
    }

    @Override
    public void close(){
        leftArgAdapter.dispose();
        rightArgAdapter.dispose();
        mathExpressionAdapter.dispose();
        resultAdapter.dispose();
    }

    private static StringBinding createStringBinding(ReadOnlyObjectProperty<Double> property){
        return Bindings.createStringBinding(() -> {
            Double left = property.get();
            if(left != null){
                return left.toString();
            } else {
                return "";
            }
        }, property);
    }

    private static StringBinding createMathExpressionStringBinding(StringBinding left, ReadOnlyObjectProperty<AAction> action, StringBinding right){
        return Bindings.createStringBinding(() -> {
            if(action.get() == null){
                return left.get();
            } else {
                return action.get().buildMathExpression(left.get(), right.get());
            }
        }, left, action, right);
    }
}
