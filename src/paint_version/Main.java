package paint_version;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import paint_version.GameObjects.Food;
import paint_version.GameObjects.GameObject;
import paint_version.GameObjects.SnakeTile;

import java.util.ArrayList;

public class Main extends Application {

    private GraphicsContext graphics;
    private boolean gamePaused = false;

    private ArrayList<GameObject> gameObjects;
    private ArrayList<SnakeTile> snake;
    private Food food;

    private Point2D movDirection;
    private int movSpeed = 50;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas();
        graphics = canvas.getGraphicsContext2D();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gamePaused) return;

                Settings.UPDATE_DELTA_TIME += 16;
                if (Settings.UPDATE_DELTA_TIME < 333) return;

                Settings.UPDATE_DELTA_TIME = 0;
                update();
            }
        };
        gameLoop.start();

        canvas.setWidth(Settings.SCREEN_WIDTH);
        canvas.setHeight(Settings.SCREEN_HEIGHT);

        Pane root = new Pane();
        Scene scene = new Scene(root);

        root.getChildren().add(canvas);

        stage.setScene(scene);
        scene.setOnKeyPressed(event -> setInputActions(event));

        stage.setTitle("V I P E R");
        stage.show();

        restartGame();
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
                gamePaused = !gamePaused;
                break;
        }
    }

    private void initializeGame() {
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, GameObject.Type.SNAKE, Color.color(0, 0, 0), null));
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, GameObject.Type.SNAKE, snake.get(0).color.brighter(), snake.get(0)));
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, GameObject.Type.SNAKE, snake.get(1).color.brighter(), snake.get(1)));
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, GameObject.Type.SNAKE, snake.get(2).color.brighter(), snake.get(2)));
        //snake.add(new SnakeTile(100, 100, 50, 50, node_version.GameObject.Type.SNAKE, snake.get(3).color.brighter(), snake.get(3)));

        // add wall
        gameObjects.add(new GameObject(50, 50, Settings.SCREEN_WIDTH - 100, Settings.SCREEN_HEIGHT- 100, GameObject.Type.ENVIRONMENT, Color.BLACK));

        // add food
        food = new Food(0, 0, 50, 50, GameObject.Type.FOOD, Color.GREEN);
        food.changePositionRandom();
        gameObjects.add(food);

    }

    private void restartGame() {
        // reset movDirection
        movDirection = new Point2D(0, 0);

        // TODO - reset score

        // reset snake
        snake = new ArrayList<>();

        // reset gameObjects
        gameObjects = new ArrayList<>();

        // initialize game
        initializeGame();
    }

    private void update() {
        moveSnake();
        checkCollisions();
        repaintCanvas();
    }


    private void moveSnake() {
        for (SnakeTile snakeTile : snake) {
            if (snakeTile.parentTile == null) {
                snakeTile.previousPos = new Point2D(snakeTile.x, snakeTile.y);
                snakeTile.x += movDirection.getX() * movSpeed;
                snakeTile.y += movDirection.getY() * movSpeed;
            }
            else {
                snakeTile.previousPos = new Point2D(snakeTile.x, snakeTile.y);
                snakeTile.x = (int) snakeTile.parentTile.previousPos.getX();
                snakeTile.y = (int) snakeTile.parentTile.previousPos.getY();
            }
        }
    }

    private void checkCollisions() {
        SnakeTile snakeHead = snake.get(0);

        // Wall check
        if (snakeHead.x < 0 + 50
                || snakeHead.y < 0 + 50
                || snakeHead.x > Settings.SCREEN_WIDTH - 100
                || snakeHead.y > Settings.SCREEN_HEIGHT- 100)
            restartGame();

        // Food check
        if (snakeHead.x == food.x && snakeHead.y == food.y) collectFood();

        // Tail check
        if (snake.size() > 4) {
            for (int i = 1; i < snake.size(); i++) {
                if (snakeHead.x == snake.get(i).x && snakeHead.y == snake.get(i).y) restartGame();
            }
        }

    }

    private void repaintCanvas() {
        graphics.clearRect(0, 0 , Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

        for (GameObject gameObject : gameObjects) {
            graphics.setStroke(gameObject.color);
            graphics.strokeRect(gameObject.x, gameObject.y, gameObject.width, gameObject.height);
        }

        for (GameObject gameObject : snake) {
            graphics.setFill(gameObject.color);
            graphics.fillRect(gameObject.x, gameObject.y, gameObject.width, gameObject.height);
        }

    }


    private void changeMoveDirection(int x, int y) {
        movDirection = new Point2D(x, y);
    }

    private void collectFood() {
        // add new snakeTile
        SnakeTile tile = new SnakeTile(snake.get(snake.size() - 1).x, snake.get(snake.size() - 1).y, 50, 50, GameObject.Type.SNAKE, snake.get(snake.size() - 1).color.brighter(), snake.get(snake.size() - 1));
        snake.add(tile);

        // TODO - increase score


        // change food position
        food.changePositionRandom();

    }



}
