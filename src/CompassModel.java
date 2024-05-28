import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
class CompassModel extends MyDefaultModel {

    private ArrayList<CardinalPoint> activeDirections;

    // A compass may or may not allow multiple directions to be selected at once.
    // By default true.
    private boolean allowMultipleDirections;

    public CompassModel() {
        activeDirections = new ArrayList<>();
        allowMultipleDirections = true;
    }

    public void setValue(CardinalPoint newValue) {
        if (allowMultipleDirections) {
            if (!activeDirections.contains(newValue)) {
                activeDirections.add(newValue);
            }
        } else {
            // Remove everything except for the new value.
            activeDirections.clear();
            activeDirections.add(newValue);
        }
    }

    public void setAllowMultipleDirections(boolean allowMultiple) {
        allowMultipleDirections = allowMultiple;
    }

//    public void setValue(String newValue) {
//    }
    public CardinalPoint getValue() {
        if (activeDirections.size() == 1) {
            return activeDirections.get(0);
        } else {
            return null;
        }
    }

    /**
     * Return the list of all active directions.
     *
     * @return
     */
    public ArrayList<CardinalPoint> getCurrentDirections() {
//        System.out.println("Compass " + serial + " getCurrentDirection is " + currentDirection);
        ArrayList<CardinalPoint> list = new ArrayList<>();
        for (CardinalPoint cp : activeDirections) {
            list.add(cp);
        }
        return list;
    }

    public void toggle(CardinalPoint newDirection) {
        if (activeDirections.contains(newDirection)) {
            activeDirections.remove(newDirection);
            System.out.println("CompassModel: removing " + newDirection);
        } else {
            activeDirections.add(newDirection);
            System.out.println("CompassModel: adding " + newDirection);
        }
    }

    @Override
    public void selectContent() {
    }

    /**
     * Check if the compass has exactly one direction active.
     *
     *
     * @return
     */
    public boolean hasOnlyOneDirection() {
        return activeDirections.size() == 1;
    }
}
