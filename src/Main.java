import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    int frameCap = 2;
    double timeSinceLastUpdate = 0.0;
    long lastTick = 0;
    boolean gamePaused = false;

    Point2D movementDirection = new Point2D(0, 0);
    //boolean movDirInputUsed = false;
    boolean snakeHasMoved = false;
    int movementSpeed = 50;
    int score = 0;

    Group snake = new Group();
    GameObject head = new GameObject(375, 275, movementSpeed, movementSpeed, GameObject.Type.HEAD, Color.BLACK);
    GameObject food = new GameObject(200, 200, 25, 25, GameObject.Type.FOOD, Color.GREEN);
    TextObject scoreText = new TextObject(50, 50, "Score: " + score);
    TextObject pausedText = new TextObject(400, 300, "PAUSED");
    Random r = new Random();
    Pane root;



    @Override
    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.setTitle("V I P E R");
        stage.show();



        scene.setOnKeyPressed(event -> setKeyActions(event));



        initialize();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentTick = System.currentTimeMillis();
                timeSinceLastUpdate = currentTick - lastTick;

                // System.out.println("Last Update: " + timeSinceLastUpdate);


                // game loop
                if (gamePaused) return;

                changeFoodRotation();

                if (timeSinceLastUpdate + 8 >= 1000.0 / frameCap) {
                    update();
                    lastTick = currentTick;
                }
            }
        };
        timer.start();

    }


    private void initialize() {
        // add walls
        root.getChildren().add(new GameObject(0, 0, 800, 24, GameObject.Type.ENVIRONMENT, Color.GRAY));
        root.getChildren().add(new GameObject(0, 0, 800, 24, GameObject.Type.ENVIRONMENT, Color.GRAY));
        root.getChildren().add(new GameObject(0, 576, 800, 24, GameObject.Type.ENVIRONMENT, Color.GRAY));
        root.getChildren().add(new GameObject(0, 0, 24, 600, GameObject.Type.ENVIRONMENT, Color.GRAY));
        root.getChildren().add(new GameObject(776, 0, 24, 600, GameObject.Type.ENVIRONMENT, Color.GRAY));

        // add snake
        snake.getChildren().add(head);
        root.getChildren().add(snake);

        // add food
        root.getChildren().add(food);

        // add UI
        root.getChildren().add(scoreText);
        root.getChildren().add(pausedText);
        pausedText.setDisable(!gamePaused);


        changeFoodPosition();
    }

    private void update() {
        moveSnake();

        checkCollisions();
    }




    private void checkCollisions() {
        for (GameObject gameObject : convertNodesToGameObjects(root.getChildren())) {
            switch (gameObject.getType()) {
                case ENVIRONMENT:
                    if (gameObject.getBoundsInParent().intersects(head.getBoundsInParent())) {
                        System.out.println("Hit Wall");
                        gameOver();
                    }
                    break;

                case BODY:
                    if (gameObject.getCurrentPos().getX() == head.getCurrentPos().getX() &&
                            gameObject.getCurrentPos().getY() == head.getCurrentPos().getY()) {
                        System.out.println("Hit Body");
                        gameOver();
                    }
                    break;

                case FOOD:
                    if (gameObject.getBoundsInParent().intersects(head.getBoundsInParent())) {
                        addBodyToSnake();
                        addScore();
                        changeFoodPosition();
                    }
                    break;
            }

        }

    }

    private ArrayList<GameObject> convertNodesToGameObjects(List<Node> nodes) {
        ArrayList<GameObject> gameObjects = new ArrayList<>();

        for (Node node : nodes) {
            try {
                gameObjects.add((GameObject) node);
            } catch (Exception ignored) {
                try {
                    Group group = (Group)node;
                    for (Node possibleGroup : group.getChildren()) {
                        gameObjects.add((GameObject) possibleGroup);
                    }
                } catch (Exception ig) {}

            }
        }

        return gameObjects;
    }

    private void setKeyActions(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                changeMoveDirection(new Point2D(0, -1));
                break;
            case A:
                changeMoveDirection(new Point2D(-1, 0));
                break;

            case S:
                changeMoveDirection(new Point2D(0, 1));
                break;

            case D:
                changeMoveDirection(new Point2D(1, -0));
                break;

            case SPACE:
                moveSnake();
                //System.out.println("Movement: " + movementDirection.getX() + "   " + movementDirection.getY());
                break;

            case ESCAPE:
                pauseGame();
                break;
        }
    }

    private void changeMoveDirection(Point2D newMoveDirection) {
        //if (!movDirInputUsed) return;
        if (snake.getChildren().size() != 1) {
            if (movementDirection.multiply(-1).getX() == newMoveDirection.getX() ||
                    movementDirection.multiply(-1).getY() == newMoveDirection.getY()) return;
        }

        movementDirection = newMoveDirection;
        //movDirInputUsed = false;
    }

    private void moveSnake() {
        head.move(new Point2D(
                head.getTranslateX() + movementDirection.getX() * movementSpeed,
                head.getTranslateY() + movementDirection.getY() * movementSpeed));

        // Sets movPos for snakePart to lastPos of previous snakePart
        for (int i = 1; i < snake.getChildren().size(); i++) {
            GameObject previous = (GameObject) snake.getChildren().get(i - 1);
            GameObject body = (GameObject) snake.getChildren().get(i);

            body.move(previous.getLastPos());
        }

        //movDirInputUsed = true;
    }

    private void addBodyToSnake() {
        GameObject lastSnakePart = (GameObject)snake.getChildren().get(snake.getChildren().size() - 1);

        GameObject body = new GameObject((int)lastSnakePart.getLastPos().getX(), (int)lastSnakePart.getLastPos().getY(), 50, 50, GameObject.Type.BODY, Color.GRAY);

        snake.getChildren().add(body);
    }

    private void changeFoodPosition() {
        double xPos = r.nextInt(800 / 50) * 50 + 25 + food.getWidth() / 2;
        double yPos = r.nextInt(600 / 50) * 50 + 25 + food.getWidth() / 2;
        food.setTranslateX(xPos);
        food.setTranslateY(yPos);

        for (GameObject gameObject : convertNodesToGameObjects(root.getChildren())) {
            if (gameObject.getType() == GameObject.Type.FOOD) return;
            if (gameObject.getBoundsInParent().intersects(food.getBoundsInParent())) {
                changeFoodPosition();
            }
        }

        // TODO - prevent food from spawning inside snake body
    }

    private void addScore() {
        score++;
        scoreText.setText("Score: " + score);
        if (score % 5 == 0) {
            frameCap++;
        }
    }

    private void changeFoodRotation() {
        food.setRotate(food.getRotate() + 0.5);
    }

    private void gameOver() {
        System.out.println("Game Over");
        pauseGame();
    }

    private void pauseGame() {
        gamePaused = !gamePaused;
        pausedText.setDisable(!gamePaused);
    }





    public static void main(String[] args) {
        launch(args);
    }

}
