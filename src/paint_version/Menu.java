package paint_version;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Menu extends VBox {

    Main gameMaster;

    public Menu(Main master) {
        this.gameMaster = master;

        Label title = new Label("V I P E R");
        Button startGame = new Button("START");
        Button exitGame = new Button("EXIT");

        title.setPrefSize(400, 50);
        title.setAlignment(Pos.CENTER);
        startGame.setPrefSize(400, 50);
        exitGame.setPrefSize(400, 50);

        startGame.setOnAction(event -> this.getScene().setRoot(new DifficultyWindow()));
        exitGame.setOnAction(event -> System.exit(0));

        this.getChildren().addAll(title, startGame, exitGame);

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 50, 0, 0));

    }




    private class DifficultyWindow extends HBox {

        public DifficultyWindow() {
            Label difficultyText = new Label("SELECT DIFFICULTY");
            Button easy = new Button("EASY");
            Button normal = new Button("NORMAL");
            Button hard = new Button("HARD");
            Button back = new Button("BACK");


            difficultyText.setPrefSize(300, 50);
            difficultyText.setAlignment(Pos.CENTER);
            easy.setPrefSize(200, 50);
            normal.setPrefSize(200, 50);
            hard.setPrefSize(200, 50);
            back.setPrefSize(300, 50);


            easy.setOnAction(event -> {
                Settings.GAME_DIFFICULTY = Settings.Difficulty.EASY;
                gameMaster.startGame();
            });
            normal.setOnAction(event -> {
                Settings.GAME_DIFFICULTY = Settings.Difficulty.NORMAL;
                gameMaster.startGame();
            });
            hard.setOnAction(event -> {
                Settings.GAME_DIFFICULTY = Settings.Difficulty.HARD;
                gameMaster.startGame();
            });
            back.setOnAction(event -> {
                this.getScene().setRoot(new Menu(gameMaster));
            });


            this.getChildren().addAll(difficultyText, easy, normal, hard, back);

        }



    }


}
