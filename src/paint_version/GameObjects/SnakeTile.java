package paint_version.GameObjects;

import com.sun.javafx.geom.Point2D;

public class SnakeTile extends GameObject {

    public SnakeTile parentTile;

    public SnakeTile(int x, int y, int width, int height, Type type, SnakeTile parentTile) {
        super(x, y, width, height, type);
        this.parentTile = parentTile;

    }

    public void move() {
        x = parentTile.x;
        y = parentTile.y;
    }




}
