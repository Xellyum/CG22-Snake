import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas();
        canvas.setWidth(800);
        canvas.setHeight(600);
        GraphicsContext cg = canvas.getGraphicsContext2D();

        cg.strokeRect(0, 0, 800, 600);
        cg.fillRect(100, 100, 50, 50);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);

        stage.setScene(new Scene(root, 900, 700));
        stage.setTitle("CD2DProject");
        stage.show();
    }
}
