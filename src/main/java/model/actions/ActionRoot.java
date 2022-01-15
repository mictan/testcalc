package model.actions;

public class ActionRoot extends AAction{
    @Override
    public Double execute(Double left, Double right) {
        if(right == 0){
            return 1.0;
        }
        return Math.pow(left, 1.0 / right);
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
