/**
 *
 * @author arthu
 */
public abstract class DirectionalInstructionModel extends InstructionModel {

    protected CompassModel currentDirection;

    public DirectionalInstructionModel(CompassModel newCompassModel) {
        super();
        currentDirection = newCompassModel;
    }

    public CardinalPoint getCardinalPoint() {
        if (currentDirection.getCurrentDirections().size() > 0) {
            return currentDirection.getCurrentDirections().get(0);
        } else {
            return null;
        }
    }

    /**
     * Give the compass a single direction.
     *
     * @param newCardPoint
     */
    public void setDirection(CardinalPoint newCardPoint) {
        currentDirection.setValue(newCardPoint);
        Notification n = createNotification();
        notifyObservers(n);
    }

    /**
     * Give the compass a set of one or several directions.
     *
     * @param parameters a string formatted as "SOUTH EAST WEST"
     */
    public void setDirection(String parameters) {
        currentDirection.setValue(parameters);
        Notification n = createNotification();
        notifyObservers(n);
    }

    /**
     * Return the direction specified by the compass.
     * If the compass has multiple selected directions, choose randomly between
     * them.
     *
     * @return the direction selected by the compass
     */
    @Override
    public String getOptions() {

        String option = "";

        CardinalPoint chosenDirection = currentDirection.getValue();

        switch (chosenDirection) {
        case CENTER:
            option = "C";
            break;
        case NORTH:
            option = "N";
            break;
        case SOUTH:
            option = "S";
            break;
        case EAST:
            option = "E";
            break;
        case WEST:
            option = "W";
            break;
        default:
            option = "_";
            break;
        }
        return option;
    }

}
