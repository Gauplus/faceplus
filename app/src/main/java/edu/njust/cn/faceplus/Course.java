package edu.njust.cn.faceplus;

/**
 * 课程类
 */
public class Course {
    private String courseName;
    private String courseTime;
    private String coursePlace;

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    public String getCoursePlace() {
        return coursePlace;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourseTime() {
        return courseTime;
    }
}
