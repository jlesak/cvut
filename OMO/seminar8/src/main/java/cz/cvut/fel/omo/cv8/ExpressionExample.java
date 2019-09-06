package cz.cvut.fel.omo.cv8;

import com.google.common.collect.ImmutableList;

public class ExpressionExample {

    public static void main(String[] args) {
        Context c = new Context();
        //storing variable x to context
        //c.put("x", ImmutableList.<Integer>of(11, 12, 13));
        
        PrintListExpressionVisitor vp = new PrintListExpressionVisitor();

        ListExpression l1 = IntList.of(ImmutableList.of(1, 2, 3, 4, 5));
        ListExpression l2 = IntList.of(ImmutableList.of(5, 6, 7));
        ListExpression l3 = IntList.of(ImmutableList.of(3, 2, 1, 2, 2, 1));
        //ListExpression l4 = new VarList("x");

        //ListExpression e1 = new Concatenate(l1, l2);
        //e1.accept(vp);
        //System.out.println();
        //System.out.println(e1.evaluate(c));

        //System.out.println();
        //ListExpression e2 = new Unique(l3);
        //e2.accept(vp);
        //System.out.println();
        //System.out.println(e2.evaluate(c));

        //ListExpression e3 = new Remove(l3, 2);
        //ListExpression e4 = new Concatenate(e1, e3);
        //SimplifyListExpressionVisitor vs = new SimplifyListExpressionVisitor();
        //System.out.println("\noriginal: ");
        //e4.accept(vp);
        //e4.accept(vs);
        //ListExpression  simplified = vs.getValue();
        //System.out.println("\nsimplified: ");
        //simplified.accept(vp);
    }
}
