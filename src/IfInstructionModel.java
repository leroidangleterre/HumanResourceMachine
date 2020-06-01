import java.util.ArrayList;

/**
 *
 * @author arthurmanoha
 */
public class IfInstructionModel extends InstructionModel implements Observable, Observer {

    // The direction in which we look for the object we want to test
    private CardinalPoint currentDirection;
    // The boolean we use to compare the direction and the expected value
    private BooleanConstant currentBoolean;
    private String textValue;
    private boolean isNumber;
    private int intValue;

    // What we expect the tested element to be (or not to be, depending on the equality sign).
    private MyChoiceBoxModel choiceBoxModel;

    // We send a notification to the terrain, which replies via another notification;
    // we then evaluate the condition.
    private boolean isEvaluated;
    private boolean logicValue;

    private int elseAddress;
    private int endAddress;
    private InstructionModel elseTarget;
    private InstructionModel endTarget;

    private static int NB_IIM_CREATED = 0;
    private int id;

    public IfInstructionModel() {
        super();
        elseAddress = 0;
        elseTarget = null;
        endAddress = 0;
        endTarget = null;
        observersList = new ArrayList<>();
        currentDirection = CardinalPoint.WEST;
        currentBoolean = BooleanConstant.LOWER_THAN;
        choiceBoxModel = null;
        textValue = "If ";
        id = NB_IIM_CREATED;
        NB_IIM_CREATED++;
    }

    public CardinalPoint getCardinalPoint() {
        return this.currentDirection;
    }

