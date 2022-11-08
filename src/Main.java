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
        // load font
        InputStream stream = getClass().getResourceAsStream("/pixelmix.ttf");
        Settings.FONT = Font.loadFont(stream, 24);

        this.stage = stage;
        stage.setScene(new Scene(new Pane(), Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT));

        startMenu();

        stage.show();
    }


    public Scene getScene() {
        return stage.getScene();
    }

    public void startMenu() {
        stage.getScene().setRoot(new Menu(this));
    }

    public void startGame() {
        stage.getScene().setRoot(new Game(this));
    }


}
