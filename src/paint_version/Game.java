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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Pane {

    private Main gameMaster;
    private GraphicsContext graphics;
    private boolean gamePaused;

    private ArrayList<SnakeTile> snake;
    private Rectangle food;
    private Rectangle wall;

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
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50 - 25, 50, 50, Color.color(0.10, 0.10, 0.10), null));
        for (int i = 0; i < 4; i++) {
            snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 50 - 25, 50, 50, Color.color(snake.get(i).getColor().getRed() + 0.01, snake.get(i).getColor().getGreen() + 0.01, snake.get(i).getColor().getBlue() + 0.01), snake.get(i)));
        }

        // add wall
        wall = new Rectangle((Settings.SCREEN_WIDTH % 50) / 2 + 50 - 5, (Settings.SCREEN_HEIGHT % 50) / 2 + 50 - 5, (int)(Settings.SCREEN_WIDTH / 50) * 50 - 100 + 10, (int)(Settings.SCREEN_HEIGHT / 50) * 50 - 100 + 10);

        // add food
        food = new Rectangle(0, 0, 25, 25);
        changeFoodPosition();

    }

    private void restartGame() {
        gamePaused = false;
        if (this.getChildren().size() > 1) this.getChildren().remove(1);

        // reset movDirection
        movDirection = new Point2D(0, 0);

        // reset score
        if (score > Settings.HIGHSCORE) Settings.HIGHSCORE = score;
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

        if (gamePaused) {
            graphics.setFont(new Font(100));
            graphics.setFill(Color.GRAY);
            graphics.fillText("PAUSED", 500,500);
        }

        //if (gamePaused) this.getChildren().add(new PauseWindow());
        //else if (this.getChildren().size() > 1) this.getChildren().remove(1);
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
        if (snakeHead.getX() == food.getX() && snakeHead.getY() == food.getY()) collectFood();

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
            graphics.fillRoundRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), 25, 25);
        }

        // draw food
        graphics.setFill(Color.GREEN);
        graphics.fillRoundRect(food.getX() + food.getWidth() / 2, food.getY() + food.getHeight() / 2, food.getWidth(), food.getHeight(), food.getWidth() / 3, food.getHeight() / 3);

        // draw score
        graphics.setFont(new Font(25));
        graphics.setFill(Color.GRAY);
        graphics.fillText("SCORE: " + score, wall.getX(), wall.getY() + wall.getHeight() + 25);
        graphics.fillText("HIGHSCORE: " + Settings.HIGHSCORE, wall.getX() + wall.getWidth() - 170, wall.getY() + wall.getHeight() + 25);




    }


    private void changeMoveDirection(int x, int y) {
        if (movDirection.multiply(-1).getX() == x && movDirection.multiply(-1).getY() == y) return;

        movDirection = new Point2D(x, y);
    }

    private void collectFood() {
        // add new snakeTile
        SnakeTile tile = new SnakeTile((int) snake.get(snake.size() - 1).getX(), (int) snake.get(snake.size() - 1).getY(), 50, 50, Color.color(snake.get(snake.size() - 1).getColor().getRed() + 0.01, snake.get(snake.size() - 1).getColor().getGreen() + 0.01, snake.get(snake.size() - 1).getColor().getBlue() + 0.01) , snake.get(snake.size() - 1));
        snake.add(tile);

        // increase score
        score++;

        // change food position
        changeFoodPosition();

    }

    private void changeFoodPosition() {
        food.setX(random.nextInt((Settings.SCREEN_WIDTH - 100) / 50) * 50 + (Settings.SCREEN_WIDTH % 50) / 2 + 50);
        food.setY(random.nextInt((Settings.SCREEN_HEIGHT - 100) / 50) * 50 + (Settings.SCREEN_HEIGHT % 50) / 2 + 50);
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
            Label scoreText = new Label(String.valueOf(score));
            Button restart = new Button("RESTART");
            Button exit = new Button("EXIT GAME");


            restart.setPrefSize(200, 50);
            exit.setPrefSize(200, 50);


            restart.setOnAction(event -> {
                restartGame();
            });
            exit.setOnAction(event -> System.exit(0));


            this.getChildren().addAll(defeat, scoreText, restart, exit);

        }


    }



}
