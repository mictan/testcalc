package model.data;

import model.actions.AAction;

public class HistoryItem {
    private final Double left;
    private final AAction action;
    private final Double right;
    private final Double result;

    public HistoryItem(Double left, AAction action, Double right, Double result) {
        this.left = left;
        this.action = action;
        this.right = right;
        this.result = result;
    }

    public Double getLeft() {
        return left;
    }

    public AAction getAction() {
        return action;
    }

    public Double getRight() {
        return right;
    }

    public Double getResult() {
        return result;
    }
}
