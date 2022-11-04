package paint_version;

import javafx.scene.text.Font;

public class Settings {

    // 4:3 Aspect Ratio
    public static int SCREEN_WIDTH = 1024;
    public static int SCREEN_HEIGHT = 768;

    public static double UPDATE_DELTA_TIME = 0;
    public static int UPDATE_RATE = 400;

    public static Difficulty GAME_DIFFICULTY;
    public static int HIGHSCORE = 0;


    // Font from: https://www.wfonts.com/font/pixelmix
    public static Font FONT;




    public enum Difficulty {
        EASY(0.5),
        NORMAL(1),
        HARD(2);

        private double value;

        Difficulty(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }


}
