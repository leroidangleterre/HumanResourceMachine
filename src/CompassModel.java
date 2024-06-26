import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author arthurmanoha
 */
public class CompassModel extends MyDefaultModel implements Observable {

    private ArrayList<CardinalPoint> activeDirections;

    // A compass may or may not allow multiple directions to be selected at once.
    // By default true.
    private boolean allowMultipleDirections;

    private ArrayList<Observer> observersList;

    public CompassModel() {
        activeDirections = new ArrayList<>();
        allowMultipleDirections = true;
        observersList = new ArrayList<>();
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
        Notification notif = new Notification("CompassChanged", null);
        notifyObservers(notif);
    }

    /**
     * Give the compass a set of one or several directions.
     *
     * @param parameters a string formatted as "SOUTH EAST WEST"
     */
    public void setValue(String parameters) {
        for (String s : parameters.split(" ")) {
            switch (s) {
            case "NORTH":
                activeDirections.add(CardinalPoint.NORTH);
                break;
            case "SOUTH":
                activeDirections.add(CardinalPoint.SOUTH);
                break;
            case "EAST":
                activeDirections.add(CardinalPoint.EAST);
                break;
            case "WEST":
                activeDirections.add(CardinalPoint.WEST);
                break;
            case "CENTER":
                activeDirections.add(CardinalPoint.CENTER);
                break;
            case "NORTHEAST":
                activeDirections.add(CardinalPoint.NORTHEAST);
                break;
            case "SOUTHEAST":
                activeDirections.add(CardinalPoint.SOUTHEAST);
                break;
            case "SOUTHWEST":
                activeDirections.add(CardinalPoint.SOUTHWEST);
                break;
            case "NORTHWEST":
                activeDirections.add(CardinalPoint.NORTHWEST);
                break;
            }
        }
    }

    public void setAllowMultipleDirections(boolean allowMultiple) {
        allowMultipleDirections = allowMultiple;
    }

    /**
     * Retrieve the direction pointed to by the compass.
     * If several directions are active, choose randomly one of them.
     *
     * @return
     */
    public CardinalPoint getValue() {
        CardinalPoint result;
        if (activeDirections.size() == 1) {
            result = activeDirections.get(0);
        } else if (activeDirections.size() > 1) {
            // Choose a value at random among those currently active.
            int nbPossibleDirections = activeDirections.size();
            int chosenRank = new Random().nextInt(nbPossibleDirections);
            result = activeDirections.get(chosenRank);
        } else {
            result = CardinalPoint.CENTER;
        }
        return result;
    }

    /**
     * Return the list of all active directions.
     *
     * @return
     */
    public ArrayList<CardinalPoint> getCurrentDirections() {
        ArrayList<CardinalPoint> list = new ArrayList<>();
        for (CardinalPoint cp : activeDirections) {
            list.add(cp);
        }
        return list;
    }

    public void toggle(CardinalPoint newDirection) {
        if (allowMultipleDirections) {
            if (activeDirections.contains(newDirection)) {
                activeDirections.remove(newDirection);
            } else {
                activeDirections.add(newDirection);
            }
        } else {
            // Only one direction active, default CENTER
            activeDirections.clear();
            if (activeDirections.contains(newDirection)) {
                // Go back to center
                activeDirections.add(CardinalPoint.CENTER);
            } else {
                activeDirections.add(newDirection);
            }
        }
        Notification notif = new Notification("CompassChanged", null);
        notifyObservers(notif);
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

    private void printAllDirections() {
        if (activeDirections != null) {
            System.out.println("All directions:");
            for (CardinalPoint cp : activeDirections) {
                if (cp != null) {
                    System.out.println("    " + cp.name());
                }
            }
            System.out.println("All directions end");
        }
    }

    /* As an observable object, we notify the listeners when the CompassModel changes. */
    @Override
    public void addObserver(Observer newObserver) {
        this.observersList.add(newObserver);
    }

    @Override
    public void removeObserver(Observer obs) {
        this.observersList.remove(obs);
    }

    @Override
    public void notifyObservers(Notification notif) {
        for (Observer obs : observersList) {
            obs.update(notif);
        }
    }

}
