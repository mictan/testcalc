package model.data;

public class Value {
    private final double value;

    public Value(double value) {
        this.value = value;
    }

    public Value(String value){
        this.value = Double.parseDouble(value);
    }

    public double get(){
        return value;
    }

    @Override
    public String toString() {
        String string = Double.toString(value);
        if(string.endsWith(".0")){
            string = string.substring(0, string.length() - 2);
        }
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof Value){
            return value == ((Value) obj).value;
        } else {
            return false;
        }
    }
}
