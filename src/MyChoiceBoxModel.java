/**
 *
 * @author arthurmanoha
 */
public class MyChoiceBoxModel extends MyDefaultModel {

    // The current value of the choice box. That String may represent an element such as a Worker or a Wall, or an integer value.
    private String textValue;

    // When the choice box represents a value, this flag is set and the attribute carries the value.
    private boolean isNumber;
    private int intValue;

    // The instruction that the choice box will pilot.
    private InstructionModel instruction;
    private boolean isCompass;
    private CompassModel compassModel;

    public MyChoiceBoxModel(int val) {
        isNumber = true;
        intValue = val;
        textValue = "" + val;
        isCompass = false;
        compassModel = null;
    }

    public MyChoiceBoxModel(String text) {
        isNumber = false;
        intValue = 0;
        textValue = text;
        isCompass = false;
        compassModel = null;
    }

    public void setInstructionModel(InstructionModel newInst) {
        instruction = newInst;
    }

    /**
     * Togge the type of the choice box: IntValue -> Empty -> Wall -> Hole ->
     * Worker -> DataCube -> Compass -> Intvalue
     */
    public void toggle() {

        switch (textValue) {
        case "Empty":
            textValue = "Wall";
            isNumber = false;
            break;
        case "Wall":
            textValue = "Hole";
            isNumber = false;
            break;
        case "Hole":
            textValue = "Ground";
            isNumber = false;
            break;
        case "Ground":
            textValue = "Worker";
            isNumber = false;
            break;
        case "Worker":
            textValue = "DataCube";
            isNumber = false;
            break;
        case "DataCube":
            textValue = "NORTH";
            isNumber = false;
            isCompass = true;
            compassModel.setValue("NORTH");
            compassModel.setAllowMultipleDirections(false);
            break;
        case "NORTH":
        case "SOUTH":
        case "EAST":
        case "WEST":
            textValue = "0";
            isNumber = true;
            isCompass = false;
            break;
        default: // Integer value
            textValue = "Empty";
            isNumber = false;
            break;
        }
    }

    public void setValue(String s) {
        try {
            this.intValue = Integer.parseInt(s);
            // Conversion went OK, value is a number.
            this.isNumber = true;
        } catch (NumberFormatException e) {
            // Value is an actual String.
            this.isNumber = false;

            try {
                // If the value is a cardinal point, we need a compass.
                CardinalPoint cp = CardinalPoint.valueOf(s);
                compassModel.setValue(cp);
                this.isCompass = true;
                this.textValue = cp.toString();
            } catch (IllegalArgumentException exc) {
                // The exception simply says that the value is not a cardinal point,
                // which means it is a normal text value.
                this.isCompass = false;
                this.isNumber = false;
                this.textValue = s;
            }
        }
    }

    /**
     * Increase the number value of the box.
     *
     */
    public void increaseValue() {
        if (isNumber) {
            intValue++;
        }
    }

    /**
     * Decrease the number value of the box.
     *
     */
    public void decreaseValue() {
        if (isNumber) {
            intValue--;
        }
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        if (isCompass) {
            return compassModel.getValue().name();
        } else if (isNumber) {
            return this.intValue + "";
        } else {
            return this.textValue;
        }
    }

    public boolean isNumber() {
        return this.isNumber;
    }

    public boolean isCompass() {
        return this.isCompass;
    }

    @Override
    public void selectContent() {
    }

    public void setCompassModel(CompassModel newModel) {
        this.compassModel = newModel;
    }

    public boolean isSquareType() {
        return textValue.equals("Ground")
                || textValue.equals("Wall")
                || textValue.equals("Hole")
                || textValue.equals("Input")
                || textValue.equals("Output");
    }

    public void setAllowMultipleDirections(boolean areMultipleDirectionsAllowed) {
        this.compassModel.setAllowMultipleDirections(areMultipleDirectionsAllowed);
    }
}
