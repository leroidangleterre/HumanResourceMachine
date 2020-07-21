package humanresourcemachine;

import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class Ground extends Square {

    public Ground() {
        this(0, 0, 1);
    }

    public Ground(double x, double y, double size) {
        super(x, y, size, new Color(200, 200, 200));
    }

    @Override
    protected boolean isEmpty() {
        return !(this.containsDataCube() || this.containsWorker());
    }
}
