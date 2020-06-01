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
    private Compass compass;

    public MyChoiceBoxModel(int val) {
        isNumber = true;
        intValue = val;
        textValue = "" + val;
        isCompass = false;
        compass = null;
    }

    public MyChoiceBoxModel(String text) {
        isNumber = false;
        intValue = 0;
        textValue = text;
        isCompass = false;
        compass = null;
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
            if (compass == null) {
//                compass = new Compass();
                System.out.println("MyChoiceBoxModel.toggle(): ERROR, compass is null");
            }
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
                if (!isCompass) {
                    this.isCompass = true;
                    if (this.compass == null) {
                        compass = new Compass();
                    }
                }
                compass.setDirection(cp);
            } catch (IllegalArgumentException exc) {
                // The exception simply says that the value is not a cardinal point,
                // which means it is a normal text value.
            }
            this.textValue = s;
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

    public String getValue() {
        String result = "";
        if (this.isNumber) {
            result = "" + this.intValue;
        } else if (this.isCompass) {
            result = compass.toString();
        } else {
            result = this.textValue;
        }
        return result;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        if (isCompass) {
            return compass.toString();
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

    void setCompass(Compass compass) {
        this.compass = compass;
    }
}
