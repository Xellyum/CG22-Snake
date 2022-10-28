package paint_version.GameObjects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class SnakeTile extends GameObject {

    public SnakeTile parentTile;
    public Point2D previousPos;

    public SnakeTile(int x, int y, int width, int height, Type type, Color color, SnakeTile parentTile) {
        super(x, y, width, height, type, color);
        this.parentTile = parentTile;

    }

    public void move() {
        x = parentTile.x;
        y = parentTile.y;
    }




}
