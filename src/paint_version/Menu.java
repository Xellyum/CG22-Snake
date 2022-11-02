package paint_version;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Menu extends VBox {

    private Main gameMaster;


    public Menu(Main gameMaster) {
        this.gameMaster = gameMaster;

        Label title = new Label("W O R M");
        Label difficulty = new Label("CHOOSE DIFFICULTY:");

        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(0, 0, 50, 0));
        difficulty.setAlignment(Pos.CENTER);

        title.setFont(new Font(Settings.FONT.getName(), 100));
        difficulty.setFont(new Font(Settings.FONT.getName(), 20));


        HBox difBox = new HBox();

        Button easy = new Button("EASY");
        Button normal = new Button("NORMAL");
        Button hard = new Button("HARD");

        easy.setPrefSize(200, 30);
        normal.setPrefSize(200, 30);
        hard.setPrefSize(200, 30);

        easy.setFont(new Font(Settings.FONT.getName(), 18));
        normal.setFont(new Font(Settings.FONT.getName(), 18));
        hard.setFont(new Font(Settings.FONT.getName(), 18));

        easy.setOnAction(event -> {
            Settings.GAME_DIFFICULTY = Settings.Difficulty.EASY;
            this.gameMaster.startGame();
        });
        normal.setOnAction(event -> {
            Settings.GAME_DIFFICULTY = Settings.Difficulty.NORMAL;
            this.gameMaster.startGame();
        });
        hard.setOnAction(event -> {
            Settings.GAME_DIFFICULTY = Settings.Difficulty.HARD;
            this.gameMaster.startGame();
        });

        difBox.setAlignment(Pos.CENTER);
        difBox.setSpacing(10);
        difBox.getChildren().addAll(easy, normal, hard);


        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.getChildren().addAll(title, difficulty, difBox);

    }

}
