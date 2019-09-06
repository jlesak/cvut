package cz.cvut.fel.omo.cv8;

import com.google.common.collect.ImmutableList;

public interface ListExpression {
    ImmutableList<Integer> evaluate(Context c);
    void accept(ListExpressionVisitor v);
}
