package ParticleSim;

import java.awt.*;
import java.util.*;

public abstract class Cell {
    protected static Random random = new Random();

    private Color color;
    private Color[] colors;

    // Do not manually control, is used to determine if something has already been updated
    public boolean flip = false;

    public int row, col = 0; // updated in updateDisplay, don't manually control, will be overriden
    public Cell(Color ... colors) {
        this.colors = colors;
        this.color = colors[random.nextInt(colors.length)];
    }
    
    public void update() {};

    public void render() {
        SandLab.display.setColor(row, col, color);
    }

    public void move(int row, int col) {
        if (canMove(row, col)) {
            SandLab.grid[this.row][this.col] = null;
            SandLab.grid[row][col] = this;
        }
    }

    /**
     * Remove the cell from the grid, can still be readded if move is called
     */
    public void removeCell() {
        SandLab.grid[row][col] = null;
    }

    public Pos getPos() {
        return new Pos(row, col);
    }

    protected void changeColor() {
        this.color = colors[random.nextInt(colors.length)];
    }

    public Cell getRelative(Direction direction) {
        switch (direction) {
            case BOTTOM:
                if (!SandLab.inBounds(row+1, col)) return null;
                return SandLab.grid[row+1][col];
            case TOP:
                if (!SandLab.inBounds(row-1, col)) return null;
                return SandLab.grid[row-1][col];
            case LEFT:
                if (!SandLab.inBounds(row, col-1)) return null;
                return SandLab.grid[row][col-1];
            case RIGHT:
                if (!SandLab.inBounds(row, col+1)) return null;
                return SandLab.grid[row][col+1];
            case TOPLEFT:
                if (!SandLab.inBounds(row-1, col-1)) return null;
                return SandLab.grid[row-1][col-1];
            case TOPRIGHT:
                if (!SandLab.inBounds(row-1, col+1)) return null;
                return SandLab.grid[row-1][col+1];
            case BOTTOMLEFT:
                if (!SandLab.inBounds(row+1, col-1)) return null;
                return SandLab.grid[row+1][col-1];
            case BOTTOMRIGHT:
                if (!SandLab.inBounds(row+1, col+1)) return null;
                return SandLab.grid[row+1][col+1];
            default: 
                return null;
        }
    }

    public void move(Direction direction) {
        switch (direction) {
            case TOP:
                if (canMove(row-1, col)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[--row][col] = this;
                }
                break;
            case BOTTOM:
                if (canMove(row+1, col)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[++row][col] = this;
                }
                break;
            case LEFT:
                if (canMove(row, col-1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[row][--col] = this;
                }
                break;
            case RIGHT:
                if (canMove(row, col+1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[row][++col] = this;
                }
                break;
            case BOTTOMLEFT:
                if (canMove(row+1, col-1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[++row][--col] = this;
                }
            case BOTTOMRIGHT:
                if (canMove(row+1, col+1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[++row][++col] = this;
                }
            case TOPLEFT:
                if (canMove(row-1, col-1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[--row][--col] = this;
                }
            case TOPRIGHT:
                if (canMove(row-1, col+1)) {
                    SandLab.grid[row][col] = null;
                    SandLab.grid[--row][++col] = this;
                }
        }
    }

    /**
     * @return random empty side of a cell, null if all sides full
     */
    public Pos randomEmptySide() {
        ArrayList<Pos> freePositions = new ArrayList<Pos>();

        Cell top = this.getRelative(Direction.TOP);
        if (top == null && this.canMove(Direction.TOP)) {
            freePositions.add(new Pos(row-1, col));
        }

        Cell bottom = this.getRelative(Direction.BOTTOM);
        if (bottom == null && this.canMove(Direction.BOTTOM)) {
            freePositions.add(new Pos(row+1, col));
        }

        Cell left = this.getRelative(Direction.LEFT);
        if (left == null && this.canMove(Direction.LEFT)) {
            freePositions.add(new Pos(row, col-1));
        }

        Cell right = this.getRelative(Direction.RIGHT);
        if (right == null && this.canMove(Direction.RIGHT)) {
            freePositions.add(new Pos(row, col+1));
        }

        Cell topleft = this.getRelative(Direction.TOPLEFT);
        if (topleft == null && this.canMove(Direction.TOPLEFT)) {
            freePositions.add(new Pos(row-1, col-1));
        }

        Cell topright = this.getRelative(Direction.TOPRIGHT);
        if (topright == null && this.canMove(Direction.TOPRIGHT)) {
            freePositions.add(new Pos(row-1, col+1));
        }

        Cell bottomleft = this.getRelative(Direction.BOTTOMLEFT);
        if (bottomleft == null && this.canMove(Direction.BOTTOMLEFT)) {
            freePositions.add(new Pos(row+1, col-1));
        }

        Cell bottomright = this.getRelative(Direction.BOTTOMRIGHT);
        if (bottomright == null && this.canMove(Direction.BOTTOMRIGHT)) {
            freePositions.add(new Pos(row+1, col+1));
        }

        if (freePositions.size() != 0) {
            int rand = random.nextInt(freePositions.size());
            return freePositions.get(rand);
        }

        return null;
    }

    public static boolean canMove(int row, int col) {
        if (row >= 0 && row < SandLab.grid.length) {
            if (col >= 0 && col < SandLab.grid[row].length) {
                if (SandLab.grid[row][col] != null) return false;
                return true;
            }
        }
        return false;
    }

    public boolean canMove(Direction direction) {
        switch (direction) {
            case BOTTOM:
                return canMove(row+1, col);
            case TOP:
                return canMove(row-1, col);
            case LEFT:
                return canMove(row, col-1);
            case RIGHT:
                return canMove(row, col+1);
            case TOPLEFT:
                return canMove(row-1, col-1);
            case TOPRIGHT:
                return canMove(row-1, col+1);
            case BOTTOMLEFT:
                return canMove(row+1, col-1);
            case BOTTOMRIGHT:
                return canMove(row+1, col+1);
        }
        return false;
    }

    /**
     * Get a random surrounding cell of a certain type
     * 
     * @param type The class type derived from Entity to look for
     * 
     * @return The entity if it was found
     */
    @SuppressWarnings("unchecked")
    protected <T extends Cell> T getSurroundingCell(Class<T> type) {
        Cell top = this.getRelative(Direction.TOP);
        if (top != null && type.isInstance(top)) return (T)top;

        Cell bottom = this.getRelative(Direction.BOTTOM);
        if (bottom != null && type.isInstance(bottom)) return (T)bottom;

        Cell left = this.getRelative(Direction.LEFT);
        if (left != null && type.isInstance(left)) return (T)left;

        Cell right = this.getRelative(Direction.RIGHT);
        if (right != null && type.isInstance(right)) return (T)right;

        Cell topleft = this.getRelative(Direction.TOPLEFT);
        if (topleft != null && type.isInstance(topleft)) return (T)topleft;

        Cell topRight = this.getRelative(Direction.TOPRIGHT);
        if (topRight != null && type.isInstance(topRight)) return (T)topRight;

        Cell bottomLeft = this.getRelative(Direction.BOTTOMLEFT);
        if (bottomLeft != null && type.isInstance(bottomLeft)) return (T)bottomLeft;

        Cell bottomRight = this.getRelative(Direction.BOTTOMRIGHT);
        if (bottomRight != null && type.isInstance(bottomRight)) return (T)bottomRight;

        return null;
    }
}