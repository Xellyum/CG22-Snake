package paint_version;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Random;

public class Game extends StackPane {

    private final Main gameMaster;
    private final GraphicsContext graphics;
    private boolean gamePaused;
    private boolean gameOver;

    private ArrayList<SnakeTile> snake;
    private Rectangle food;
    private Rectangle wall;

    private Point2D movDirection;
    private ArrayList<Point2D> movInputQueue;
    private int snakeMoves;

    private int score;
    private final Random random = new Random();


    public Game(Main gameMaster) {
        this.gameMaster = gameMaster;
        gameMaster.getScene().setOnKeyPressed(this::setInputActions);

        Canvas canvas = new Canvas();
        graphics = canvas.getGraphicsContext2D();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) return;
                if (gamePaused) return;

                Settings.UPDATE_DELTA_TIME += 16.6;
                if (Settings.UPDATE_DELTA_TIME < Settings.UPDATE_RATE / Settings.GAME_DIFFICULTY.getValue()) return;

                update();

                Settings.UPDATE_DELTA_TIME = 0;
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
                addMovementInput(0, -1);
                break;
            case A:
                addMovementInput(-1, 0);
                break;

            case S:
                addMovementInput(0, 1);
                break;

            case D:
                addMovementInput(1, 0);
                break;

            case ESCAPE:
                pauseGame();
                break;
        }
    }

    private void initializeGame() {
        // add snake
        snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 25, 50, 50, Color.color(0.10, 0.10, 0.10), null));
        for (int i = 0; i < 4; i++) {
            snake.add(new SnakeTile(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2 - 25, 50, 50,
                    Color.color(snake.get(i).getColor().getRed() + 0.01, snake.get(i).getColor().getGreen() + 0.01, snake.get(i).getColor().getBlue() + 0.01), snake.get(i)));
        }

        // add wall
        wall = new Rectangle((Settings.SCREEN_WIDTH % 50) / 2.0 + 50 - 5, (Settings.SCREEN_HEIGHT % 50) / 2.0 + 50 - 5,
                (Settings.SCREEN_WIDTH / 50.0) * 50 - 120 + 6, (Settings.SCREEN_HEIGHT / 50.0) * 50 - 120 + 12);

        // add food
        food = new Rectangle(0, 0, 25, 25);
        changeFoodPosition();

    }

    private void restartGame() {
        gameOver = false;
        gamePaused = false;

        // reset movDirection
        movDirection = new Point2D(0, 0);
        movInputQueue = new ArrayList<>();

        // reset score
        score = 0;
        snakeMoves = 0;

        // reset snake
        snake = new ArrayList<>();

        // initialize game
        initializeGame();
    }

    private void update() {
        moveSnake();
        checkCollisions();
        repaintCanvas();
    }

    private void pauseGame() {
        if (gameOver) return;

        gamePaused = !gamePaused;

        if (gamePaused) {
            graphics.setTextAlign(TextAlignment.CENTER);
            graphics.setFont(new Font(Settings.FONT.getName(), 48));
            graphics.setFill(Color.GRAY);
            graphics.fillText("PAUSED", Settings.SCREEN_WIDTH / 2.0,Settings.SCREEN_HEIGHT / 2.0);
        }

    }

    private void gameOver() {
        gameOver = true;
        if (score > Settings.HIGHSCORE) Settings.HIGHSCORE = score;

        this.getChildren().add(new DefeatWindow());

    }


    private void moveSnake() {
        //System.out.println(movInputQueue);

        if (movInputQueue.size() > 0) {
            movDirection = movInputQueue.get(0);
            movInputQueue.remove(0);
        }

        if (movDirection.magnitude() == 0) return;


        for (int i = snake.size() - 1; i >= 0; i--) {
            snake.get(i).move(movDirection.multiply(50));
        }

        snakeMoves++;
    }

    private void checkCollisions() {
        if (movDirection.magnitude() == 0) return;

        SnakeTile snakeHead = snake.get(0);

        // Food check
        if (snakeHead.getX() == food.getX() && snakeHead.getY() == food.getY()) collectFood();

        // Wall check
        if (snakeHead.getX() < 50
                || snakeHead.getY() < 50
                || snakeHead.getX() > Settings.SCREEN_WIDTH - 100
                || snakeHead.getY() > Settings.SCREEN_HEIGHT- 100)
            gameOver();

        // Tail check
        for (int i = 1; i < snake.size(); i++) {
            if (snakeHead.getX() == snake.get(i).getX() && snakeHead.getY() == snake.get(i).getY()) gameOver();
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
        graphics.fillRoundRect(food.getX() + food.getWidth() / 2, food.getY() + food.getHeight() / 2, food.getWidth(), food.getHeight(),
                food.getWidth(), food.getHeight());

        // draw score
        graphics.setTextAlign(TextAlignment.LEFT);
        graphics.setFont(Settings.FONT);
        graphics.setFill(Color.DIMGRAY);
        graphics.fillText("SCORE: " + score, wall.getX(), wall.getY() + wall.getHeight() + 25);
        graphics.fillText("HIGHSCORE: " + Settings.HIGHSCORE, wall.getX() + wall.getWidth() - 240, wall.getY() + wall.getHeight() + 25);

        // draw hint
        if (movDirection.magnitude() != 0) return;
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setFont(new Font(Settings.FONT.getName(), 24));
        graphics.setFill(Color.GRAY);
        graphics.fillText("USE W / A / S / D TO MOVE", Settings.SCREEN_WIDTH / 2.0, Settings.SCREEN_HEIGHT / 1.5);

    }


    private void addMovementInput(int x, int y) {
        // don't add input to queue if: queue size = 3, new movement input == last input in queue, new movement input == opposite current movement Input
        if (movInputQueue.size() >= 3) return;

        if (movInputQueue.size() == 0) {
            if (movDirection.multiply(-1).getX() == x && movDirection.multiply(-1).getY() == y) return;
            if (movDirection.getX() == x && movDirection.getY() == y) return;
        }
        else {
            if (movInputQueue.get(movInputQueue.size() - 1).multiply(-1).getX() == x && movInputQueue.get(movInputQueue.size() - 1).multiply(-1).getY() == y) return;
            if (movInputQueue.get(movInputQueue.size() - 1).getX() == x && movInputQueue.get(movInputQueue.size() - 1).getY() == y) return;
        }


        movInputQueue.add(new Point2D(x, y));

    }

    private void collectFood() {
        // add new snakeTile
        SnakeTile tile = new SnakeTile(
                (int) snake.get(snake.size() - 1).getX(),
                (int) snake.get(snake.size() - 1).getY(),
                50, 50,
                Color.color(
                        snake.get(snake.size() - 1).getColor().getRed() + 0.01,
                        snake.get(snake.size() - 1).getColor().getGreen() + 0.01,
                        snake.get(snake.size() - 1).getColor().getBlue() + 0.01) ,
                snake.get(snake.size() - 1));
        snake.add(tile);

        // increase score
        if (snakeMoves <= 15) score += 50;
        else score += 50 - (5 * (snakeMoves - 15));
        snakeMoves = 0;

        changeFoodPosition();

    }

    private void changeFoodPosition() {
        food.setX(random.nextInt((Settings.SCREEN_WIDTH - 100) / 50) * 50 + (Settings.SCREEN_WIDTH % 50) / 2.0 + 50);
        food.setY(random.nextInt((Settings.SCREEN_HEIGHT - 100) / 50) * 50 + (Settings.SCREEN_HEIGHT % 50) / 2.0 + 50);

        for (SnakeTile tile : snake) {
            if (tile.getX() == food.getX() && tile.getY() == food.getY()) {
                changeFoodPosition();
                return;
            }
        }

    }



    private class DefeatWindow extends VBox {

        public DefeatWindow() {
            gamePaused = true;
            Label defeat = new Label("GAME OVER");
            Label scoreText = new Label("SCORE: " + score);
            Button restart = new Button("RESTART");
            Button exit = new Button("CHANGE DIFFICULTY");

            defeat.setTextFill(Color.DIMGRAY);
            scoreText.setTextFill(Color.DIMGRAY);
            restart.setTextFill(Color.DIMGRAY);
            exit.setTextFill(Color.DIMGRAY);

            defeat.setFont(new Font(Settings.FONT.getName(), 42));
            scoreText.setFont(new Font(Settings.FONT.getName(), 20));
            restart.setFont(new Font(Settings.FONT.getName(), 20));
            exit.setFont(new Font(Settings.FONT.getName(), 20));

            restart.setPrefSize(280,  40);
            exit.setPrefSize(280, 40);


            restart.setOnAction(event -> {
                Game.this.getChildren().remove(1);
                restartGame();
            });
            exit.setOnAction(event -> gameMaster.startMenu());


            this.setAlignment(Pos.CENTER);
            this.setSpacing(10);
            this.getChildren().addAll(defeat, scoreText, restart, exit);

        }


    }


    private static class SnakeTile extends Rectangle {

        private final SnakeTile parentTile;


        public SnakeTile(int x, int y, int width, int height, Color color, SnakeTile parentTile) {
            super(x, y, width, height);
            setFill(color);
            this.parentTile = parentTile;

        }

        public void move(Point2D movement) {
            if (parentTile == null) {
                setX(getX() + movement.getX());
                setY(getY() + movement.getY());
            }
            else {
                setX(parentTile.getX());
                setY(parentTile.getY());
            }
        }

        public Color getColor() {
            return (Color)getFill();
        }


    }


}
