package cz.cvut.fel.omo.cv8;

public class SimplifyListExpressionVisitor implements ListExpressionVisitor {

    private ListExpression value;

    public ListExpression getValue() {
        return value;
    }

    @Override
    public void visitIntList(IntList v) {
        value = v;
    }
    
}
