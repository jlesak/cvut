import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

interface OMOSetView {
    boolean contains(int element); // testuje na přítomnost prvku v množině

    int[] toArray(); //vrátí kopii prvků množiny v poli (na pořadí prvků nezáleží)

    OMOSetView copy(); //vrátí kopii množiny
}

// třída reprezentující obecnou množinu, definuje metody add/remove pro přidávání/odebírání prvků
class OMOSet implements OMOSetView {
    private HashSet<Integer> array = new HashSet<>();

    public void add(int element) {
        array.add(element);
    }

    public void remove(int element) {
        array.remove(element);
    }
    @Override
    public boolean contains(int element) {
        return array.contains(element);
    }

    @Override
    public int[] toArray() {
        int[] output = new int[array.size()];
        int i = 0;
        for (int value : array) {
            output[i++] = value;
        }
        return output;
    }
    @Override
    public OMOSetView copy() {
        OMOSet output = new OMOSet();
        for (int i:array){
            output.add(i);
        }
        return output;
    }

// metody rozhraní OMOSetView
}

// třída reprezentující sjednocení dvou množin: A sjednoceno B
class OMOSetUnion implements OMOSetView {

    private OMOSetView setA;
    private OMOSetView setB;
    OMOSetUnion(OMOSetView setA, OMOSetView setB) {
        this.setA = setA;
        this.setB = setB;
    }

    @Override
    public boolean contains(int element) {
        return (setA.contains(element) || setB.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSet output = new OMOSet();
        for (int elementA : setA.toArray()) {
            output.add(elementA);
        }
        for (int elementB : setB.toArray()) {
            if (!output.contains(elementB)) output.add(elementB);
        }
        return output.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSet output = new OMOSet();
        for (int i:this.toArray()){
            output.add(i);
        }
        return output;
    }

// metody rozhraní OMOSetView
}

// třída reprezentující průnik dvou množin: A průnik B
class OMOSetIntersection implements OMOSetView {

    private OMOSetView setA;
    private OMOSetView setB;
    OMOSetIntersection(OMOSetView setA, OMOSetView setB) {
        this.setA = setA;
        this.setB = setB;
    }

    @Override
    public boolean contains(int element) {
        return (setA.contains(element) && setB.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSet output = new OMOSet();
        for (int elementA: setA.toArray()) {
            if (setB.contains(elementA)) output.add(elementA);
        }
        return output.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSet output = new OMOSet();
        for (int i:this.toArray()){
            output.add(i);
        }
        return output;
    }

// metody rozhraní OMOSetView
}

// třída reprezentující A\B: doplněk množiny B vzhledem k množině A:  A\B = { x | x∈A ∧ x∉B }
class OMOSetComplement implements OMOSetView {
    private OMOSetView setA;
    private OMOSetView setB;
    OMOSetComplement(OMOSetView setA, OMOSetView setB) {
        this.setA = setA;
        this.setB = setB;
    }

    @Override
    public boolean contains(int element) {
        return setA.contains(element) && !setB.contains(element);
    }

    @Override
    public int[] toArray() {
        OMOSet array = new OMOSet();
        for (int elementA : setA.toArray()){
            if (!setB.contains(elementA)) array.add(elementA);
        }
        return array.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSet output = new OMOSet();
        for (int i:this.toArray()){
            output.add(i);
        }
        return output;
    }

// metody rozhraní OMOSetView
}

// třída reprezentující množinu sudých čísel
class OMOSetEven implements OMOSetView {

    OMOSetView setA;
    OMOSetEven(OMOSetView setA) {
        this.setA = setA;
    }

    @Override
    public boolean contains(int element) {

        return ((element % 2 == 0) && setA.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSet array = new OMOSet();
        for (int elementA : setA.toArray()){
            if (elementA % 2 == 0)array.add(elementA);
        }
        return array.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSet output = new OMOSet();
        for (int i:this.toArray()){
            output.add(i);
        }
        return output;
    }

// metody rozhraní OMOSetView
}