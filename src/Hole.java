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
public class Hole extends Square {

    public Hole() {
        this(0, 0, 1);
    }

    public Hole(double x, double y, double size) {
        super(x, y, size, new Color(0, 0, 0));
    }
}
