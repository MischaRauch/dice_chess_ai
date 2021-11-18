import logic.PieceAndSquareTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class test {

    public static void main(String args[]) {
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a.add(1);
        }
        List<Integer> b = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            b.add(2);
        }
        ListIterator litr = a.listIterator();
        while(litr.hasNext()) {
            int t = (int) litr.next();
            litr.set(3);
            System.out.println(t);
            ListIterator litr2 = b.listIterator();
            while(litr2.hasNext()) {
                int t2 = (int) litr2.next();
                System.out.println(t2);
            }
        }
        List<Integer> actualList = new ArrayList<Integer>();
        litr.forEachRemaining((aaa)->actualList.add((Integer) aaa));

    }

}
