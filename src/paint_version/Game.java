package paint_version;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Pane {

    private Main gameMaster;
    private GraphicsContext graphics;
    private boolean gamePaused;

    private ArrayList<SnakeTile> snake;
    private Circle food;
    private Rectangle wall;
    private String scoreText;

    private Point2D movDirection;
    private int movSpeed = 50;
    private int score;

    private Random random = new Random();


    public Game(Main gameMaster) {
        this.gameMaster = gameMaster;
        Canvas canvas = new Canvas();
        graphics = canvas.getGraphicsContext2D();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gamePaused) return;

                Settings.UPDATE_DELTA_TIME += 16.6;
                if (Settings.UPDATE_DELTA_TIME < Settings.UPDATE_RATE / Settings.GAME_DIFFICULTY.getValue()) return;

                Settings.UPDATE_DELTA_TIME = 0;
                update();
            }
        };
        gameLoop.start();

        canvas.setWidth(Settings.SCREEN_WIDTH);
        canvas.setHeight(Settings.SCREEN_HEIGHT);


        this.getChildren().add(canvas);


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
                pauseGame();
                break;
        }
    }

    private void initializeGame() {
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, Color.color(0, 0, 0), null));
        for (int i = 0; i < Settings.GAME_DIFFICULTY.getValue() * 4; i++) {
            snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, snake.get(i).getColor().brighter(), snake.get(i)));
        }
        //snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, snake.get(0).getColor().brighter(), snake.get(0)));
        //snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, snake.get(1).getColor().brighter(), snake.get(1)));
        //snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50, 50, 50, snake.get(2).getColor().brighter(), snake.get(2)));
        //snake.add(new SnakeTile(100, 100, 50, 50, node_version.GameObject.Type.SNAKE, snake.get(3).color.brighter(), snake.get(3)));

        // add wall
        wall = new Rectangle(50, 50, Settings.SCREEN_WIDTH - 100, Settings.SCREEN_HEIGHT- 100);

        // add food
        food = new Circle(0, 0, 25, Color.GREEN);
        changeFoodPosition();

        // add scoreText
        scoreText = "Score: " + score;


    }

    private void restartGame() {
        gamePaused = false;
        if (this.getChildren().size() > 1) this.getChildren().remove(1);

        // reset movDirection
        movDirection = new Point2D(0, 0);

        // reset score
        score = 0;

        // reset snake
        snake = new ArrayList<>();

        // initialize game
        initializeGame();
    }

    private void update() {
        // should not be in update
        this.getScene().setOnKeyPressed(event -> setInputActions(event));


        moveSnake();
        checkCollisions();
        repaintCanvas();
    }

    private void pauseGame() {
        gamePaused = !gamePaused;
        if (gamePaused) this.getChildren().add(new PauseWindow());
        else if (this.getChildren().size() > 1) this.getChildren().remove(1);
    }


    private void moveSnake() {
        if (movDirection.magnitude() == 0) return;

        for (int i = snake.size() - 1; i >= 0; i--) {
            snake.get(i).move(movDirection.multiply(movSpeed));
        }

    }

    private void checkCollisions() {
        if (movDirection.magnitude() == 0) return;

        SnakeTile snakeHead = snake.get(0);

        // Food check
        if (snakeHead.getX() == food.getCenterX() && snakeHead.getY() == food.getCenterY()) collectFood();

        // Wall check
        if (snakeHead.getX() < 0 + 50
                || snakeHead.getY() < 0 + 50
                || snakeHead.getX() > Settings.SCREEN_WIDTH - 100
                || snakeHead.getY() > Settings.SCREEN_HEIGHT- 100)
            this.getChildren().add(new DefeatWindow());

        // Tail check
        for (int i = 1; i < snake.size(); i++) {
            if (snakeHead.getX() == snake.get(i).getX() && snakeHead.getY() == snake.get(i).getY()) this.getChildren().add(new DefeatWindow());
        }

    }

    private void repaintCanvas() {
        graphics.clearRect(0, 0 , Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

        // draw wall
        graphics.setStroke(Color.BLACK);
        graphics.strokeRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());

        // draw snake
        for (SnakeTile tile : snake) {
            graphics.setFill(tile.getColor());
            graphics.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
        }

        // draw food
        graphics.setFill(food.getFill());
        graphics.fillOval(food.getCenterX(), food.getCenterY(), food.getRadius(), food.getRadius());

        // draw score
        //graphics.setFont(Font.loadFont("Font.ttf", 20));
        graphics.setStroke(Color.BLACK);
        graphics.strokeText(scoreText, 10, 10);

    }


    private void changeMoveDirection(int x, int y) {
        if (movDirection.multiply(-1).getX() == x && movDirection.multiply(-1).getY() == y) return;

        movDirection = new Point2D(x, y);
    }

    private void collectFood() {
        // add new snakeTile
        SnakeTile tile = new SnakeTile((int) snake.get(snake.size() - 1).getX(), (int) snake.get(snake.size() - 1).getY(), 50, 50, snake.get(snake.size() - 1).getColor().brighter(), snake.get(snake.size() - 1));
        snake.add(tile);

        // increase score
        score++;
        scoreText = "Score: " + score;

        // change food position
        changeFoodPosition();

    }

    private void changeFoodPosition() {
        food.setCenterX(random.nextInt((Settings.SCREEN_WIDTH - 50) / 50) * 50 + 50);
        food.setCenterY(random.nextInt((Settings.SCREEN_HEIGHT - 50) / 50) * 50 + 50);
    }




    private class PauseWindow extends VBox {

        public PauseWindow() {
            this.setMinSize(800, 400);

            Button resume = new Button("RESUME");
            Button changeDif = new Button("CHANGE DIFFICULTY");
            Button exit = new Button("EXIT GAME");


            resume.setPrefSize(200, 50);
            changeDif.setPrefSize(200, 50);
            exit.setPrefSize(200, 50);


            resume.setOnAction(event -> pauseGame());
            changeDif.setOnAction(event -> gameMaster.startMenu());
            exit.setOnAction(event -> System.exit(0));


            this.getChildren().addAll(resume, changeDif, exit);

        }

    }

    private class DefeatWindow extends VBox {

        public DefeatWindow() {
            gamePaused = true;
            Label defeat = new Label("YOU DIED!");
            Label score = new Label(scoreText);
            Button restart = new Button("RESTART");
            Button exit = new Button("EXIT GAME");


            restart.setPrefSize(200, 50);
            exit.setPrefSize(200, 50);


            restart.setOnAction(event -> {
                restartGame();
            });
            exit.setOnAction(event -> System.exit(0));


            this.getChildren().addAll(defeat, score, restart, exit);

        }


    }



}
