package model.data;

import model.actions.AAction;

import java.math.BigDecimal;

public class HistoryItem {
    private final BigDecimal left;
    private final AAction action;
    private final BigDecimal right;
    private final BigDecimal result;

    public HistoryItem(BigDecimal left, AAction action, BigDecimal right, BigDecimal result) {
        this.left = left;
        this.action = action;
        this.right = right;
        this.result = result;
    }

    public BigDecimal getLeft() {
        return left;
    }

    public AAction getAction() {
        return action;
    }

    public BigDecimal getRight() {
        return right;
    }

    public BigDecimal getResult() {
        return result;
    }
}
