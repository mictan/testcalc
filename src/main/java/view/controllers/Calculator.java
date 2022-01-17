package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import model.ICalculatorInput;
import model.SimpleCalculatorOutputWrapper;
import model.actions.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.*;

public class Calculator implements Initializable {
    @FXML private Region calculatorRoot;
    @FXML private TextField outputCalcState;
    @FXML private TextField outputCalcNum;
    @FXML private GridPane buttons;
    @FXML private Button buttonAdd;
    @FXML private Button buttonSub;
    @FXML private Button buttonMul;
    @FXML private Button buttonDiv;
    @FXML private Button buttonInvert;
    @FXML private Button buttonRoot;
    @FXML private Button buttonPow;
    @FXML private Button buttonEq;
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Button button0;
    @FXML private Button buttonSign;
    @FXML private Button buttonDot;
    @FXML private Button buttonC;
    @FXML private Button buttonCE;
    @FXML private Button buttonBackspace;
    @FXML private Button buttonToggleHistory;

    private ICalculatorInput inputAdapter = null;
    private SimpleCalculatorOutputWrapper outputAdapter = null;
    private StringBinding output = null;

    private BooleanProperty openHistory;
    private ReadOnlyBooleanProperty historyOpened;

    private HashMap<KeyCode, Runnable> keyBindings = new HashMap<>();
    private HashMap<KeyCode, Runnable> keyBindingsShift = new HashMap<>();

    public void historyControl(BooleanProperty openHistory, ReadOnlyBooleanProperty historyOpened){
        this.openHistory = openHistory;
        this.historyOpened = historyOpened;
        if(buttonToggleHistory != null){
            initButtonToggleHistory();
        }
    }

