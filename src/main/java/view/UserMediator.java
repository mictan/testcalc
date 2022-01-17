package view;

import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import model.data.User;
import model.database.UserDatabaseProvider;
import view.dialogs.LoginDialog;
import view.dialogs.RegisterDialog;

import java.util.Optional;

public class UserMediator {
    private final UserDatabaseProvider database;

    public UserMediator(UserDatabaseProvider database) {
        this.database = database;
    }

    public ObjectBinding<User> createCurrentUserBinding(){
        return database.createCurrentUserBinding();
    }

    public LongBinding createCurrentUserIdBinding(){
        return database.createCurrentUserIdBinding();
    }

    public void login(){
        Optional<Pair<String, String>> login = new LoginDialog().showSync();
        login.ifPresent(loginPair -> {
            UserDatabaseProvider.LoginResult result = database.login(loginPair.getKey(), loginPair.getValue());
            if(result == UserDatabaseProvider.LoginResult.NOT_FOUND){
                alert("User not found", "User " + loginPair.getKey() + " is not registered");
            } else if(result == UserDatabaseProvider.LoginResult.PASSWORD){
                alert("Incorrect password", "Password doesn't match");
            }
        });
    }

    public void register(){
        Optional<Pair<String, String>> register = new RegisterDialog().showSync();
        register.ifPresent(registerPair -> {
            UserDatabaseProvider.RegisterResult result = database.register(registerPair.getKey(), registerPair.getValue());
            if(result == UserDatabaseProvider.RegisterResult.DUP_NAME){
                alert("User already registered", "User " + registerPair.getKey() + " already registered");
            }
        });
    }

    public void logout(){
        database.logout();
    }

    private void alert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);

        alert.showAndWait();
    }
}
