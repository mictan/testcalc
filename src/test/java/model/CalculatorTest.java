package model;


import model.actions.AAction;
import model.actions.ActionAdd;
import model.actions.ActionSub;
import model.data.Value;
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
        calculator.leftArgProperty().set(new Value(10));//10 null null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.actionProperty().set(AAction.createByName(ActionAdd.NAME));//10 + null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.rightArgProperty().set(new Value(5));//10 + 5 = null
        Assertions.assertTrue(calculator.calculateResultIfComplete());//10 + 5 = 15 (-> history)
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.next();//15 null null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.actionProperty().set(AAction.createByName(ActionSub.NAME));//15 - null = null
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        calculator.rightArgProperty().set(new Value(7));//15 - 7 = null
        Assertions.assertTrue(calculator.calculateResultIfComplete());//15 - 7 = 8 (-> history)
        Assertions.assertFalse(calculator.calculateResultIfComplete());
        Assertions.assertSame(calculator.resultProperty().get(), BigDecimal.valueOf(8));
        Assertions.assertSame(history.getItems().size(), 2);
    }

    @Test
    public void testExpr(){
        AAction.init();
        Calculator calculator = new Calculator();
        SimpleCalculatorInputAdapter adapter = new SimpleCalculatorInputAdapter(calculator);
        adapter.tryParseExpression("-123.456");
        checkCalculatorState(calculator, -123.456, null, null);
        calculator.reset();
        adapter.tryParseExpression("-123.456 + ");
        checkCalculatorState(calculator, -123.456, AAction.createByName(ActionAdd.NAME), null);
        calculator.reset();
        adapter.tryParseExpression("-123.456 + -123456");
        checkCalculatorState(calculator, -123.456, AAction.createByName(ActionAdd.NAME), -123456.0);
        calculator.reset();
        adapter.tryParseExpression("-123.456");
        checkCalculatorState(calculator, -123.456, null, null);
        adapter.tryParseExpression("+");
        checkCalculatorState(calculator, -123.456, AAction.createByName(ActionAdd.NAME), null);
        adapter.tryParseExpression("-123.456");
        checkCalculatorState(calculator, -123.456, AAction.createByName(ActionAdd.NAME), -123456.0);
    }

    private void checkCalculatorState(Calculator calculator, Double left, AAction action, Double right){
        Value rleft = calculator.leftArgProperty().get();
        AAction raction = calculator.actionProperty().get();
        Value rright = calculator.rightArgProperty().get();
        if(left != null){
            Assertions.assertNotNull(rleft);
            Assertions.assertEquals(rleft.get(), left);
        } else {
            Assertions.assertNull(rleft);
        }
        if(action != null){
            Assertions.assertNotNull(raction);
            Assertions.assertEquals(raction, action);
        } else {
            Assertions.assertNull(raction);
        }
        if(right != null){
            Assertions.assertNotNull(rright);
            Assertions.assertEquals(rright.get(), right);
        } else {
            Assertions.assertNull(rright);
        }
    }
}
