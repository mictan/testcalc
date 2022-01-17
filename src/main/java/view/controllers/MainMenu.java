package view.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.data.User;
import view.UserMediator;
import view.dialogs.AboutDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    @FXML private Menu menuUserRoot;
    @FXML private MenuItem menuUserLogin;
    @FXML private MenuItem menuUserRegister;
    @FXML private MenuItem menuUserLogout;

    @FXML private MenuItem menuAbout;

    private UserMediator userMediator = null;
    private final BooleanProperty userRegistered = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuUserLogin.setOnAction(event -> {userMediator.login();});
        menuUserRegister.setOnAction(event -> {userMediator.register();});
        menuUserLogout.setOnAction(event -> {userMediator.logout();});
        menuAbout.setOnAction(event -> {new AboutDialog().showSync();});

        menuUserLogout.visibleProperty().bind(userRegistered);
    }

    public void setUserMediator(UserMediator userMediator){
        this.userMediator = userMediator;
        if(userMediator != null){
            ObjectBinding<User> userBinding = userMediator.createCurrentUserBinding();
            userRegistered.bind(Bindings.createBooleanBinding(() -> userBinding.get() != null, userBinding));
        } else {
            userRegistered.unbind();
            userRegistered.set(false);
        }
    }
}
