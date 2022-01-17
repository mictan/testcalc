package model.data;

import model.actions.AAction;
import model.database.converters.ActionToStringConverter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "HistoryItem")
@Table(name = "calc_history")
public class HistoryItem {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId = -1L;
    @Column(name = "time", nullable = false)
    private Timestamp time;
    @Column(name = "left")
    private Double left;
    @Column(name = "action")
    @Convert(converter = ActionToStringConverter.class)
    private AAction action;
    @Column(name = "right")
    private Double right;
    @Column(name = "result")
    private Double result;

    public HistoryItem(){}

    public HistoryItem(Double left, AAction action, Double right, Double result) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.left = left;
        this.action = action;
        this.right = right;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public AAction getAction() {
        return action;
    }

    public void setAction(AAction action) {
        this.action = action;
    }

    public Double getRight() {
        return right;
    }

    public void setRight(Double right) {
        this.right = right;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HistoryItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", time=" + time +
                ", left=" + left +
                ", action=" + action +
                ", right=" + right +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        } else if(obj instanceof HistoryItem){
            HistoryItem o = (HistoryItem) obj;
            return Objects.equals(id, o.id)
                    && Objects.equals(userId, o.userId)
                    && Objects.equals(time, o.time)
                    && Objects.equals(left, o.left)
                    && Objects.equals(action, o.action)
                    && Objects.equals(right, o.right)
                    && Objects.equals(result, o.result);
        } else {
            return false;
        }
    }

    public static String valueToString(Double value){
        if(value == null){
            return "";
        }
        String string = Double.toString(value);
        if(string.endsWith(".0")){
            string = string.substring(0, string.length() - 2);
        }
        return string;
    }
}
