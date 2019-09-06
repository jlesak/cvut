package cz.cvut.fel.omo.cv8;

import com.google.common.collect.ImmutableList;

public class IntList implements ListExpression {
    protected final ImmutableList<Integer> list;

    private IntList(ImmutableList<Integer> list) {
        this.list = list;
    }

    public static IntList empty() {
        return new IntList(ImmutableList.<Integer>of());
    }

    public static IntList of(int value) {
        return new IntList(ImmutableList.of(value));
    }

    public static IntList of(ImmutableList<Integer> list) {
        return new IntList(list);
    }

    public int size() {
        return list.size();
    }

    @Override
    public ImmutableList<Integer> evaluate(Context c) {
        return list;
    }

    @Override
    public void accept(ListExpressionVisitor v) {
        v.visitIntList(this);
    }
}
