package MyWorm;

import java.util.ArrayList;
import java.util.LinkedList;

public class MyWorm implements Worm {
    public Segment head;
    public Segment tail;

    public MyWorm() {
    }

    public MyWorm(String[] types) {
        for (String type : types) {
            add(type);
        }
    }

    public Segment getHead() {
        return head;
    }

    public Segment getTail() {
        return tail;
    }

    public void add(String type) {
        boolean found = false;
        for (String segmentType : Segment.SEGMENT_TYPES) {
            if (segmentType.equals(type)) {
                found = true;
            }
        }
        if (!found) {
            throw new BiohazardException(type);
        }
        Segment n = new Segment();
        n.type = type;
        n.forwardVessel = tail;
        if (getHead() == null) {
            head = n;
        } else {
            tail.backwardVessel = n;
        }
        tail = n;
    }

    @Override
    public Worm split(int pos) {
        MyWorm second = new MyWorm();
        Segment split = getSegment(pos + 1);
        if (split == null) {
            return second;
        }
        second.head = split;
        second.tail = tail;
        tail = split.forwardVessel;
        if (tail != null) {
            tail.backwardVessel = null;
        } else {
            head = null;
        }
        second.head.forwardVessel = null;
        return second;
    }

    @Override
    public void splice(int pos, Worm second) {
        Worm third = split(pos - 1);
        ArrayList<String> secondParts = second.dissect();
        for (String type : secondParts) {
            add(type);
        }
        ArrayList<String> thirdParts = third.dissect();
        for (String type : thirdParts) {
            add(type);
        }
    }

    @Override
    public ArrayList<String> dissect() {
        ArrayList<String> segments = new ArrayList<>();
        if (head != null) {
            segments.add(head.type);
            Segment n = head.backwardVessel;
            while (n != null) {
                segments.add(n.type);
                n = n.backwardVessel;
            }
        }
        head = null;
        tail = null;

        return segments;
    }

    @Override
    public Worm cloneWorm() {
        MyWorm worm = new MyWorm();
        if (head != null) {
            worm.add(head.type);

            Segment n = head.backwardVessel;

            while (n != null) {
                worm.add(n.type);
                n = n.backwardVessel;
            }
        }
        return worm;
    }

    @Override
    public Segment getSegment(int n) {
        Segment segment = getHead();
        for (int i = 0; i < n; i++) {
            segment = segment.backwardVessel;
        }
        return segment;
    }
}
