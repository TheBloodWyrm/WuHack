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
    
    private String teacher;
    private String subject;
    private String classroom;
    private int hour;
    private String weekRange;
    private int Week;
    private WeekDay weekDay;
    private LocalDate day;

    public Lesson(String teacher, String subject, String classroom, int hour, String weekRange, int Week, WeekDay weekDay, LocalDate day) {
        this.teacher = teacher;
        this.subject = subject;
        this.classroom = classroom;
        this.hour = hour;
        this.weekRange = weekRange;
        this.Week = Week;
        this.weekDay = weekDay;
        this.day = day;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }  
}
