package model.actions;

public class ActionAdd extends AAction{
    @Override
    public Double execute(Double left, Double right) {
        return left + right;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "+";
}
