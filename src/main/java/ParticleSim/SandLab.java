package ParticleSim;

import java.awt.*;

import ParticleSim.Cells.*;

public class SandLab {
    public static void main(String[] args) {
        SandLab lab = new SandLab(100, 200);
        lab.run();
    }

    // do not add any more fields
    public static Cell[][] grid;
    public static SandDisplay display;
    private final int numRows, numCols;

    // Used to determine which cells have been updated
    private static boolean flip = false;

    // Color of the sky
    public static Color skyColor = new Color(109, 183, 222);

    public static final int RESET = 0;
    public static final int EMPTY = 1;
    public static final int SAND = 2;
    public static final int METAL = 3;
    public static final int WATER = 4;
    public static final int WOOD = 5;
    public static final int FIRE = 6;

    public SandLab(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        grid = new Cell[this.numRows][this.numCols];

        String[] names;
        names = new String[7];
        names[RESET] = "Reset";
        names[EMPTY] = "Empty";
        names[SAND] = "Sand";
        names[METAL] = "Metal";
        names[WATER] = "Water";
        names[WOOD] = "Wood";
        names[FIRE] = "Fire";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);
    }

    // called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool) {
        switch (tool) {
            case RESET:
                grid = new Cell[this.numRows][this.numCols];
                break;
            case EMPTY:
                this.drawCircle(col, row, display.sizeSliderValue(), null);
                break;
            case SAND:
                this.drawCircle(col, row, display.sizeSliderValue(), Sand.class);
                break;
            case METAL:
                this.drawCircle(col, row, display.sizeSliderValue(), Metal.class);
                break;
            case WATER:
                this.drawCircle(col, row, display.sizeSliderValue(), Water.class);
                break;
            case WOOD:
                this.drawCircle(col, row, display.sizeSliderValue(), Wood.class);
                break;
            case FIRE:
                this.drawCircle(col, row, display.sizeSliderValue(), Fire.class);
                break;
        }
    }

    private void putPixel(int x, int y, Cell cell) {
        if (Cell.canMove(y, x)) {
            grid[y][x] = cell;
        }
    }

    private <T extends Cell> void drawCircle(int centerX, int centerY, int radius, Class<T> cellType) {
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (j * j + i * i < radius * radius) {
                    if (cellType == null && inBounds(centerY + i, centerX + j)) {
                        grid[centerY + i][centerX + j] = null;
                        continue;
                    }
                    try {
                        putPixel(centerX + j, centerY + i, cellType.getConstructor().newInstance());
                    } catch (Exception e) {}
                }
            }
        }
    }

    public static boolean inBounds(int row, int col) {
        if (row >= 0 && row < SandLab.grid.length) {
            if (col >= 0 && col < SandLab.grid[row].length) {
                return true;
            }
        }
        return false;
    }

    public void update() {
        // Update all entities in reverse order
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell cell = grid[i][j];
                if (cell == null) continue;
                if (flip != cell.flip) continue;
                cell.row = i;
                cell.col = j;
                cell.update();
                cell.flip = !cell.flip;
            }
        }
        flip = !flip;
    } 

    public void render() {
        // Call cell renders
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell entity = grid[i][j];
                if (entity == null) {
                    display.setColor(i, j, skyColor);
                    continue;
                }
                entity.render();
            }
        }
    }


    final int TICKS_PER_SECOND = 60;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;
    
    // do not modify
    // JOSH: I modified for steadier FPS and update time
    public void run() {
        double nextTick = System.currentTimeMillis();
        int loops;

        while (true) {
            loops = 0;
            while (System.currentTimeMillis() > nextTick && loops < MAX_FRAMESKIP) {
                update();

                nextTick += SKIP_TICKS;
                loops++;
            }

            render();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}