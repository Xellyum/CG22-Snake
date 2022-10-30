package paint_version;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Menu extends VBox {

    private Main gameMaster;


    public Menu(Main master) {
        this.gameMaster = master;

        Label title = new Label("worm");
        Label difficulty = new Label("CHOOSE DIFFICULTY:");


        title.setPrefSize(400, 50);
        title.setAlignment(Pos.CENTER);
        difficulty.setPrefSize(300, 50);
        difficulty.setAlignment(Pos.CENTER);


        HBox difBox = new HBox();

        Button easy = new Button("EASY");
        Button normal = new Button("NORMAL");
        Button hard = new Button("HARD");

        easy.setPrefSize(200, 50);
        normal.setPrefSize(200, 50);
        hard.setPrefSize(200, 50);

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

        difBox.getChildren().addAll(easy, normal, hard);
        difBox.setAlignment(Pos.CENTER);



        this.getChildren().addAll(title, difficulty, difBox);

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 50, 0, 0));



    }

}
