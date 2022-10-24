package paint_version;

public abstract class GameObject {

    int x;
    int y;
    int width;
    int height;

    Type type;
    boolean movable;


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
        FOOD
    }

}
