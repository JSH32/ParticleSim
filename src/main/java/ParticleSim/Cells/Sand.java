package ParticleSim.Cells;

import java.awt.Color;

import ParticleSim.Cell;
import ParticleSim.SandLab;
import ParticleSim.Direction;

public class Sand extends Cell {
    public Sand() {
        super(new Color(246, 215, 176), new Color(242, 210, 169), new Color(236, 204, 162), new Color(231, 196, 150), new Color(225, 191, 146));
    }

    public void update() {
        Cell below = this.getRelative(Direction.BOTTOM);
        if (below != null && below instanceof Water) {
            SandLab.grid[++row][col] = this;
            if (below.row != 0) {
                SandLab.grid[--below.row][below.col] = below;
            }
            return;
        }

        if (this.canMove(Direction.BOTTOM)) {
            this.move(Direction.BOTTOM);
            return;
        }

        boolean attemptedLeft = false;
        boolean attemptedRight = false;

        loop: {
            switch (random.nextInt(2)) {
                case 0: {
                    if (this.canMove(Direction.BOTTOMLEFT)) {
                        this.move(Direction.BOTTOMLEFT);
                        return;
                    }
                    attemptedLeft = true;
                    if (attemptedRight) {
                        break;
                    }
                }
                case 1: {
                    if (this.canMove(Direction.BOTTOMRIGHT)) {
                        this.move(Direction.BOTTOMRIGHT);
                        return;
                    }
                    attemptedRight = true;
                    if (attemptedLeft) {
                        break loop;
                    }
                }
            }
        }
    }
}
