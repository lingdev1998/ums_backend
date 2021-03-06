package com.linkdoan.backend.gaScheduleModel;

import java.util.ArrayList;

public class StudentsGroup {

    private int id;
    private String name;
    private int numberOfStudents;
    private ArrayList<CourseClass> courseClasses;

    public StudentsGroup() {
        name = "";
        courseClasses = new ArrayList();
    }

    public StudentsGroup(int id, String name, int numberOfStudents) {
        this.id = id;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
        courseClasses = new ArrayList();
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public ArrayList<CourseClass> getCourseClasses() {
        return courseClasses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public void setCourseClasses(ArrayList<CourseClass> courseClasses) {
        this.courseClasses = courseClasses;
    }


    public void addCourseClass(CourseClass courseClass) {
        courseClasses.add(courseClass);
    }

}
