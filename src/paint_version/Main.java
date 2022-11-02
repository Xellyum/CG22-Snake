package paint_version;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        InputStream stream = getClass().getResourceAsStream("/paint_version/pixelmix.ttf");
        Settings.FONT = Font.loadFont(stream, 24);

        this.stage = stage;

        Scene scene = new Scene(new Pane(), Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        stage.setScene(scene);

        startMenu();

        stage.show();
    }


    public void startMenu() {
        stage.getScene().setRoot(new Menu(this));
    }

    public void startGame() {
        stage.getScene().setRoot(new Game(this));
    }



}
