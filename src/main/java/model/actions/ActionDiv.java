package model.actions;

import java.math.BigDecimal;

public class ActionDiv extends AAction{
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.divide(right);
    }//TODO check x/0, check 1/3

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "/";
}
