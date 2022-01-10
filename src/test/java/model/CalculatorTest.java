package model;


import model.actions.AAction;
import model.actions.ActionAdd;
import model.actions.ActionSub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CalculatorTest {
    @Test
    public void ActionsTest(){
        AAction.init();
        Assertions.assertSame(AAction.createByName(ActionAdd.NAME).getClass(), ActionAdd.class);
        Assertions.assertSame(AAction.createByName(ActionSub.NAME).getClass(), ActionSub.class);
    }
    @Test
    public void test(){
        AAction.init();
        History history = new History();
        Calculator calculator = new Calculator();
        calculator.historyProperty().set(history);
        calculator.leftArgProperty().set(BigDecimal.valueOf(10));//10 null null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.actionProperty().set(AAction.createByName(ActionAdd.NAME));//10 + null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.rightArgProperty().set(BigDecimal.valueOf(5));//10 + 5 = null
        Assertions.assertTrue(calculator.calculateResultIfComplete());//10 + 5 = 15 (-> history)
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.next();//15 null null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.actionProperty().set(AAction.createByName(ActionSub.NAME));//15 - null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.rightArgProperty().set(BigDecimal.valueOf(7));//15 - 7 = null
        Assertions.assertTrue(calculator.calculateResultIfComplete());//15 - 7 = 8 (-> history)
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        Assertions.assertSame(calculator.resultProperty().get(), BigDecimal.valueOf(8));
        Assertions.assertSame(history.getItems().size(), 2);
    }
}
