package model.actions;

import model.data.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActionDiv extends AAction{
    @Override
    public Value execute(Value left, Value right) {
        /*if(right.get().equals(BigDecimal.ZERO)){
            return new Value(BigDecimal.ZERO);//TODO +-infinity / nan
        }
        try {
            return new Value(left.get().divide(right.get()));
        } catch (ArithmeticException e){
            //Бесконечная дробь.
            return new Value(left.get().divide(right.get(), 30, RoundingMode.HALF_UP));
        }*/
        return new Value(left.get() / right.get());
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "/";
}
