package MyWorm;

import java.util.Random;

public class Segment {
    /**
     * The segment type
     */
    String type;
    public final static String[] SEGMENT_TYPES = { "Heart", "Liver", "Gut", "Eyestalk", "Antennae", "Brain", "Claws",
            "Kidneys", "Pancreas", "VenomSack", "Mouth" };
    public final static Random RAND = new Random();

    /**
     * Go to the nutrient and stem cell bath and extract a random set of Worm
     * segments.
     */
    public final static String[] growSegments(int num) {
        String[] segments = new String[num];
        for (int i = 0; i < num; i++)
            segments[i] = SEGMENT_TYPES[RAND.nextInt(SEGMENT_TYPES.length)];
        return segments;
    }

    /**
     * The forward blood vessels carry blood and nourishment from the tail, through
     * the segmented to the head of the worm's body.
     */
    Segment forwardVessel;
    /**
     * The backward blood vessels carry blood and nourishment from the head, through
     * the segmented to the tail of the worm's body.
     */
    Segment backwardVessel;

    public Segment() {
    }

    public Segment(String d, Segment p, Segment n) {
        type = d;
        forwardVessel = p;
        backwardVessel = n;
    }

    /**
     * Returns a String representing this, and following elements.
     */
    public String toString() {
        String ret = type;
        if (backwardVessel != null)
            return "(" + ret + ")-" + backwardVessel.toString();
        else
            return "(" + ret + ")";
    }
}
