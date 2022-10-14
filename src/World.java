public class World extends Thread {

    private GraphicSystem graphicSystem;

    public void initializeWorld() {
        System.out.printf("World Initialized");
    }

    @Override
    public void run() {
        long lastTick = System.currentTimeMillis();

        // Start game loop
        while (true) {
            long currentTick = System.currentTimeMillis();
            Const.deltaTime = currentTick - lastTick;

            if (Const.deltaTime < 1000.0 / Const.FRAME_RATE) {
                try {
                    Thread.sleep((long) (1000.0 / Const.FRAME_RATE - Const.deltaTime));
                } catch (InterruptedException ignored){}
                currentTick = System.currentTimeMillis();
                Const.deltaTime = currentTick - lastTick;
            }

            lastTick = currentTick;


            System.out.println(Const.deltaTime);
            update();
        }
    }

    private void update() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO - process user input

        // TODO - move objects / handle collisions

        // TODO - delete all dead objects

        // TODO - draw all objects

        // (TODO - redraw everything)
    }


    public void setGraphicSystem(GraphicSystem gs) {
        graphicSystem = gs;
    }
}
