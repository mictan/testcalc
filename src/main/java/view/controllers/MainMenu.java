package view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.database.UserDatabaseFacade;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    @FXML private Menu menuUserRoot;
    @FXML private MenuItem menuUserLogin;

    @FXML private MenuItem menuAbout;

    private UserDatabaseFacade userFacade = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
