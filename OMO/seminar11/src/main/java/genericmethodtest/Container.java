/*
package genericmethodtest;


import java.util.ArrayList;
import java.util.List;


public class Container<T> {
    private T value;

    public T get() {
        return value;
    }

    public void add(T value) {
        this.value = value;
    }

}

class Main {
    public static void main(String[] args) {
        */
/* part 1 *//*

        Container<Integer> integerBox = new Container<Integer>();
        Container<String> stringBox = new Container<String>();

        integerBox.add(new Integer(10));
        stringBox.add(new String("Hello World"));

        System.out.printf("Integer Value :%d\n\n", integerBox.get());
        System.out.printf("String Value :%s\n", stringBox.get());

        */
/* part 2 *//*

        // Create arrays of Integer, Double and Character
        Integer[] intArray = {1, 2, 3, 4, 5};
        Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
        Character[] charArray = {'T', 'E', 'S', 'T'};

        System.out.println("Array integerArray contains:");
        printArray(intArray);   // pass an Integer array

        System.out.println("\nArray doubleArray contains:");
        printArray(doubleArray);   // pass a Double array

        System.out.println("\nArray characterArray contains:");
        printArray(charArray);   // pass a Character array
    }

    private static <E> void printArray(E[] array) {
        for (E element : array){
            System.out.println(element + " ");
        }
    }
}
*/
