package model.data;

import model.actions.AAction;

public class HistoryItem {
    private final Value left;
    private final AAction action;
    private final Value right;
    private final Value result;

    public HistoryItem(Value left, AAction action, Value right, Value result) {
        this.left = left;
        this.action = action;
        this.right = right;
        this.result = result;
    }

    public Value getLeft() {
        return left;
    }

    public AAction getAction() {
        return action;
    }

    public Value getRight() {
        return right;
    }

    public Value getResult() {
        return result;
    }
}
