package paint_version;

public class Settings {

    public static int SCREEN_WIDTH = 1600;
    public static int SCREEN_HEIGHT = 900;

    public static double UPDATE_DELTA_TIME = 0;
    public static int UPDATE_RATE = 400;

    public static Difficulty GAME_DIFFICULTY;




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
