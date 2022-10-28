package paint_version.GameObjects;

import javafx.scene.paint.Color;

public class GameObject {

    public int x;
    public int y;
    public int width;
    public int height;

    public Type type;
    public boolean movable;
    public Color color;


    public GameObject(int x, int y, int width, int height, Type type, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.color = color;
    }





    public enum Type {
        ENVIRONMENT,
        SNAKE,
        FOOD,
        TEXT
    }

}
