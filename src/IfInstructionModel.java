import java.util.ArrayList;

/**
 *
 * @author arthurmanoha
 */
public class IfInstructionModel extends InstructionModel implements Observable, Observer {

    // The direction in which we look for the object we want to test
    private CardinalPoint currentDirection;
    private BooleanConstant currentBoolean;
    private String textValue;
    private boolean isNumber;
    private int intValue;

    // What we expect the tested element to be (or not to be, depending on the equality sign).
    private String choiceValue;

    // We send a notification to the terrain, which replies via another notification;
    // we then evaluate the condition.
    private boolean isEvaluated;
    private boolean logicValue;

    private int elseAddress;
    private int endAddress;
    private InstructionModel elseTarget;
    private InstructionModel endTarget;

    private ArrayList<Observer> observersList;

    public IfInstructionModel() {
        super();
        elseAddress = 0;
        elseTarget = null;
        endAddress = 0;
        endTarget = null;
        observersList = new ArrayList<>();
        currentDirection = CardinalPoint.WEST;
        currentBoolean = BooleanConstant.LOWER_THAN;
        choiceValue = "3";
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

    public void setChoiceValue(String newChoiceVal) {
        this.choiceValue = newChoiceVal;
    }

    public String getChoiceValue() {
//        System.out.println("IfInstructionModel.getChoiceValue(): result is "
//                + this.choiceValue);
        return this.choiceValue;
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

        System.out.println("IfInstructionModel.execute(): 42");
        String optionalText = "42" + w.getSerial() + "-" + w.getDirection();

        Notification n = new Notification(this.getName(), w, optionalText);

        isEvaluated = false;
        notifyObservers(n);
    }

    public void setElseInstruction(InstructionModel newTarget) {
        elseTarget = newTarget;
    }

    public void setElseAddress(int newAddress) {
        if (newAddress != elseAddress) {
            System.out.println("IfInstructionModel: setting 'else' target from " + elseAddress + " to " + newAddress);
            elseAddress = newAddress;
        }
    }

    public int getElseAddress() {
        return elseAddress;
    }

    public void setEndInstruction(InstructionModel endInstruction) {
        endTarget = endInstruction;
    }

    public void setEndAddress(int newAddress) {
        if (newAddress != endAddress) {
            System.out.println("IfInstructionModel: setting 'end' target from " + endAddress + " to " + newAddress);
            endAddress = newAddress;
        }
    }

    public int getEndAddress() {
        return endAddress;
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
        String res = "";

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
        res += "_" + textValue + "_" + this.elseAddress + "_" + this.endAddress;

        return res;
    }

    @Override
    public void update(Notification n) {
        if (n.getName().equals("IfReply")) {
            System.out.println("If received a notification: <" + n.getName() + ", " + n.getOptions() + ">");
            // The terrain sent information about the target square.
            String tab[] = n.getOptions().split(" ");

            for (String s : tab) {
                System.out.print("<" + s + ">");
            }
            System.out.println("");

            String squareType = tab[1];
            int workerSerial = Integer.parseInt(tab[2]);
            String datacubeValue = tab[3];
            int observedWorkerId = Integer.parseInt(tab[4]);

            String options;
            if (makeChoice(squareType, workerSerial, datacubeValue, observedWorkerId)) {
                // Goto the THEN part.
                // Send a notification to the Worker,
                // saying that worker <workerSerial> must go to instruction <address>
                options = workerSerial + " +1"; // Next instruction.
            } else {
                // Goto the ELSE part.
                options = workerSerial + " " + elseAddress;
                System.out.println("ELSE address: " + elseAddress + ", END: " + endAddress);
            }
            System.out.println("    options = " + options);
            Notification branchNotif = new Notification("workerToAddress", null, options);
            System.out.println("Notification was created, options are <" + branchNotif.getOptions() + ">");
            notifyObservers(branchNotif);
        }
    }

    /**
     * Make the actual test of the If Instruction.
     *
     * @param squareType
     * @param workerSerial
     * @param dataCubeStringVal may be "_" if no cube exists; is a number String
     * otherwise.
     * @param observedWorkerSerial
     * @return true when the condition is true and we branch to the THEN part,
     * false otherwise and we branch to the ELSE part.
     */
    private boolean makeChoice(String squareType, int workerSerial, String dataCubeStringVal, int observedWorkerSerial) {

        int expectedValue;
        boolean result;
        try {
            expectedValue = Integer.parseInt(choiceValue);
            int dataCubeVal = Integer.parseInt(dataCubeStringVal);
            // Compare expectedValue with dataCubeVal.
            switch (this.currentBoolean) {
                case EQUAL:
                    result = dataCubeVal == expectedValue;
                    break;
                case GREATER_THAN:
                    result = dataCubeVal >= expectedValue;
                    break;
                case LOWER_THAN:
                    System.out.println("        makeChoice:");
                    System.out.println("        expected value = " + expectedValue + ", dataCube val = " + dataCubeVal);
                    result = dataCubeVal <= expectedValue;
                    System.out.println("        result = " + result);
                    break;
                case NOT_EQUAL:
                    result = dataCubeVal != expectedValue;
                    break;
                case STRICTLY_GREATER_THAN:
                    result = dataCubeVal > expectedValue;
                    break;
                case STRICTLY_LOWER_THAN:
                    result = dataCubeVal < expectedValue;
                    break;
                default:
                    result = true;
                    break;
            }

        } catch (NumberFormatException ex) {
            // Expected value is a String, not a number.
            result = true;
        }
        System.out.println("Choice made for worker " + workerSerial + ": " + result);
        return result;
    }
}
