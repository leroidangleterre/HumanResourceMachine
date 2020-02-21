import java.util.ArrayList;

/**
 *
 * @author arthurmanoha
 */
public class IfInstructionModel extends InstructionModel implements Observable {

    // The direction in which we look for the object we want to test
    private CardinalPoint currentDirection;
    private BooleanConstant currentBoolean;
    private String textValue;
    private boolean isNumber;
    private int intValue;

    private int elseAddress;
    private int endIfAddress;
    private InstructionModel elseTarget;
    private InstructionModel endIfTarget;

    private ArrayList<Observer> observersList;

    public IfInstructionModel() {
        super();
        elseAddress = 0;
        elseTarget = null;
        endIfAddress = 0;
        endIfTarget = null;
        observersList = new ArrayList<>();
        currentBoolean = BooleanConstant.NOT_EQUAL;
        currentDirection = CardinalPoint.WEST;
    }

    public CardinalPoint getCardinalPoint() {
        return this.currentDirection;
    }

    public void toggleDirection() {

        switch (currentDirection) {
            case NORTH:
                currentDirection = CardinalPoint.EAST;
                break;
            case EAST:
                currentDirection = CardinalPoint.SOUTH;
                break;
            case SOUTH:
                currentDirection = CardinalPoint.WEST;
                break;
            case WEST:
                currentDirection = CardinalPoint.CENTER;
                break;
            case CENTER:
                currentDirection = CardinalPoint.NORTH;
                break;
            default:
                break;
        }
    }

    public void setDirection(CardinalPoint newCardPoint) {
        this.currentDirection = newCardPoint;
    }

    public BooleanConstant getCurrentBoolean() {
        return this.currentBoolean;
    }

    public void setCurrentBoolean(BooleanConstant newBool) {
        this.currentBoolean = newBool;
    }

    /**
     * Execute the IF: evaluate the condition, then set the worker's next
     * address.
     *
     * @param date
     * @param w
     */
    @Override
    public void execute(int date, Worker w) {
        super.execute(date, w);
        // Analyse the three sub-components: the direction, the operator, and the choice box,
        // and choose between going to the "then" or to the "else" block.

        String notifText = "IfEvaluation";
        String optionalText = getOptions();//direction + " " + currentBoolean;

        Notification n = new Notification(notifText, w, optionalText);

        notifyObservers(n);
    }

    public void setElseInstruction(InstructionModel newTarget) {
        elseTarget = newTarget;
    }

    public void setElseAddress(int newAddress) {
        System.out.println("IfInstructionModel: setting 'else' target from " + elseAddress + " to " + newAddress);
        elseAddress = newAddress;
    }

    public int getElseAddress() {
        return elseAddress;
    }

    public void setEndInstruction(InstructionModel endInstruction) {
        endIfTarget = endInstruction;
    }

    public void setEndAddress(int newAddress) {
        endIfAddress = newAddress;
    }

    public int getEndAddress() {
        return endIfAddress;
    }

    // As an Observable, we notify Observers (the Terrain) when this Script changes.
    @Override
    public void addObserver(Observer obs) {
        if (!observersList.contains(obs)) {
            observersList.add(obs);
        }
    }

    @Override
    public void removeObserver(Observer obs) {
        observersList.remove(obs);
    }

    @Override
    public void notifyObservers(Notification n) {
        for (Observer observer : observersList) {
            observer.update(n);
        }
    }

    @Override
    public String getName() {
        return "InstructionIf";
    }

    /**
     * Options for an If instruction are: The direction we test for; the
     * operator; and the type of data.
     */
    @Override
    public String getOptions() {
        String res = "_";

        // Example of instruction: if Square north is holds a datacube, then goto 5, else goto 8.
        // res = "_N_==_DataCube_5_8";
        switch (this.currentDirection) {
            case CENTER:
                res += "C_";
                break;
            case NORTH:
                res += "N_";
                break;
            case SOUTH:
                res += "S_";
                break;
            case EAST:
                res += "E_";
                break;
            case WEST:
                res += "W_";
                break;
            default:
                break;
        }

        switch (this.currentBoolean) {
            case EQUAL:
                res += "==";
                break;
            case NOT_EQUAL:
                res += "!=";
                break;
            case GREATER_THAN:
                res += ">=";
                break;
            case LOWER_THAN:
                res += "<=";
                break;
            case STRICTLY_GREATER_THAN:
                res += ">";
                break;
            case STRICTLY_LOWER_THAN:
                res += "<";
                break;
        }

        // add the expected type or value
        // Add the "then" address
        // add the "else" address
        res += "_" + textValue + "_" + this.elseAddress + "_" + this.endIfAddress;

        return res;
    }
}
