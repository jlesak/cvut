public class Main {
    public static void main(String[] args) {
        Homework1 hw1 = new Homework1();
        Homework1 hw2 = new Homework1();



        System.out.println("HW1");
        System.out.println(hw1.f());
        System.out.println(hw1.h());
        System.out.println(hw1.h());
        System.out.println(hw1.i());

        System.out.println("HW2");
        System.out.println(hw2.f());
        System.out.println(hw2.h());
        System.out.println(hw2.h());
        System.out.println(hw2.i());

        System.out.println("static");
        System.out.println(Homework1.g());
        System.out.println(Homework1.i());
        System.out.println(Homework1.i());
        System.out.println(Homework1.i());


    }
}
