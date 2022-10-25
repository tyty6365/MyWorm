package MyWorm;

import java.lang.reflect.*;
import java.util.*;

public class WormTester {

    public static boolean eq(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equals(b);
    }

    public static String makeSeg(int n) {
        return Segment.SEGMENT_TYPES[n];
    }

    public static String asString(Worm w) {
        StringBuilder sb = new StringBuilder();
        Segment s = w.getHead();
        if (s == null)
            return "**";
        sb.append('*');
        boolean first = true;
        while (s != null) {
            if (first)
                first = false;
            else
                sb.append("-");
            sb.append(s.type);
            s = s.backwardVessel;
        }
        sb.append('*');
        return sb.toString();
    }

    public static void print(Worm w) {
        System.out.println(asString(w));
    }

    public static boolean eq2(ArrayList<String> al, List<String> li) {
        if (al.size() != li.size())
            return false;
        for (int i = 0; i < al.size(); i++) {
            String i1 = al.get(i);
            String i2 = li.get(i);
            if (!i1.equals(i2))
                return false;
        }
        return true;
    }

    public static boolean eq(Worm w1, Worm w2) {
        System.out.println(asString(w1) + " == " + asString(w2));
        Segment x1 = w1.getHead();
        Segment x2 = w2.getHead();
        while (true) {
            if (x1 == null && x2 == null)
                return true;
            if (x1 == null || x2 == null)
                return false;
            if (!x1.type.equals(x2.type))
                return false;
            x1 = x1.backwardVessel;
            x2 = x2.backwardVessel;
        }
    }

    public static boolean eq(ArrayList<String> al, List<String> li) {
        boolean result = eq2(al, li);
        if (!result) {
            System.out.println("Expected: " + li);
            System.out.println("     Got: " + al);
        }
        return result;
    }

    static ArrayList<String> fromWorm(Worm w) {
        checkWorm(w);
        ArrayList<String> al = new ArrayList<>();
        if (w.getHead() == null)
            assert w.getTail() == null;
        for (int i = 0; true; i++) {
            Segment s = w.getSegment(i);
            assert s != null : String.format("getSegment(%d) returned null", i);
            al.add(s.type);
            if (s == w.getTail())
                break;
        }
        return al;
    }

    public static boolean eq(Worm w, List<String> li) {
        System.out.println(asString(w) + " == " + li);
        ArrayList<String> al = fromWorm(w);
        boolean result = eq2(al, li);
        if (!result) {
            System.out.println("Expected: " + li);
            System.out.println("     Got: " + al);
        }
        return result;
    }

    static String[] toSegments(int... ar) {
        String[] s = new String[ar.length];
        for (int i = 0; i < ar.length; i++)
            s[i] = Segment.SEGMENT_TYPES[ar[i]];
        return s;
    }

    static List<String> toSegmentList(int... ar) {
        List<String> s = new ArrayList<String>();
        for (int i = 0; i < ar.length; i++)
            s.add(Segment.SEGMENT_TYPES[ar[i]]);
        return s;
    }

    static void checkWorm(Worm w) {
        Segment x = w.getHead();
        if (x == null) {
            assert w.getTail() == null : "If getHead() is null, getTail() should also be null";
            return;
        }
        boolean foundTail = false;
        assert x.forwardVessel == null : "Head should have forwardVessel == null";
        while (x != null) {
            if (x == w.getTail()) {
                foundTail = true;
            }
            if (x.backwardVessel == null)
                assert x == w.getTail() : "If the backwardVessel is null, this should be the tail";
            else
                assert x.backwardVessel.forwardVessel == x;
            if (x.forwardVessel == null)
                assert x == w.getHead() : "If the forwardVessel is null, this should be the head";
            else
                assert x.forwardVessel.backwardVessel == x;

            x = x.backwardVessel;
        }
        assert foundTail;
    }

