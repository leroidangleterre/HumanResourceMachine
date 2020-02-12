/**
 *
 * @author arthurmanoha
 */
public class MyChoiceBoxModel {

    // The current value of the choice box. That String may represent an element such as a Worker or a Wall, or an integer value.
    private String textValue;

    // When the choice box represents a value, this flag is set and the attribute carries the value.
    private boolean isNumber;
    private int intValue;

    // The instruction that the choice box will pilot.
    private InstructionModel instruction;

    public MyChoiceBoxModel(int val) {
        isNumber = true;
        intValue = val;
        textValue = "" + val;
    }

    public MyChoiceBoxModel(String text) {
        isNumber = false;
        intValue = 0;
        textValue = text;
    }

    public void setInstructionModel(InstructionModel newInst) {
        instruction = newInst;
    }

    /**
     * Togge the type of the choice box: IntValue -> Empty -> Wall -> Hole ->
     * Worker -> DataCube -> Intvalue
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
                textValue = "0";
                isNumber = false;
                break;
            default: // Integer value
                textValue = "Empty";
                isNumber = true;
                break;
        }
    }

    public String getValue() {
        return this.textValue;
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
}
