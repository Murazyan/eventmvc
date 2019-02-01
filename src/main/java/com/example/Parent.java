package com.example;

public class Parent  {
    private   int a;

    public Parent(int a) {
        this.a = a;
    }
}
class B extends Parent{



    public B() {
        super(10);
    }
}
