package list;

import java.util.Iterator;
import java.util.ListIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyListTest {

    public MyListTest() {
    }

    @Test
    public void testAddLast() {
        MyList<Integer> list = new MyList<Integer>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        MyListItem<Integer> item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
    }
        
    @Test
    public void testAddLast2(){
        MyList<Integer> list = new MyList<Integer>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        
        MyListItem<Integer> item = list.last;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.previous;
        assertNull(item);

        item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
}

    @Test
    public void testAddFirst() {
        MyList<Integer> list = new MyList<Integer>();
        list.addFirst(4);
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        MyListItem<Integer> item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
    }

    @Test
    public void testAddFirst2(){
        MyList<Integer> list = new MyList<Integer>();
        list.addFirst(4);
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        MyListItem<Integer> item = list.last;

        assertEquals(item.value, Integer.valueOf(4));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.previous;
        assertNull(item);
        item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
    }

    @Test
    public void testLastFirst() {
        MyList<Integer> list = new MyList<Integer>();
        list.addFirst(2);
        list.addLast(3);
        list.addFirst(1);
        list.addLast(4);
        MyListItem<Integer> item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
    }

    @Test
    public void testAddLastFirst2(){
        MyList<Integer> list = new MyList<Integer>();
        list.addLast(3);
        list.addFirst(2);
        list.addLast(4);
        list.addFirst(1);
        MyListItem<Integer> item = list.last;

        assertEquals(item.value, Integer.valueOf(4));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.previous;
        assertNull(item);
        item = list.first;
        assertEquals(item.value, Integer.valueOf(1));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.previous;
        assertEquals(item.value, Integer.valueOf(2));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(3));
        item = item.next;
        assertEquals(item.value, Integer.valueOf(4));
        item = item.next;
        assertNull(item);
    }

    @Test
    public void testListIterator() {
        MyList<Integer> list = new MyList<Integer>();
        assertTrue (list.listIterator() instanceof MyIterator);
        ListIterator<Integer> it1 = list.listIterator();
        ListIterator<Integer> it2 = list.listIterator();
        assertFalse(it1 == it2);
    }

    @Test
    public void testIterator() {
        MyList<Integer> list = new MyList<Integer>();
        assertTrue (list.iterator() instanceof MyIterator);
        Iterator<Integer> it1 = list.iterator();
        Iterator<Integer> it2 = list.iterator();
        assertFalse(it1 == it2);
    }

    @Test
    public void testOtherType(){
        MyList<String> list = new MyList<String>();
        list.addLast("ccc");
        list.addFirst("bbb");
        list.addLast("ddd");
        list.addFirst("aaa");
        MyListItem<String> item = list.first;
        assertEquals(item.value,"aaa");
        item = item.next;
        assertEquals(item.value,"bbb");
        item = item.next;
        assertEquals(item.value,"ccc");
        item = item.next;
        assertEquals(item.value,"ddd");
        item = item.next;
        assertNull(item);
    }

}