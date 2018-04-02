package com.fih.idx.rxjavademo;

/**
 * Created by derik on 18-3-6.
 */

public class Course {
    private String name;
    private float score;

    public Course(String name, float score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