    static void biohazardTest(String type, Worm w) {
        try {
            w.add(type);
            assert false : "Biohazard test of add() method failed!";
        } catch (BiohazardException bio) {
            String msg = bio.toString();
            assert msg.contains("'" + type + "'") : "Pass the type string to the BiohazardException constructor";
            System.out.println(msg);
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            assert (false);
            throw new Error("Assertions are not enabled");
        } catch (AssertionError ae) {
        }
        assert args.length >= 1 : "You must specify which implementation you are testing";
        Class<?> c = Class.forName(args[0]);
        String[] arr1 = toSegments(1, 2, 3, 6, 7, 8, 9);
        String[] arr2 = toSegments(4, 5);

        Constructor con0 = c.getDeclaredConstructor();
        assert Modifier.isPublic(con0.getModifiers()) : "Constructor should be public";

        try {
            Worm worm = (Worm) con0.newInstance();
            assert worm.getHead() == null : "Newly constructed worm should have null head";
            assert worm.getTail() == null : "Newly constructed worm should have null tail";
            biohazardTest("Alien DNA", worm);
            biohazardTest("Human head", worm);
            String[] segs = Segment.growSegments(3);
            for (int i = 0; i < segs.length; i++) {
                String s = segs[i];
                System.out.println("Adding: " + s);
                worm.add(s);
                assert worm.getTail() != null : "The add() method does not work";
                assert eq(worm.getTail().type, s) : "The add() method does not work";
                for (int k = 0; i < i; k++) {
                    Segment s2 = worm.getSegment(k);
                    assert s2 != null : "Either the add() or getSegment() method is not working";
                    assert eq(s2.type, segs[k]) : "Either the add() or getSegment() method is not working";
                }
            }
        } catch (BiohazardException bio) {
            System.out.println(bio);
        }

        Constructor con = c.getDeclaredConstructor(new Class[] { String[].class });
        assert Modifier.isPublic(con.getModifiers()) : "Constructor should be public";

        for (int i = 0; i < 10; i++) {
            String[] cargs = Segment.growSegments(5 + Segment.RAND.nextInt(5));
            Worm worm = (Worm) con.newInstance((Object) cargs);
            checkWorm(worm);
            for (int k = 0; k < cargs.length; k++) {
                Segment seg = worm.getSegment(k);
                assert seg != null : "Either the getSegment() or the MyWorm(String[] types) does not work";
                assert seg.type != null : "Either the getSegment() or the MyWorm(String[] types) does not work";
                assert eq(seg.type, cargs[k]) : "Either the getSegment() or the MyWorm(String[] types) does not work";
            }
            if (i == 0) {
                try {
                    worm.add("Alien DNA");
                    assert false : "Biohazard test failed!";
                } catch (BiohazardException bio) {
                    System.out.println(bio);
                }
                try {
                    worm.add("Human head");
                    assert false : "Biohazard test failed!";
                } catch (BiohazardException bio) {
                    System.out.println(bio);
                }
            }

            Worm cworm = worm.cloneWorm();
            assert cworm != null : "The cloneWormm() method doesn't work";
            checkWorm(cworm);
            assert cworm.getHead() != worm.getHead() : "A cloned worm should have a different head";
            assert cworm.getTail() != worm.getTail() : "A cloned worm should have a different tail";
            assert eq(worm, cworm);
            cworm.dissect();
            assert cworm.getHead() == null : "The dissect method should empty the worm";
            assert cworm.getTail() == null : "The dissect method should empty the worm";
        }

        /* basic testing */

        Worm worm1 = (Worm) con.newInstance((Object) arr1);
        assert eq(worm1.cloneWorm().dissect(), Arrays.asList(arr1))
                : "The dissect method did not return a correct result";
        Worm cworm = worm1.cloneWorm();
        cworm.dissect();
        Worm worm2 = (Worm) con.newInstance((Object) arr2);
        System.out.println("worm1: " + asString(worm1));
        System.out.println("worm2: " + asString(worm1));

        Worm worm3 = worm1.split(4);
        assert worm3 != null : "The split method returned null";
        System.out.println("worm3: " + asString(worm3));
        assert (eq(worm1.cloneWorm().dissect(), toSegmentList(1, 2, 3, 6, 7)));
        assert (eq(worm1, toSegmentList(1, 2, 3, 6, 7)));
        assert (eq(worm3.cloneWorm().dissect(), toSegmentList(8, 9)));
        assert (eq(worm3, toSegmentList(8, 9)));

        worm3 = worm1.split(4);
        assert (eq(worm1.cloneWorm().dissect(), toSegmentList(1, 2, 3, 6, 7)));
        assert (eq(worm3.cloneWorm().dissect(), toSegmentList()));

        worm1 = (Worm) con.newInstance((Object) arr1);
        System.out.printf("%s.splice(1, %s)%n", asString(worm1), asString(worm2));
        worm1.splice(1, worm2);
        assert (eq(worm1.cloneWorm().dissect(), toSegmentList(1, 4, 5, 2, 3, 6, 7, 8, 9)))
                : "The worm splice method did not work";

        worm2 = (Worm) con.newInstance((Object) arr2);
        worm1.splice(0, worm2);
        assert (eq(worm1.cloneWorm().dissect(), toSegmentList(4, 5, 1, 4, 5, 2, 3, 6, 7, 8, 9)));

        worm2 = (Worm) con.newInstance((Object) arr2);
        worm1.splice(11, worm2);
        assert (eq(worm1.cloneWorm().dissect(), toSegmentList(4, 5, 1, 4, 5, 2, 3, 6, 7, 8, 9, 4, 5)));

        // Add random tests
        final Random rand = new Random();
        for (int k = 0; k < 10; k++) {
            ArrayList<String> tst1 = new ArrayList<>();
            ArrayList<String> tst2 = new ArrayList<>();
            String[] start = Segment.growSegments(1);
            int size = rand.nextInt(10) + 20;
            worm1 = (Worm) con.newInstance((Object) start);
            tst1.add(start[0]);
            start = Segment.growSegments(1);
            worm2 = (Worm) con.newInstance((Object) start);
            tst2.add(start[0]);
            for (int i = 0; i < size; i++) {
                start = Segment.growSegments(1);
                worm3 = (Worm) con.newInstance((Object) start);
                worm1.splice(i + 1, worm3);
                tst1.add(start[0]);
                start = Segment.growSegments(1);
                worm3 = (Worm) con.newInstance((Object) start);
                worm2.splice(i + 1, worm3);
                tst2.add(start[0]);
            }
            assert eq(worm1.cloneWorm().dissect(), tst1);
            assert eq(worm2.cloneWorm().dissect(), tst2);
            int pos1 = rand.nextInt(10) + 5;
            int pos2 = rand.nextInt(5) + 1;
            ArrayList<String> tst3 = new ArrayList<>();
            for (int i = 0; i < tst2.size(); i++) {
                if (pos2 == i) {
                    for (int j = pos1 + 1; j < tst1.size(); j++) {
                        tst3.add(tst1.get(j));
                    }
                }
                tst3.add(tst2.get(i));
            }
            worm3 = worm1.split(pos1);
            worm2.splice(pos2, worm3);
            assert eq(worm2.cloneWorm().dissect(), tst3);
            tst3.clear();
            for (int i = 0; i <= pos1; i++)
                tst3.add(tst1.get(i));
            assert eq(worm1.cloneWorm().dissect(), tst3);
        }

        System.out.println("All tests passed.");
    }
}

