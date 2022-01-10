package model.actions;

import java.math.BigDecimal;

public class ActionMul extends AAction{
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.multiply(right);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "*";
}
