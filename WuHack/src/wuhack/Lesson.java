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
    private String klasse;
    private String[] classrooms;
    private int hour;
    private int Week;
    private WeekDay weekDay;

    public Lesson(Kürzel[] teachers, String subject, String klasse, String[] classrooms, int hour, int Week, WeekDay weekDay) {
        this.teachers = teachers;
        this.subject = subject;
        this.klasse = klasse;
        this.classrooms = classrooms;
        this.hour = hour;
        this.Week = Week;
        this.weekDay = weekDay;
    }

  public String getKlasse()
  {
    return klasse;
  }

  public void setKlasse(String klasse)
  {
    this.klasse = klasse;
  }

    public Kürzel[] getTeachers() {
        return teachers;
    }

    public void setTeachers(Kürzel[] teacher) {
        this.teachers = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(String[] classroom) {
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
