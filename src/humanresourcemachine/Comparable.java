package humanresourcemachine;

/**
 * Instances of the classes that implement this interface will be compared
 * against each other. The value of a datacube may be compared to that of
 * another data cube; a square may be compared to the class (Worker, or Wall) to
 * check its content.
 *
 * @author arthurmanoha
 */
public interface Comparable {

    public boolean isLessThan(Comparable other);

    public boolean isEqualTo(Comparable other);

    public boolean isMoreThan(Comparable other);

    public boolean isDifferentThan(Comparable other);
}
