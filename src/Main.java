import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;

public class Main extends Application {

    Point2D movementDirection = new Point2D(0, 0);
    double movementSpeed = 10.0;
    Rectangle player;


    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas();
        canvas.setWidth(800);
        canvas.setHeight(600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.setTitle("CD2DProject");
        stage.show();

        player = new Rectangle(10, 10);

        root.getChildren().add(player);

        player.setTranslateX(100);
        player.setTranslateY(100);


        Point2D point = new Point2D(1, 2);
        scene.setOnKeyPressed(event -> setKeyActions(event));

    }


    private void setKeyActions(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                movementDirection = new Point2D(0, -1);
                break;
            case A:
                movementDirection = new Point2D(-1, 0);
                break;

            case S:
                movementDirection = new Point2D(0, 1);
                break;

            case D:
                movementDirection = new Point2D(1, 0);
                break;

            case SPACE:
                movePlayer();
                System.out.println("Movement: " + movementDirection.getX() + "   " + movementDirection.getY());
                break;
        }
    }


    private void movePlayer() {
        System.out.println(player.getX());

        player.setTranslateX(player.getTranslateX() + (movementDirection.getX() * movementSpeed));
        player.setTranslateY(player.getTranslateY() + (movementDirection.getY() * movementSpeed));
    }


    private void gameOver() {
        System.out.println("Game Over");
        while (true);
    }







    public static void main(String[] args) {
        launch(args);
    }

}
