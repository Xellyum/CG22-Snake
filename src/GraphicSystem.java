import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GraphicSystem extends Canvas{

    private GraphicsContext graphics;
    int x = 100;

    public GraphicSystem() {
        this.setWidth(Const.SCREEN_WIDTH);
        this.setHeight(Const.SCREEN_HEIGHT);
        graphics = this.getGraphicsContext2D();
    }

    public void clear() {
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
    }

    public void draw() {
        graphics.setFill(Color.DARKRED);
        graphics.fillOval(x, 100, 25, 25);
        x += 1;

    }



}
