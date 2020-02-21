/**
 * A boolean test component evaluates two elements (squares,values, ...) and
 * returns a boolean result.
 *
 * @author arthurmanoha
 */
class BooleanTest {

    /**
     * Where we look for an element to compare
     */
    private Compass compass;

    /**
     * The kind of test: equal, not equal, inferior to, ...
     */
    private BooleanButton operator;

    /**
     * The type that we hope to find in the direction given by compass, or an
     * integer value.
     */
    private Object testedObject;

    public BooleanTest() {
        this.compass = new Compass();
        this.operator = new BooleanButton();
        this.testedObject = null;
    }
}
