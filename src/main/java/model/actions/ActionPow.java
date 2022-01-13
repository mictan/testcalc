package model.actions;

import model.data.Value;

public class ActionPow extends AAction{
    @Override
    public Value execute(Value left, Value right) {
        return new Value(Math.pow(left.get(), right.get()));
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "^";
}
