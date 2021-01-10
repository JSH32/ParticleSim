package ParticleSim;

import java.awt.Color;

public abstract class FlammableCell extends Cell {
    private int durability;

    public FlammableCell(int durability, Color ... colors) {
        super(colors);
        this.durability = durability;
    }

    /**
     * @return did the entity break
     */
    public boolean damage() {
        if (random.nextDouble() < 0.1) {
            durability--;
        }
        if (durability <= 0) {
            this.removeCell();
            return true;
        }
        return false;
    }
}