    public void setCalculator(model.Calculator calculator, ICalculatorInput inputAdapter){
        if(output != null){
            output.dispose();
        }
        outputCalcState.textProperty().unbind();
        this.inputAdapter = inputAdapter;
        if(inputAdapter != null && calculator != null){
            this.outputAdapter = new SimpleCalculatorOutputWrapper(calculator);
            if (outputCalcState != null) {
                outputCalcState.textProperty().bind(outputAdapter.mathExpressionProperty());
            }
            output = createOutputStringBinding(inputAdapter.inputProperty(), outputAdapter.resultProperty(), inputAdapter.displayInputProperty());
            if (outputCalcNum != null) {
                outputCalcNum.textProperty().bind(output);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(output != null){
            outputCalcNum.textProperty().bind(output);
        }
        if(outputAdapter != null){
            outputCalcState.textProperty().bind(outputAdapter.mathExpressionProperty());
        }
        button1.setOnAction(event -> onNumberButtonClick("1"));
        button2.setOnAction(event -> onNumberButtonClick("2"));
        button3.setOnAction(event -> onNumberButtonClick("3"));
        button4.setOnAction(event -> onNumberButtonClick("4"));
        button5.setOnAction(event -> onNumberButtonClick("5"));
        button6.setOnAction(event -> onNumberButtonClick("6"));
        button7.setOnAction(event -> onNumberButtonClick("7"));
        button8.setOnAction(event -> onNumberButtonClick("8"));
        button9.setOnAction(event -> onNumberButtonClick("9"));
        button0.setOnAction(event -> onNumberButtonClick("0"));
        buttonSign.setOnAction(event -> onSignButtonClick());
        buttonDot.setOnAction(event -> onDotButtonClick());
        buttonBackspace.setOnAction(event -> onBackspaceButtonClick());
        buttonAdd.setOnAction(event -> onActionButtonClick(ActionAdd.NAME));
        buttonSub.setOnAction(event -> onActionButtonClick(ActionSub.NAME));
        buttonMul.setOnAction(event -> onActionButtonClick(ActionMul.NAME));
        buttonDiv.setOnAction(event -> onActionButtonClick(ActionDiv.NAME));
        buttonInvert.setOnAction(event -> onInvertButtonClick());
        buttonRoot.setOnAction(event -> onActionButtonClick(ActionRoot.NAME));
        buttonPow.setOnAction(event -> onActionButtonClick(ActionPow.NAME));
        buttonEq.setOnAction(event -> onEqButtonClick());
        buttonC.setOnAction(event -> onCClick());
        buttonCE.setOnAction(event -> onCEClick());
        if(openHistory != null && historyOpened != null){
            initButtonToggleHistory();
        }
        calculatorRoot.setOnKeyPressed(this::onKeyPressed);
        outputCalcState.setOnKeyPressed(this::onKeyPressed);
        outputCalcNum.setOnKeyPressed(this::onKeyPressed);
        initKeyBindings();
    }

    private void onNumberButtonClick(String text){
        inputAdapter.onNumberClick(text);
    }

    private void onDotButtonClick(){
        inputAdapter.onDotClick();
    }

    private void onBackspaceButtonClick(){
        inputAdapter.onBackspaceClick();
    }

    private void onSignButtonClick(){
        inputAdapter.onSignClick();
    }

    private void onActionButtonClick(String actionName){
        inputAdapter.onActionClick(actionName);
    }

    private void onInvertButtonClick(){
        inputAdapter.onInvertClick();
    }

    private void onCClick(){
        inputAdapter.onCClick();
    }

    private void onCEClick(){
        inputAdapter.onCEClick();
    }

    private void onEqButtonClick(){
        inputAdapter.onEqClick();
    }

    private void onKeyPressed(KeyEvent event){
        System.out.println(event);
        KeyCode code = event.getCode();
        if(event.isShiftDown() && keyBindingsShift.containsKey(code)) {
            keyBindingsShift.get(code).run();
        } else if(keyBindings.containsKey(code)){
            keyBindings.get(code).run();
        } else {
            if(event.isControlDown()){
                if(code == V){
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    try {
                        Object data = clipboard.getData(DataFlavor.stringFlavor);
                        if (data instanceof String){
                            inputAdapter.tryParseExpression((String) data);
                        }
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                } else if(code == C){
                    if(event.getTarget() == outputCalcState || event.getTarget() == outputCalcNum){//Они сами обрабатываются
                        return;
                    }
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    String s = outputCalcNum.getText();
                    clipboard.setContents(new StringSelection(s), null);
                }
            }
        }
    }

    private void initKeyBindings(){
        keyBindings.put(EQUALS, this::onEqButtonClick);
        keyBindings.put(ENTER, keyBindings.get(EQUALS));
        keyBindings.put(DIGIT0, ()->{onNumberButtonClick("0");});
        keyBindings.put(DIGIT1, ()->{onNumberButtonClick("1");});
        keyBindings.put(DIGIT2, ()->{onNumberButtonClick("2");});
        keyBindings.put(DIGIT3, ()->{onNumberButtonClick("3");});
        keyBindings.put(DIGIT4, ()->{onNumberButtonClick("4");});
        keyBindings.put(DIGIT5, ()->{onNumberButtonClick("5");});
        keyBindings.put(DIGIT6, ()->{onNumberButtonClick("6");});
        keyBindings.put(DIGIT7, ()->{onNumberButtonClick("7");});
        keyBindings.put(DIGIT8, ()->{onNumberButtonClick("8");});
        keyBindings.put(DIGIT9, ()->{onNumberButtonClick("9");});
        keyBindings.put(NUMPAD0, keyBindings.get(DIGIT0));
        keyBindings.put(NUMPAD1, keyBindings.get(DIGIT1));
        keyBindings.put(NUMPAD2, keyBindings.get(DIGIT2));
        keyBindings.put(NUMPAD3, keyBindings.get(DIGIT3));
        keyBindings.put(NUMPAD4, keyBindings.get(DIGIT4));
        keyBindings.put(NUMPAD5, keyBindings.get(DIGIT5));
        keyBindings.put(NUMPAD6, keyBindings.get(DIGIT6));
        keyBindings.put(NUMPAD7, keyBindings.get(DIGIT7));
        keyBindings.put(NUMPAD8, keyBindings.get(DIGIT8));
        keyBindings.put(NUMPAD9, keyBindings.get(DIGIT9));
        keyBindings.put(ADD, () -> {onActionButtonClick(ActionAdd.NAME);});
        keyBindings.put(PLUS, keyBindings.get(ADD));
        keyBindings.put(SUBTRACT, () -> {onActionButtonClick(ActionSub.NAME);});
        keyBindings.put(MINUS, keyBindings.get(SUBTRACT));
        keyBindings.put(DIVIDE, () -> {onActionButtonClick(ActionDiv.NAME);});
        keyBindings.put(SLASH, keyBindings.get(DIVIDE));
        keyBindings.put(MULTIPLY, () -> {onActionButtonClick(ActionMul.NAME);});
        keyBindings.put(STAR, keyBindings.get(MULTIPLY));
        keyBindings.put(COMMA, this::onDotButtonClick);
        keyBindings.put(PERIOD, this::onDotButtonClick);
        keyBindings.put(BACK_SPACE, this::onBackspaceButtonClick);
        keyBindingsShift.put(DIGIT6, () -> {onActionButtonClick(ActionPow.NAME);});
        keyBindingsShift.put(DIGIT8, keyBindings.get(MULTIPLY));
        keyBindingsShift.put(EQUALS, keyBindings.get(ADD));
        keyBindingsShift.put(BACK_SLASH, keyBindings.get(DIVIDE));
    }

    private StringBinding createOutputStringBinding(ReadOnlyStringProperty input, StringBinding result, ReadOnlyBooleanProperty displayInput){
        return Bindings.createStringBinding(() -> {
            if(displayInput.get() || result.get() == null) {
                return input.get();
            } else {
                return result.get();
            }
        }, input, result, displayInput);
    }

    private void initButtonToggleHistory(){
        historyOpened.addListener((observable, oldValue, newValue) -> buttonToggleHistory.textProperty().set(newValue ? ">>" : "<<" ));
        buttonToggleHistory.setOnAction((event) -> openHistory.set(!historyOpened.get()));
        buttonToggleHistory.textProperty().set(historyOpened.get() ? ">>" : "<<");
    }
}
