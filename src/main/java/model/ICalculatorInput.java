package model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface ICalculatorInput {
    ReadOnlyStringProperty inputProperty();
    ReadOnlyBooleanProperty displayInputProperty();
    void onNumberClick(String text);
    void onDotClick();
    void onBackspaceClick();
    void onSignClick();
    void onActionClick(String actionName);
    void onInvertClick();
    void onCClick();
    void onCEClick();
    void onEqClick();
    boolean tryParseExpression(String expression);
}
