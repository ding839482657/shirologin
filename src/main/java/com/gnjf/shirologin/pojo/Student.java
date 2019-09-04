package com.gnjf.shirologin.pojo;

public class Student {
    private int age;
    private boolean sex;
    private int weight;

    public Student(int age, boolean sex, int weight) {
        this.age = age;
        this.sex = sex;
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
