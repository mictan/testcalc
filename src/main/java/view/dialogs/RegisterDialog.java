package view.dialogs;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class RegisterDialog extends ADialog<Pair<String, String>> {
    private final ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
    private TextField username = null;
    private PasswordField password = null;
    private PasswordField confirmPassword = null;

    @Override
    protected void initDialog() {
        setTitle("Register");
        Node registerButton = lookupButton(registerButtonType);
        BooleanBinding incorrectLoginBinding = Bindings.createBooleanBinding(
                () -> username.getText().trim().isEmpty(), username.textProperty());
        BooleanBinding incorrectPasswordBinding = Bindings.createBooleanBinding(
                () -> !password.getText().equals(confirmPassword.getText()), password.textProperty(), confirmPassword.textProperty());
        registerButton.disableProperty().bind(incorrectLoginBinding.or(incorrectPasswordBinding));
        Platform.runLater(username::requestFocus);
    }

    @Override
    protected Collection<ButtonType> getButtonTypes() {
        return Arrays.asList(registerButtonType, ButtonType.CANCEL);
    }

    @Override
    protected Node createContent() {
        try {
            FXMLLoader inputsLoader = new FXMLLoader(getClass().getResource("/view/RegisterDialog.fxml"));
            Node inputs = inputsLoader.load();
            username = (TextField) inputs.lookup("#usernameField");
            password = (PasswordField) inputs.lookup("#passwordField");
            confirmPassword = (PasswordField) inputs.lookup("#confirmPasswordField");
            return inputs;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Callback<ButtonType, Pair<String, String>> getResultConverter() {
        return dialogButton -> {
            if (dialogButton == registerButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        };
    }
}
