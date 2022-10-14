public class GameObject {

    double x;
    double y;
    double speed = 10;
    double angle = 0;

    public void move() {
        x += Math.cos(angle) * speed * Const.deltaTime;
        y += Math.sin(angle) * speed * Const.deltaTime;
    }

}
