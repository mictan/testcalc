package model.actions;

import model.data.Value;

import java.math.BigDecimal;
import java.util.HashMap;

public abstract class AAction {
    public abstract Value execute(Value left, Value right);
    public abstract String getName();

    public String buildMathExpression(String left, String right){
        return left + ' ' + getName() + ' ' + right;
    }

    @Override
    public String toString() {
        return getName();
    }

    public interface ICreator{
        AAction create();
    }

    private static HashMap<String, ICreator> creators = new HashMap<>();
    private static HashMap<String, AAction> objects = new HashMap<>();

    public static AAction createByName(String name){
        if(objects.containsKey(name)){
            return objects.get(name);
        }
        if(!creators.containsKey(name)){
            throw new IllegalArgumentException("cannot find action by name " + name);
        }
        AAction action = creators.get(name).create();
        objects.put(name, action);
        return action;
   }
   public static void init() {
       creators.put(ActionAdd.NAME, ActionAdd::new);
       creators.put(ActionSub.NAME, ActionSub::new);
       creators.put(ActionMul.NAME, ActionMul::new);
       creators.put(ActionDiv.NAME, ActionDiv::new);
       creators.put(ActionPow.NAME, ActionPow::new);
       creators.put(ActionRoot.NAME, ActionRoot::new);
   }
}
