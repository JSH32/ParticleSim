package ParticleSim.Cells;

import java.awt.Color;

import ParticleSim.Direction;
import ParticleSim.Cell;
import ParticleSim.FlammableCell;
import ParticleSim.Pos;
import ParticleSim.SandLab;

public class Fire extends Cell {
    // Fire despawns when not near a flammable cell for too long
    private int awayFromFlammable = 0;

    // How many ticks away from flammable does fire have
    private static final int lifeTime = 40;

    public Fire() {
        super(new Color(130, 30, 2), new Color(176, 58, 2), new Color(230, 117, 23), new Color(249, 158, 50), new Color(247, 232, 99));
    }

    private void spread(FlammableCell spreadTo) {
        Pos position = spreadTo.randomEmptySide();
        if (random.nextDouble() > 0.5) {
            if (position != null) {
                SandLab.grid[position.y][position.x] = new Fire();
            }
        } else {
            spreadTo.damage();
        }
    }

    public void update() {
        FlammableCell near = getSurroundingCell(FlammableCell.class);
        if (near != null) {
            awayFromFlammable = 0;
            spread(near);
            return;
        }

        int rand = random.nextInt(5);
        switch (rand) {
            case 0:
                this.move(Direction.TOP);
                break;
            case 1:
                this.move(Direction.LEFT);
                break;
            case 2:
                this.move(Direction.RIGHT);
                break;
            case 3:
                this.move(Direction.TOPLEFT);
                break;
            case 4:
                this.move(Direction.TOPRIGHT);
                break;
        }

        if (++awayFromFlammable > lifeTime) {
            this.removeCell();
            return;
        }
    }
}
