package view;

import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.util.Pair;
import model.data.User;
import model.database.UserDatabaseFacade;
import view.dialogs.LoginDialog;
import view.dialogs.RegisterDialog;

import java.util.Optional;

public class UserMediator {
    private final UserDatabaseFacade database;

    public UserMediator(UserDatabaseFacade database) {
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
            database.login(loginPair.getKey(), loginPair.getValue());
        });
    }

    public void register(){
        Optional<Pair<String, String>> register = new RegisterDialog().showSync();
        register.ifPresent(registerPair -> {
            database.register(registerPair.getKey(), registerPair.getValue());
        });
    }

    public void logout(){
        database.logout();
    }
}
