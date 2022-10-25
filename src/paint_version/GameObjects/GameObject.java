package paint_version.GameObjects;

public abstract class GameObject {

    public int x;
    public int y;
    public int width;
    public int height;

    public Type type;
    public boolean movable;


    public GameObject(int x, int y, int width, int height, Type type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }





    public enum Type {
        ENVIRONMENT,
        SNAKE,
        FOOD,
        TEXT
    }

}
