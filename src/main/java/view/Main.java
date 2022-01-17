package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Calculator;
import model.History;
import model.actions.AAction;
import model.database.HistoryDatabaseProvider;
import model.database.UserDatabaseProvider;
import model.helpers.FinalizePool;
import view.controllers.MainWindow;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AAction.init();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            MainWindow mainController = loader.getController();
            Calculator calculatorModel = new Calculator();
            UserMediator userMediator = new UserMediator(new UserDatabaseProvider());
            HistoryDatabaseProvider historyProvider = new HistoryDatabaseProvider();
            historyProvider.userIdProperty().bind(userMediator.createCurrentUserIdBinding());
            History history = new History().setDatabase(historyProvider);
            calculatorModel.historyProperty().set(history);
            mainController.setCalculatorModel(calculatorModel);
            mainController.setUserMediator(userMediator);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Calculator");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        FinalizePool.getInstance().applyFinalize();
    }
}
