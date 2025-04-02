package hello.core;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class test {
    private final int a;
    private final int b;

    public static void main(String[] args) {
        test t1 = new test(1, 2);


        System.out.println(t1.a);
        System.out.println(t1.b);
    }
}
