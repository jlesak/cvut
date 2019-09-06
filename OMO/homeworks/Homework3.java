import java.util.ArrayList;
import java.util.List;

//interface CustomIterator {
//    boolean hasNext();
//    int next();
//}

class Node {
    final int contents;
    final Node left, right;
    Node parent;

    Node(int contents, Node left, Node right) {
        this.contents = contents;
        this.left = left;
        if (left != null) left.parent = this;
        this.right = right;
        if (right != null) right.parent = this;
    }

    CustomIterator preorderIterator() { return new PreorderIterator(this);}
    CustomIterator inorderIterator() {return new InorderIterator(this);}
    CustomIterator postorderIterator() { return new PostorderIterator(this); }
}
class  PreorderIterator implements CustomIterator{
    private Node node;
    private Node previous;

    public PreorderIterator(Node node) {
        this.node = node;
    }

    public boolean hasNext() {
       return checkHasNext(node, previous);
    }

    public int next() {
        return step(node);
    }
    private boolean checkHasNext(Node node, Node previous){
        if (node.parent == null && (node.right == previous || node.right == null)) return false;

        else if ((node.left == previous && node.right == null) || (node.right == previous)){
            return checkHasNext(node.parent, node);
        }
        return true;
    }
    private int step(Node node){

        if (node == null) return 0;

        if (node.left == previous && previous != null){
            previous = node;
            if (node.right != null){
                return step(node.right);
            }
            return step(node.parent);
        }
        else if(node.right == previous && previous != null){
            previous = node;
            return step(node.parent);
        }

        int output = node.contents;
        previous = node;

        if (node.left != null && node.left!= previous){
            this.node = node.left;
        }

        else if(node.right != null && node.right!= previous){
            this.node = node.right;
        }

        else {
            previous = node;
            this.node = node.parent;
        }

        return output;
    }
}

class InorderIterator implements CustomIterator{
    private Node node;
    private Node previous;

    //projde strom doleva. Pokud se z leva vraci, vypise hodnotu
    public InorderIterator(Node node) {
        this.node = node;
    }

    public boolean hasNext() {
        return checkHasNext(node, previous);
    }

    private boolean checkHasNext(Node node, Node previous){
        if (node.parent == null && previous == null) {
            return true;
        }
        if(previous == node) return false;
        if (node.parent == null && (node.right == previous || node.right == null)) {
            return node.right == null;
        }

        else if ((node.right == previous && node.right == null) || (node.right == previous)){
            return checkHasNext(node.parent, node);
        }
        return true;
    }

    public int next() {
        return step(this.node);
    }

    private int step(Node node){

        if (node.left != null && node.parent == previous){
            previous = node;
            return step(node.left);
        }

        else if(previous == node.right){
            previous = node;

            return step(node.parent);
        }

        else if(node.left == null){
            /*if(node == previous.left){
                this.node = node.parent;
            }
            else*/ if(node.right != null) {
                this.node = node.right;
            }
            else this.node = node.parent;
            previous = node;
            return node.contents;
        }

        else {
            previous = node;
            if(node.right != null){
                this.node = node.right;
            }
            else if (node.parent != null) {
                this.node = node.parent;
            }
            return node.contents;
        }
    }
}

class PostorderIterator implements CustomIterator{
    private Node node;
    private Node previous;

    public PostorderIterator(Node node) {
        this.node = node;
    }

    public boolean hasNext() {
        return (node != null);//node.parent != null || previous == null || (previous != node.right && node.right != null);
    }

    public int next() {
        return step(node);
    }

    private int step(Node node) {

        if (node.left != null && previous == node.parent){
            previous = node;
            return step(node.left);
        }

        else if (node.right != null && previous == node.parent){
            previous = node;
            return step(node.right);
        }

        else if (previous == node.left){
            if (node.right != null){
                previous = node;
                return step(node.right);
            }
            else {
                previous = node;
                this.node = node.parent;
                return node.contents;
            }
        }

        /*else if (previous == node.right){
            previous = node;
            this.node = node.parent;
            return node.contents;
        }*/

        else {
            previous = node;
            this.node = node.parent;
            return node.contents;
        }
    }
}