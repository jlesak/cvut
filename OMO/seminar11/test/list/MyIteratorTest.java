package list;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyIteratorTest {
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
    public void testHasNext() {
        assertTrue(fullIt.hasNext());
        assertFalse(emptyIt.hasNext());
        fullIt.next();
        fullIt.next();
        fullIt.next();
        fullIt.next();
        fullIt.next();
        assertFalse(fullIt.hasNext());
    }

    @Test
    public void testHasPrevious(){
        assertFalse(fullIt.hasPrevious());
        assertFalse(emptyIt.hasPrevious());
        fullIt.next();
        assertTrue(fullIt.hasPrevious());
    }

    @Test
    public void testNextExceptioned() {
        boolean a;
        try{
            a = false;
            emptyIt.next();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
        assertTrue(fullIt.hasNext());
        fullIt.next();
        fullIt.next();
        assertTrue(fullIt.hasNext());
        fullIt.next();
        fullIt.next();
        assertTrue(fullIt.hasNext());
        fullIt.next();
        assertFalse(fullIt.hasNext());
        try{
            a = false;
            fullIt.next();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
    }


    @Test
    public void testPreviousExceptioned() {
        boolean a;
        try{
            a = false;
            emptyIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
        try{
            a = false;
            fullIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);

        fullIt.next();
        fullIt.next();
        fullIt.previous();
        fullIt.previous();
        
        try{
            a = false;
            fullIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
    }


    @Test
    public void testNext() {
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(4));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(5));
        assertFalse(fullIt.hasNext());
    }


    @Test
    public void testPrevious() {
        testNext();
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(5));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(4));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(2));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        assertFalse(fullIt.hasPrevious());
        boolean a;
        try{
            a = false;
            fullIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
        try{
            a = false;
            emptyIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
    }

    @Test
    public void testNextPrevious() {
        testNext();
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(5));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(4));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(2));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        assertFalse(fullIt.hasPrevious());
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        boolean a;
        try{
            a = false;
            fullIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
        try{
            a = false;
            emptyIt.previous();
        } catch (NoSuchElementException ex) {a=true;}
        assertTrue(a);
    }


    @Test
    public void testNextIndex() {
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 0);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 1);
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 2);
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 3);
        assertEquals(fullIt.next(), Integer.valueOf(4));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 4);
        assertEquals(fullIt.next(), Integer.valueOf(5));
        assertFalse(fullIt.hasNext());
        assertEquals(fullIt.nextIndex(), 5);

        assertEquals(emptyIt.nextIndex(), 0);
    }

    @Test
    public void testPreviousIndex() {
        testNextIndex();
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), 4);
        assertEquals(fullIt.nextIndex(), 5);
        assertEquals(fullIt.previous(), Integer.valueOf(5));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), 3);
        assertEquals(fullIt.nextIndex(), 4);
        assertEquals(fullIt.previous(), Integer.valueOf(4));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), 2);
        assertEquals(fullIt.nextIndex(), 3);
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.previousIndex(), 1);
        assertEquals(fullIt.nextIndex(), 2);
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertEquals(fullIt.previousIndex(), 2);
        assertEquals(fullIt.nextIndex(), 3);
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(3));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), 1);
        assertEquals(fullIt.nextIndex(), 2);
        assertEquals(fullIt.previous(), Integer.valueOf(2));
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), 0);
        assertEquals(fullIt.nextIndex(), 1);
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        assertFalse(fullIt.hasPrevious());
        assertEquals(fullIt.previousIndex(), -1);
        assertEquals(fullIt.nextIndex(), 0);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertEquals(fullIt.previousIndex(), 0);
        assertEquals(fullIt.nextIndex(), 1);
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        assertEquals(fullIt.previousIndex(), -1);
        assertEquals(fullIt.nextIndex(), 0);
        assertEquals(emptyIt.previousIndex(), -1);
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

    @Test
    public void testRemove1() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {1,2,4,5};
        boolean a = false;
        assertContent(full, pole1);
                a = false;
        try{
            fullIt.remove();
        } catch (IllegalStateException ex) {a=true;}
        assertTrue(a);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertEquals(fullIt.next(), Integer.valueOf(3));
        fullIt.remove();
        a = false;
        try{
            fullIt.remove();
        } catch (IllegalStateException ex) {a=true;}
        assertTrue(a);
        assertEquals(fullIt.next(), Integer.valueOf(4));
        assertEquals(fullIt.next(), Integer.valueOf(5));
        assertContent(full, pole2);
    }


    @Test
    public void testRemove2() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {2,4};
        assertContent(full, pole1);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertEquals(fullIt.next(), Integer.valueOf(3));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(4));
        assertEquals(fullIt.next(), Integer.valueOf(5));
        fullIt.remove();
        assertContent(full, pole2);
    }

    @Test
    public void testRemove3() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {};
        assertContent(full, pole1);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(2));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(3));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(4));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(5));
        fullIt.remove();
        assertContent(full, pole2);
    }

    @Test
    public void testRemove4() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {3};
        assertContent(full, pole1);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertEquals(fullIt.previous(), Integer.valueOf(2));
        fullIt.remove();
        assertEquals(fullIt.previous(), Integer.valueOf(1));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(3));
        assertEquals(fullIt.next(), Integer.valueOf(4));
        fullIt.remove();
        assertEquals(fullIt.next(), Integer.valueOf(5));
        fullIt.remove();
        assertContent(full, pole2);
    }

    @Test
    public void testSet() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {50,70,80,90};
        assertContent(full, pole1);
        boolean a = false;
        try{
            fullIt.set(4);
        } catch (IllegalStateException ex) {a=true;}
        assertTrue(a);
        assertEquals(fullIt.next(), Integer.valueOf(1));
        fullIt.set(10);
        assertEquals(fullIt.next(), Integer.valueOf(2));
        fullIt.set(20);
        assertEquals(fullIt.previous(), Integer.valueOf(20));
        fullIt.set(30);
        assertEquals(fullIt.previous(), Integer.valueOf(10));
        fullIt.set(40);
        assertEquals(fullIt.next(), Integer.valueOf(40));
        fullIt.set(50);
        assertEquals(fullIt.next(), Integer.valueOf(30));
        fullIt.set(60);
        fullIt.remove();
        a = false;
        try{
            fullIt.set(4);
        } catch (IllegalStateException ex) {a=true;}
        assertTrue(a);
        assertEquals(fullIt.next(), Integer.valueOf(3));
        fullIt.set(70);
        assertEquals(fullIt.next(), Integer.valueOf(4));
        fullIt.set(80);
        assertEquals(fullIt.next(), Integer.valueOf(5));
        fullIt.set(90);
        assertFalse(fullIt.hasNext());
        assertContent(full, pole2);
    }

    @Test
    public void testAdd() {
        int [] pole1 = {1,2,3,4,5};
        int [] pole2 = {0,1,10,20,2,3,30,40,4,5,60};
        assertContent(full, pole1);
        assertFalse(fullIt.hasPrevious());
        fullIt.add(0);
        assertTrue(fullIt.hasNext());
        assertTrue(fullIt.hasPrevious());
        assertEquals(fullIt.previous(), Integer.valueOf(0));
        assertFalse(fullIt.hasPrevious());
        assertEquals(fullIt.next(), Integer.valueOf(0));
        assertEquals(fullIt.next(), Integer.valueOf(1));
        fullIt.add(10);
        fullIt.add(20);
        assertEquals(fullIt.next(), Integer.valueOf(2));
        assertEquals(fullIt.next(), Integer.valueOf(3));
        fullIt.add(30);
        fullIt.add(40);
        assertEquals(fullIt.previous(), Integer.valueOf(40));
        assertEquals(fullIt.previous(), Integer.valueOf(30));
        assertEquals(fullIt.next(), Integer.valueOf(30));
        assertEquals(fullIt.next(), Integer.valueOf(40));
        assertEquals(fullIt.next(), Integer.valueOf(4));
        assertEquals(fullIt.next(), Integer.valueOf(5));
        fullIt.add(60);
        assertEquals(fullIt.previous(), Integer.valueOf(60));
        assertEquals(fullIt.previous(), Integer.valueOf(5));
        assertTrue(fullIt.hasNext());
        assertEquals(fullIt.next(), Integer.valueOf(5));
        assertEquals(fullIt.next(), Integer.valueOf(60));
        assertFalse(fullIt.hasNext());
        assertContent(full, pole2);
    }

    @Test
    public void testAdd2() {
        int [] pole1 = {};
        int [] pole2 = {3,5,6,4,0,1,7};
        assertContent(empty, pole1);
        emptyIt.add(0);
        emptyIt.add(1);
        assertEquals(emptyIt.previous(), Integer.valueOf(1));
        assertEquals(emptyIt.previous(), Integer.valueOf(0));
        emptyIt.add(3);
        emptyIt.add(4);
        assertEquals(emptyIt.previous(), Integer.valueOf(4));
        assertEquals(emptyIt.previous(), Integer.valueOf(3));
        assertEquals(emptyIt.next(), Integer.valueOf(3));
        emptyIt.add(5);
        emptyIt.add(6);
        assertEquals(emptyIt.next(), Integer.valueOf(4));
        assertEquals(emptyIt.next(), Integer.valueOf(0));
        assertEquals(emptyIt.next(), Integer.valueOf(1));
        emptyIt.add(7);
        assertContent(empty, pole2);
    }

}