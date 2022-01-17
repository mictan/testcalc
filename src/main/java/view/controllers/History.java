package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.data.HistoryItem;

import java.net.URL;
import java.util.ResourceBundle;

public class History implements Initializable {
    @FXML private VBox historyRoot;
    @FXML private Button buttonClear;
    @FXML private TextArea historyView;

    private model.History historyModel = null;
    private ObservableList<HistoryItem> displayItems = null;
    private StringBinding historyAdapter = null;

    public Node getHistoryRoot(){
        return historyRoot;
    }

    public void setHistoryModel(model.History historyModel){
        if(historyAdapter != null){
            historyAdapter.dispose();
        }
        this.historyModel = historyModel;
        if(historyModel != null){
            displayItems = historyModel.getItems();
            historyAdapter = Bindings.createStringBinding(() -> {
                StringBuilder s = new StringBuilder();
                for (int i = displayItems.size() -1; i >= 0; i--){
                    HistoryItem item = displayItems.get(i);
                    String left = HistoryItem.valueToString(item.getLeft());
                    String right = HistoryItem.valueToString(item.getRight());
                    s
                            .append("\r\n")
                            .append(HistoryItem.valueToString(item.getResult()))
                            .append(" = ")
                            .append(item.getAction().buildMathExpression(left, right));
                }
                if(s.length() > 0){
                    s.delete(0, 2);
                }
                return s.toString();
            }, displayItems);
            if(historyView != null){
                historyView.textProperty().bind(historyAdapter);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(historyAdapter != null){
            historyView.textProperty().bind(historyAdapter);
        }
        historyView.prefWidthProperty().bind(historyRoot.widthProperty());
        historyView.prefHeightProperty().bind(historyRoot.heightProperty());
        buttonClear.setOnAction(event -> clearHistory());
    }

    private void clearHistory(){
        if(historyModel == null){
            return;
        }
        historyModel.clearItems();
    }
}
