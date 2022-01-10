package model;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import model.actions.AAction;
import model.data.HistoryItem;

import java.math.BigDecimal;

public class Calculator {
    private SimpleObjectProperty<BigDecimal> leftArg = new SimpleObjectProperty<>();
    private SimpleObjectProperty<AAction> action = new SimpleObjectProperty<>();
    private SimpleObjectProperty<BigDecimal> rightArg = new SimpleObjectProperty<>();
    private ReadOnlyObjectWrapper<BigDecimal> result = new ReadOnlyObjectWrapper<>();

    private SimpleObjectProperty<History> history = new SimpleObjectProperty<>();

    public ObjectPropertyBase<BigDecimal> leftArgProperty(){
        return leftArg;
    }

    public ObjectPropertyBase<AAction> actionProperty(){
        return action;
    }

    public ObjectPropertyBase<BigDecimal> rightArgProperty(){
        return rightArg;
    }

    public ReadOnlyObjectProperty<BigDecimal> resultProperty(){
        return result.getReadOnlyProperty();
    }

    public SimpleObjectProperty<History> historyProperty(){
        return history;
    }

    public boolean calculateResultIfComplete(){
        if(leftArg.get() != null && action.get() != null && rightArg.get() != null && result.get() == null){
            result.set(action.get().execute(leftArg.get(), rightArg.get()));
            if(history.get() != null){
                history.get().addItem(new HistoryItem(leftArg.get(), action.get(), rightArg.get(), result.get()));
            }
            return true;
        } else {
            return false;
        }
    }

    public void applyHistoryItem(HistoryItem item){
        leftArg.set(item.getLeft());
        action.set(item.getAction());
        rightArg.set(item.getRight());
        result.set(item.getResult());
    }

    public void next(){
        leftArg.set(result.get());
        action.set(null);
        rightArg.set(null);
        result.set(null);
    }

    public void reset(){
        leftArg.set(null);
        action.set(null);
        rightArg.set(null);
        result.set(null);
    }
}