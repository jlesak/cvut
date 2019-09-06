package list;

import java.util.*;


/**
* todo here
* změňte dle potřeby i hlavičku
 */
class MyListItem<T> {
//todo here
}

/**
* todo here
* změňte dle potřeby i hlavičku
 */
public class MyList<T> implements Iterable {

    private List<T> list = new ArrayList<T>{};

    /**
     * Vlozi prvek na konec seznamu
     * @param value vkladany prvek
     */
    public void addLast(T value) {
//todo here
    }

    /**
     * Vlozi prvek na zacatek seznamu
     * @param value vkladany prvek
     */
    public void addFirst(T value) {
//todo here
    }

    /**
     * Vytvori list-iterator pro pruchod seznamem
     * NEMENTE
     * @return instanci list-iteratoru
     */
    public ListIterator<T> listIterator() {
        return new MyIterator<T>(this);
    }

    /**
     * Vytvori iterator pro pruchod seznamem
     * NEMENTE
     * @return instanci iteratoru
     * @see #listIterator()
     */
    public Iterator<T> iterator(){
        return listIterator();
    }
}


/**
 * Iterator pro pruchod seznamem
 * @param <T> typ vkladaneho prvku
 */
class MyIterator<T> implements ListIterator<T> { // v hlavičce je hint :)
    //todo here

    /**
     * @param list odkaz na iterovany seznam
     */
    MyIterator(MyList<T> list){
        //todo here
    }

    /**
     * @return true, pokud je za pozici iteratoru dalsi prvek
     */
    public boolean hasNext() {
        //todo here
    }

    /**
     * Vrati prvek za pozici iteratoru a posune o jednu pozici dopredu
     * @return dalsi prvek za pozici iteratoru
     * @throws NoSuchElementException pokud takovy prvek neexistuje
     */
    public T next() {
        //todo here
    }

    /**
     * @return True, pokud je pred pozici iteratoru prvek
     */
    public boolean hasPrevious() {
        //todo here
    }

    /**
     * Vrati prvek pred iteratorem a posune pozici iteratoru o jednu zpet.
     * @return vraci predchozi prvek pred iteratorem
     * @throws NoSuchElementException pokud takovy prvek neexistuje
     */
    public T previous() {
        //todo here
    }

    /**
     * Vraci pozici prvku, ktery by byl precteny dalsim volanim next.
     * Prvni prvek je 0, posledni n-1. Pokud je iterator za poslednim prvkem
     * (to zahrnuje i prazdny seznam), vrati n (prazdny -> n=0), kde n je
     * pocet prvku seznamu.
     * @see #next()
     * @return pozici prvku, ktery ma byt precten pomoci next
     */
    public int nextIndex() {
        //todo here
    }

    /**
     * Vraci pozici prvku, ktery by byl precten pomoci dalsiho volani previous.
     * Prvni prvek je 0, posledni n-1. Pokud je iterator pred prvni prvekem
     * (to zahrnuje i prazdny seznam), vrati -1. n je pocet prvku seznamu.
     * @see #previous()
     * @return pozici prvku, ktery ma byt precten pomoci previous
     */
    public int previousIndex() {
        //todo here
    }

    /**
     * Vyjme ze seznamu prvek, ktery byl naposled precten operaci next nebo
     * previous.
     * @see #next()
     * @see #previous()
     * @throws IllegalStateException v pripade, ze nebylo volano next ani previous,
     * nebo byla po nich volana metoda remove nebo add.
     */
    public void remove() {
        //todo here
    }

    /**
     * Nastavi novou hodnotu prvku, ktery byl naposled precten operaci next nebo
     * previous.
     * @param e nova hodnota prvku
     * @see #next()
     * @see #previous()
     * @throws IllegalStateException v pripade, ze nebylo volano next ani previous,
     * nebo byla po nich volana metoda remove nebo add.
     */
    public void set(T e) {
        //todo here
    }

    /**
     * Na misto iteratoru se vlozi novy prvek. Iterator bude ukazovat ZA vlozeny
     * prvek. Tudiz se zvetsi indexy o 1, pripadne volani previous vrati prave
     * vlozeny prvek, hodnotu volani next to neovlivni.
     * @param e hodnota vkladaneho prvku
     */
    public void add(T e) {
        //todo here
    }
}

