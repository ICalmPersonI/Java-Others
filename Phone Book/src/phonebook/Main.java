package phonebook;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ClassLoader classLoader = A.class.getClassLoader();
        System.out.println(classLoader.getName());

    }
}
class A {
}
