package com.derik.rxjavademo.data;

import java.util.ArrayList;

/**
 * Created by derik on 18-3-6.
 * Email: weilai0314@163.com
 */

public class Student implements Cloneable {
    private String name;
    private int age;
    private String gender;
    private ArrayList<Course> courses;

    private Student(Builder builder) {
        name = builder.name;
        age = builder.age;
        gender = builder.gender;
        courses = builder.courses;
    }

    public static class Builder {
        private String name;
        private int age;
        private String gender;
        private ArrayList<Course> courses;

        public Student build() {
            return new Student(this);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setCourses(ArrayList<Course> courses) {
            this.courses = (ArrayList<Course>) courses.clone();
            return this;
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("name:" + name + " ,age:" + age + " ,gender:" + gender + " ,");
        for (Course course : courses) {
            str.append(course.getName());
            str.append(":");
            str.append(course.getScore());
        }
        return str.toString();
    }

    @Override
    public Student clone() {
        Student student = null;
        try {
            student = (Student) super.clone();
            student.name = this.name;
            student.age = this.age;
            student.gender = this.gender;
            student.courses = (ArrayList<Course>)this.courses.clone();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return student;
    }
}
