package view.dialogs;

import javafx.application.Platform;
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

public class LoginDialog extends ADialog<Pair<String, String>> {
    private final ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
    private TextField username = null;
    private PasswordField password = null;

    @Override
    protected void initDialog() {
        setTitle("Login");
        Node loginButton = lookupButton(loginButtonType);
        loginButton.setDisable(true);
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        Platform.runLater(username::requestFocus);
    }

    @Override
    protected Collection<ButtonType> getButtonTypes() {
        return Arrays.asList(loginButtonType, ButtonType.CANCEL);
    }

    @Override
    protected Node createContent() {
        try {
            FXMLLoader inputsLoader = new FXMLLoader(getClass().getResource("/view/LoginDialog.fxml"));
            Node inputs = inputsLoader.load();
            username = (TextField) inputs.lookup("#usernameField");
            password = (PasswordField) inputs.lookup("#passwordField");
            return inputs;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Callback<ButtonType, Pair<String, String>> getResultConverter() {
        return dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        };
    }
}
