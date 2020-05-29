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

    public MyChoiceBoxModel(int val) {
        isNumber = true;
        intValue = val;
        textValue = "" + val;
        isCompass = false;
    }

    public MyChoiceBoxModel(String text) {
        isNumber = false;
        intValue = 0;
        textValue = text;
        isCompass = false;
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
            textValue = "Compass";
            isNumber = false;
            isCompass = true;
            break;
        case "Compass":
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
        try {
            if (this.isNumber) {
                return "" + this.intValue;
            } else {
                return this.textValue;
            }
        } catch (NullPointerException e) {
            System.out.println("NPE caught");
            return "NPE_CAUGHT";
        }
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        return this.textValue;
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
}
