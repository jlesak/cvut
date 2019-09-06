package cz.cvut.fel.omo.cv8;

public class PrintListExpressionVisitor implements ListExpressionVisitor {

    @Override
    public void visitIntList(IntList v) {
        System.out.print(v.list);
    }
    
}