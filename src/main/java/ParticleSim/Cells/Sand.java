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
            SandLab.grid[--below.row][col] = below;
            return;
        }

        if (this.canMove(Direction.BOTTOM)) {
            this.move(Direction.BOTTOM);
        } else if (this.canMove(Direction.BOTTOMLEFT)) {
            this.move(Direction.BOTTOMLEFT);
        } else if (this.canMove(Direction.BOTTOMRIGHT)) {
            this.move(Direction.BOTTOMRIGHT);
        }
    }
}
