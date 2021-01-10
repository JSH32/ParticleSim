package ParticleSim.Cells;

import java.awt.Color;

import ParticleSim.FlammableCell;

public class Wood extends FlammableCell {
    public Wood() {
        super(5, new Color(85, 51, 17), new Color(102, 68, 51), new Color(51, 34, 17));
    }
}
