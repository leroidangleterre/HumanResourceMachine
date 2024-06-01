/**
 *
 * @author arthurmanoha
 */
public class BooleanButtonModel extends MyDefaultModel {

    private BooleanConstant value;

    public BooleanButtonModel() {
        value = BooleanConstant.EQUAL;
    }

    public String getText() {
        switch (value) {
        case EQUAL:
            return "==";
        case NOTEQUAL:
            return "!=";
        case GREATERTHAN:
            return ">=";
        case LOWERTHAN:
            return "<=";
        case STRICTLYGREATERTHAN:
            return ">";
        case STRICTLYLOWERTHAN:
            return "<";
        default:
            return "?";
        }
    }

    public void setValue(BooleanConstant newValue) {
        this.value = newValue;
    }

    public void setValue(String newValue) {
        this.setValue(BooleanConstant.valueOf(newValue));
    }

    public BooleanConstant getValue() {
        return this.value;
    }

    public void toggle() {
        switch (value) {
        case EQUAL:
            value = BooleanConstant.NOTEQUAL;
            break;
        case NOTEQUAL:
            value = BooleanConstant.GREATERTHAN;
            break;
        case GREATERTHAN:
            value = BooleanConstant.LOWERTHAN;
            break;
        case LOWERTHAN:
            value = BooleanConstant.STRICTLYGREATERTHAN;
            break;
        case STRICTLYGREATERTHAN:
            value = BooleanConstant.STRICTLYLOWERTHAN;
            break;
        case STRICTLYLOWERTHAN:
            value = BooleanConstant.EQUAL;
            break;
        default:
            break;
        }
    }

    @Override
    public void selectContent() {
    }
}
