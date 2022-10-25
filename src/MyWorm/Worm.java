package MyWorm;

import java.util.ArrayList;

/**
 * A worm should have a head and tail instance field that allows the getHead()
 * and getTail() methods to work. The worm is connected in both directions with
 * blood vessels. The backward flowing vessels go from the head to the tail, and
 * the forward flowing vessels go from the tail to the head. See Segment.
 */
public interface Worm {

    /**
     * Return a reference to the first segment of the worm. Return null if the worm
     * has zero length.
     */
    public Segment getHead();

    /**
     * Return a reference to the last segment of the worm. Return null if the worm
     * has zero length.
     */
    public Segment getTail();

    /**
     * Add a single segment to the worm's tail. Throw a BiohazardException if the
     * string "type" is not one of the strings in Segment.SEGMENT_TYPES.
     * Implementation: (1) Create a boolean named found and assign it a value of
     * false. (2.a) Create a loop that iterates through all Segment.SEGMENT_TYPES.
     * (2.b) If, inside the loop, the value to be added is found, set the variable
     * found to true. (3) When the loop is done, throw a BiohazardException with the
     * value of type if found is false. (4) Instantiate a new variable of type
     * Segment named n. (5) Assign the forwardVessl of of n with the current worm's
     * tail. (6) If the head of the current worm is null, assign the head with n.
     * (7) If the head of the current worm is not null, assign the backwardVessel of
     * the tail to point to n. (8) Assign n to the tail.
     */
    public void add(String type);

    /**
     * Split worm /after/ index 'pos'. This worm will be cut, and the tail will be
     * returned as new Worm. To implement: (1) Create a variable of type Worm named
     * second and assign it a new worm. (2) Create a variable of type Segment named
     * split and assign it with the (pos+1) segment of the current worm. HINT: You
     * already wrote a getSegment() method. (3) if split is null, return second. (4)
     * Assign the head of the second worm with split. (5) Assign the tail of the
     * second worm with the current worms tail. (6) Assign the tail of the current
     * worm with split.forwardVessel. (7) if the tail of the current worm is not
     * null, set the backward vessel of the tail of the current worm to null. (8) if
     * the tail of the current worm is null, set the head of the current worm to
     * null. (9) Assign the forwardVessel of the head of the second worm to null.
     * return second
     */
    public Worm split(int pos);

    /**
     * Add new set of worm body segments /before/ index 'pos' (where pos must be
     * less than or equal to the length of worm). To implement: (1) create a
     * variable named "third" of type Worm and assign it the value of split(pos-1)
     * (2) dissect the second worm and add all its parts to the current worm (3)
     * dissect the third worm and add all its parts to the current worm.
     */
    public void splice(int pos, Worm second);

    /**
     * Return the worm as in an ArrayList of Segments. This should also empty out
     * the list. Implementation: Write a loop that starts at the head of the worm,
     * iterates through each segment following backwardVessels, and appends the
     * "type" field of the segment to an arraylist as it goes. At the end, set the
     * head and tail to null.
     */
    public ArrayList<String> dissect();

    /**
     * Clone the worm, producing a copy of the old worm. This means creating copies
     * of all the old worm's body segments.
     */
    public Worm cloneWorm();

    /**
     * Some output, good for debugging.
     */
    public String toString();

    /**
     * Return a reference to the nth segment of the worm. To implement this, write a
     * loop that iterates from the worms head, following the backwardVessels, for n
     * iterations. Return the final segment of the iteratin.
     */
    public Segment getSegment(int n);
}
