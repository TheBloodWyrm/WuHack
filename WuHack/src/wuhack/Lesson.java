/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wuhack;

import java.time.LocalDate;

/**
 *
 * @author Julian
 */
public class Lesson {
    
    private Kürzel[] teachers;
    private String subject;
    private String[] classrooms;
    private int hour;
    private int Week;
    private WeekDay weekDay;

    public Lesson(Kürzel[] teachers, String subject, String[] classrooms, int hour, int Week, WeekDay weekDay) {
        this.teachers = teachers;
        this.subject = subject;
        this.classrooms = classrooms;
        this.hour = hour;
        this.Week = Week;
        this.weekDay = weekDay;
    }

    public Kürzel[] getTeacher() {
        return teachers;
    }

    public void setTeacher(Kürzel[] teacher) {
        this.teachers = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getClassroom() {
        return classrooms;
    }

    public void setClassroom(String[] classroom) {
        this.classrooms = classroom;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getWeek() {
        return Week;
    }

    public void setWeek(int Week) {
        this.Week = Week;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }
}
