package list;

import java.util.ListIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestContinuity {

    MyList<Integer> empty;
    MyList<Integer> full;
    ListIterator<Integer> emptyIt;
    ListIterator<Integer> fullIt;

    @Before
    public void setUp() {
        empty = new MyList<Integer>();
        full = new MyList<Integer>();
        full.addLast(1);
        full.addLast(2);
        full.addLast(3);
        full.addLast(4);
        full.addLast(5);
        emptyIt = empty.listIterator();
        fullIt = full.listIterator();
    }

    @Test
    public void testRemove5() {
        int [] pole1 = {1,2,3,4,5};
        assertContent(full, pole1);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertEquals(fullIt.next(), Integer.valueOf(4));
        fullIt.remove();
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertEquals(fullIt.previous(), Integer.valueOf(2));
    }

        private void assertContent(MyList<Integer> list, int[]array){
        int i = 0;
        int value;
        ListIterator<Integer> it;
        for ( it = list.listIterator(); it.hasNext();){
            value = it.next();
            assertTrue(i < array.length); //pole je kratsi?
            assertEquals(value, array[i]);
            i++;
        }
        assertEquals(i, array.length); //stejna delka pole a listu
        while (it.hasPrevious()){
            i--;
            value = it.previous();
            assertEquals(value, array[i]);
        }
        assertEquals(i, 0);
    }
}