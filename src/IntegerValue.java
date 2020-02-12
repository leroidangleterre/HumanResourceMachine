/**
 *
 * @author arthurmanoha
 */
class IntegerValue implements Comparable {

    private int value;

    public IntegerValue(int newVal) {
        value = newVal;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isLessThan(Comparable other) {
        if (other instanceof IntegerValue) {
            return this.value <= ((IntegerValue) other).value;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEqualTo(Comparable other) {
        if (other instanceof IntegerValue) {
            return this.value == ((IntegerValue) other).value;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMoreThan(Comparable other) {
        if (other instanceof IntegerValue) {
            return this.value >= ((IntegerValue) other).value;
        } else {
            return false;
        }
    }

    @Override
    public boolean isDifferentThan(Comparable other) {
        if (other instanceof IntegerValue) {
            return this.value != ((IntegerValue) other).value;
        } else {
            return false;
        }
    }

}
