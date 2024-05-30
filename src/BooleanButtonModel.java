/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        case NOT_EQUAL:
            return "!=";
        case GREATER_THAN:
            return ">=";
        case LOWER_THAN:
            return "<=";
        case STRICTLY_GREATER_THAN:
            return ">";
        case STRICTLY_LOWER_THAN:
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
            value = BooleanConstant.NOT_EQUAL;
            break;
        case NOT_EQUAL:
            value = BooleanConstant.GREATER_THAN;
            break;
        case GREATER_THAN:
            value = BooleanConstant.LOWER_THAN;
            break;
        case LOWER_THAN:
            value = BooleanConstant.STRICTLY_GREATER_THAN;
            break;
        case STRICTLY_GREATER_THAN:
            value = BooleanConstant.STRICTLY_LOWER_THAN;
            break;
        case STRICTLY_LOWER_THAN:
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
