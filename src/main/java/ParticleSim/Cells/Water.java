package ParticleSim.Cells;

import java.awt.Color;

import ParticleSim.Direction;
import ParticleSim.Cell;

public class Water extends Cell {
    public Water() {
        super(new Color(91, 146, 228), new Color(88, 141, 220), new Color(86, 138, 216), new Color(81, 129, 202), new Color(78, 125, 195));
    }

    public void update() {
        this.changeColor();

        // Put out fire
        Fire nearbyFire = this.getSurroundingCell(Fire.class);
        if (nearbyFire != null) {
            nearbyFire.removeCell();
        }

        // Simulate water flow
        int rand = random.nextInt(2+1);
        switch (rand) {
            case 0:
                this.move(Direction.BOTTOM);
                break;
            case 1:
                this.move(Direction.LEFT);
                break;
            case 2:
                this.move(Direction.RIGHT);
                break;
        }
    }
}