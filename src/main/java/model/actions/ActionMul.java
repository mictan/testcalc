package model.actions;

import model.data.Value;

import java.math.BigDecimal;

public class ActionMul extends AAction{
    @Override
    public Value execute(Value left, Value right) {
        return new Value(left.get() * right.get());
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "*";
}
