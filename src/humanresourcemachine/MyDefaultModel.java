package humanresourcemachine;

import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public abstract class MyDefaultModel {

    protected double xMin, xMax, yMin, yMax;

    public double getXMin() {
        return xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMax() {
        return yMax;
    }

    public void unselectEverything() {
    }

    public abstract void selectContent();

//    /**
//     * Receive a command so that the model changes itself.
//     *
//     * @param text the text that represents the command
//     */
//    public abstract void receiveCommand(String text);
}
