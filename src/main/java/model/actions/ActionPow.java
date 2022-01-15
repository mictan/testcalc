package model.actions;

public class ActionPow extends AAction{
    @Override
    public Double execute(Double left, Double right) {
        return Math.pow(left, right);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "^";
}
