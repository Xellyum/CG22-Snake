package paint_version;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import paint_version.GameObjects.GameObject;

import java.util.ArrayList;

public class Main extends Application {

    Pane root;
    GraphicsContext graphics;
    AnimationTimer gameLoop;
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    ArrayList<GameObject> snake = new ArrayList<>();

    Point2D movDirection;

    int movSpeed = 50;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas();
        graphics = canvas.getGraphicsContext2D();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        canvas.setWidth(Settings.SCREEN_WIDTH);
        canvas.setHeight(Settings.SCREEN_HEIGHT);

        root = new Pane();
        Scene scene = new Scene(root);

        root.getChildren().add(canvas);


        stage.setScene(scene);
        scene.setOnKeyPressed(event -> setInputActions(event));

        stage.setTitle("V I P E R");
        stage.show();

        initializeGameObjects();
    }

    private void setInputActions(KeyEvent key) {
        switch (key.getCode()) {
            case W:
                changeMoveDirection(0, -1);
                break;
            case A:
                changeMoveDirection(-1, 0);
                break;

            case S:
                changeMoveDirection(0, 1);
                break;

            case D:
                changeMoveDirection(1, 0);
                break;

            case ESCAPE:
                pauseGame();
                break;
        }
    }

    private void changeMoveDirection(int x, int y) {
        movDirection = new Point2D(x, y);
    }

    private void pauseGame() {

    }


    private void update() {
        moveSnake();
        checkCollisions();
        repaintCanvas();
    }

    private void checkCollisions() {
        for (GameObject gameObject : snake) {
            // TODO - check each snake piece for collision with: wall / food / other snake piece
        }
    }

    private void moveSnake() {
        for (GameObject gameObject : snake) {
            // TODO - move each snake piece
        }
    }

    private void repaintCanvas() {
        for (GameObject gameObject : gameObjects) {
            // TODO - draw on canvas
            graphics.strokeRect(gameObject.x, gameObject.y, gameObject.width, gameObject.height);
            System.out.println(gameObject.x);
        }

    }

    private void initializeGameObjects() {
        gameLoop.start();
        gameObjects.add(new GameObject(100, 100, 50, 50, GameObject.Type.ENVIRONMENT) {
        });


    }


    private void startGame() {
        initializeGameObjects();
        gameLoop.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
