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
public class Wall extends Square {

    public Wall() {
        this(0, 0, 1);
    }

    public Wall(double x, double y, double size) {
        super(x, y, size, new Color(0, 0, 200));
    }

    @Override
    protected boolean isEmpty() {
        // A wall is never empty.
        return false;
    }
}