    public String getText() {
        return this.textValue;
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

//    /**
//     * Execute the IF: evaluate the condition, then set the worker's next
//     * address.
//     *
//     * @param date
//     * @param w
//     */
//    @Override
//    public void execute(int date, Worker w) {
//        super.execute(date, w);
//        // Analyse the three sub-components: the direction, the operator, and the choice box,
//        // and choose between going to the "then" or to the "else" block.
//
//        String optionalText = "42" + w.getSerial() + "-" + w.getDirection();
//        System.out.println("            IfInstructionModel.execute(date = " + date + ", worker " + w.getSerial());
//        Notification n = new Notification(this.getName(), w, optionalText);
//        System.out.println("new notification: " + n.getName() + ", " + n.getOptions());
//
//        isEvaluated = false;
//        notifyObservers(n);
//    }
    public void setElseInstruction(InstructionModel newTarget) {
        elseTarget = newTarget;
    }

    public void setElseAddress(int newAddress) {
        if (newAddress != elseAddress) {
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
        int i = 0;
        for (Observer observer : observersList) {
            System.out.println("IfInstructionModel updating observer n°" + i);
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
        res += this.currentDirection + "_";

        // If the second operand is a compass, we must ask the terrain about it.
        if (choiceBoxModel.isCompass()) {
            System.out.println("Second operand is a compass: <" + choiceBoxModel.getStringValue() + ">");
            res += choiceBoxModel.getStringValue();
        }

//        switch (this.currentBoolean) {
//        case EQUAL:
//            res += "==";
//            break;
//        case NOT_EQUAL:
//            res += "!=";
//            break;
//        case GREATER_THAN:
//            res += ">=";
//            break;
//        case LOWER_THAN:
//            res += "<=";
//            break;
//        case STRICTLY_GREATER_THAN:
//            res += ">";
//            break;
//        case STRICTLY_LOWER_THAN:
//            res += "<";
//            break;
//        }
        // add the expected type or value
        // Add the "then" address
        // add the "else" address
//        res += "_" + choiceBoxModel.getValue(); // + "_" + this.elseAddress + "_" + this.endAddress;
        return res;
    }

    @Override
    public void update(Notification n) {
        Worker w = (Worker) n.getObject();
        if (n.getName().equals("IfReply")) {
            // The terrain sent information about the target square.
            String tab[] = n.getOptions().split(" ");

            String squareType = tab[1];
            int workerSerial = Integer.parseInt(tab[2]);
            String datacubeValue = tab[3];
            int observedWorkerId = Integer.parseInt(tab[4]);

//            System.out.println("\n\nIf instruction " + this.id + " receives \"IfReply\" notification:");
//            System.out.println("square type: " + squareType);
//            System.out.println("current worker: " + workerSerial);
//            System.out.println("dataCube: " + datacubeValue);
//            System.out.println("observed worker: " + observedWorkerId);
            String options;
            if (makeChoice(squareType, workerSerial, datacubeValue, observedWorkerId)) {
                // Goto the THEN part.
                // Send a notification to the Worker,
                // saying that worker <workerSerial> must go to instruction <address>
                w.setCurrentAddress(w.getCurrentAddress() + 1);
            } else {
                // Goto the ELSE part: the first instruction after the "ELSE"
                w.setCurrentAddress(elseAddress + 1);
            }
        }
    }

    /**
     * Make the actual test of the If Instruction.
     *
     * @param squareType
     * @param workerSerial
     * @param dataCubeStringVal may be "_" if no cube exists; is a String
     * representing a number otherwise.
     * @param observedWorkerSerial
     * @return true when the condition is true and we branch to the THEN part,
     * false otherwise and we branch to the ELSE part.
     */
    private boolean makeChoice(String squareType, int workerSerial, String dataCubeStringVal, int observedWorkerSerial) {

        boolean result = false;

        if (choiceBoxModel.isNumber()) {
            try {
                int expectedValue = choiceBoxModel.getIntValue();
                int dataCubeVal = Integer.parseInt(dataCubeStringVal);

                // Compare expectedValue with dataCubeVal.
                result = compare(expectedValue, dataCubeVal, currentBoolean);

                System.out.println("If makeChoice: returning " + result);
                return result;
            } catch (NumberFormatException ex) {
                System.out.println("Error in comparing numbers. returning false.");
                return false;
            }
        } else {
            if (choiceBoxModel.isCompass()) {
                // Must compare the values of the two designated squares.
            } else {
                // Must test if designated square has the given type.
                System.out.println("IfInstrModel: TODO: test square against a given type.");
                result = false;
            }
            return result;
        }
    }

    private boolean compare(int firstValue, int secondValue, BooleanConstant bool) {
        boolean result;
        switch (this.currentBoolean) {
        case EQUAL:
            result = firstValue == secondValue;
            break;
        case GREATER_THAN:
            result = firstValue >= secondValue;
            break;
        case LOWER_THAN:
            result = firstValue <= secondValue;
            break;
        case NOT_EQUAL:
            result = firstValue != secondValue;
            break;
        case STRICTLY_GREATER_THAN:
            result = firstValue > secondValue;
            break;
        case STRICTLY_LOWER_THAN:
            result = firstValue < secondValue;
            break;
        default:
            System.out.println("Error in IfInstructionModel: unknown operator");
            result = false;
            break;
        }
        return result;
    }

    @Override
    public Notification createNotification() {
        // Worker is set via the ScriptModel.
        Notification n = new Notification(this);
        System.out.println("IfInstrModel.createNotification()");
        return n;
    }

    @Override
    public String toString() {
        System.out.println("IfInstrModel: choiceBoxModel returns " + choiceBoxModel.getValue());
        return getName() + " " + currentDirection + " " + currentBoolean + " "
                + choiceBoxModel.getValue() + " " + elseAddress + " " + endAddress;
    }

    public void setChoiceValue(String newChoiceVal) {
        if (choiceBoxModel == null) {
            System.out.println("IIM: ERROR, cBM is null");
        } else {
            choiceBoxModel.setValue(newChoiceVal);
        }
    }

    public String getChoiceValue() {
        return choiceBoxModel.getStringValue();
    }

    public void setChoiceBoxModel(MyChoiceBoxModel newChoiceBoxModel) {
        this.choiceBoxModel = newChoiceBoxModel;
    }
}
