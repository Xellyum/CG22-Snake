package node_version;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject extends Rectangle {

    private final Type type;
    private boolean moveable;
    private Point2D currentPos;
    private Point2D lastPos;

    public GameObject(int x, int y, int width, int height, Type type, Color color) {
        this.type = type;

        currentPos = new Point2D(x, y);
        setTranslateX(x);
        setTranslateY(y);
        setWidth(width);
        setHeight(height);
        setFill(color);

    }

    public void move(Point2D movePosition) {
        setTranslateX(movePosition.getX());
        setTranslateY(movePosition.getY());

        lastPos = currentPos;
        currentPos = movePosition;
    }

    public Type getType() {
        return type;
    }

    public Point2D getCurrentPos() {
        return currentPos;
    }

    public Point2D getLastPos() {
        return lastPos;
    }





    public enum Type {
        ENVIRONMENT,
        HEAD,
        BODY,
        FOOD
    }

}
