/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wuhack;

/**
 *
 * @author Julian
 */
public class Lesson {
    
    //private Kürzel[] teachers;
    private String[] teachers;
    private String subject;
    private String klasse;
    private String[] classrooms;
    private int Week;
    
    public Lesson(String[] teachers, String subject, String klasse, String[] classrooms, int Week) {
        this.teachers = teachers;
        this.subject = subject;
        this.klasse = klasse;
        this.classrooms = classrooms;
        this.Week = Week;
    }

  public String getKlasse()
  {
    return klasse;
  }

  public void setKlasse(String klasse)
  {
    this.klasse = klasse;
  }

    public String[] getTeachers() {
        return teachers;
    }

    public void setTeachers(String[] teacher) {
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

    public int getWeek() {
        return Week;
    }

    public void setWeek(int Week) {
        this.Week = Week;
    }
}
