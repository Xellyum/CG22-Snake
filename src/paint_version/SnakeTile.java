package paint_version;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SnakeTile extends Rectangle {

    private SnakeTile parentTile;
    private Color color;


    public SnakeTile(int x, int y, int width, int height, Color color, SnakeTile parentTile) {
        super(x, y, width, height);
        setFill(color);
        this.parentTile = parentTile;
        this.color = color;

    }

    public void move(Point2D movement) {
        if (parentTile == null) {
            setX(getX() + movement.getX());
            setY(getY() + movement.getY());
        }
        else {
            setX(parentTile.getX());
            setY(parentTile.getY());
        }
    }

    public Color getColor() {
        return color;
    }


}
