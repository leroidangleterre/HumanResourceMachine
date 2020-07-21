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
public class Input extends Square {

    public Input() {
        this(0, 0, 1);
    }

    public Input(double x, double y, double size) {
        super(x, y, size, new Color(255, 120, 0));
    }

    @Override
    protected boolean isEmpty() {
        // An input is never empty.
        return false;
    }

}
