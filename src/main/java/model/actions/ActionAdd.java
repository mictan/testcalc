package model.actions;

import java.math.BigDecimal;

public class ActionAdd extends AAction{
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.add(right);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "+";
}
