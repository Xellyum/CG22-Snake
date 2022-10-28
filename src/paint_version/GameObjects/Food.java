package paint_version.GameObjects;

import javafx.scene.paint.Color;
import paint_version.Settings;

import java.util.Random;

public class Food extends GameObject {

    Random random = new Random();

    public Food(int x, int y, int width, int height, Type type, Color color) {
        super(x, y, width, height, type, color);

    }

    public void changePositionRandom() {
        x = random.nextInt((Settings.SCREEN_WIDTH - 50) / 50) * 50 + 50;
        y = random.nextInt((Settings.SCREEN_HEIGHT - 50) / 50) * 50 + 50;
    }



}
