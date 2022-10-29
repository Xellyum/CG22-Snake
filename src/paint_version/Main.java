package paint_version;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        startMenu();

        stage.show();
    }


    public void startMenu() {
        stage.setScene(new Scene(new Menu(this), Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT));
    }

    public void startGame() {
        stage.setScene(new Scene(new Game(this)));
    }



}
