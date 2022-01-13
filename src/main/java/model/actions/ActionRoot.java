package model.actions;

import model.data.Value;

public class ActionRoot extends AAction{
    @Override
    public Value execute(Value left, Value right) {
        if(right.get() == 0){
            return new Value(Double.NaN);
        }
        return new Value(Math.pow(left.get(), 1.0 / right.get()));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String buildMathExpression(String left, String right) {
        return right + " " + NAME + " " + left;
    }

    public static final String NAME = "\u8730";
}
