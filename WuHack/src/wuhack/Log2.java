/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Paul
 */
public class Log2
{

  ScheduleModel model = ScheduleModel.getInstance();
  private Lesson[][][] timetable = new Lesson[32][5][12];
  private int lesson;
  private int weekday;
  
  StringBuilder text = new StringBuilder();
  
  LocalTime nowTime = LocalTime.now();
  LocalDate nowDate = LocalDate.now();
  
  LocalTime firstLesson = LocalTime.of(8, 0);
  LocalTime secondLesson = LocalTime.of(8, 50);
  LocalTime thirdLesson = LocalTime.of(9, 40);
  LocalTime longBreak = LocalTime.of(10, 30);
  LocalTime fourthLesson = LocalTime.of(10, 45);
  LocalTime fifthLesson = LocalTime.of(11, 35);
  LocalTime sixthLesson = LocalTime.of(12, 25);
  LocalTime seventhLesson = LocalTime.of(13, 15);
  LocalTime eigthLesson = LocalTime.of(14, 00);
  LocalTime shortBreak = LocalTime.of(14, 45);
  LocalTime ninthLesson = LocalTime.of(14, 50);
  LocalTime tenthLesson = LocalTime.of(15, 35);
  LocalTime eleventhLesson = LocalTime.of(16, 20);
  LocalTime twelthethLesson = LocalTime.of(17, 05);

  public void checkTime()
  {

    if (nowTime.isAfter(firstLesson) && nowTime.isBefore(secondLesson))
    {
      System.out.println("First Lesson");
      lesson = 1;
    }
    else if (nowTime.isAfter(secondLesson) && nowTime.isBefore(thirdLesson))
    {
      System.out.println("Second Lesson");
      lesson = 2;
    }
    else if (nowTime.isAfter(thirdLesson) && nowTime.isBefore(longBreak))
    {
      System.out.println("Third Lesson");
      lesson = 3;
    }
    else if (nowTime.isAfter(longBreak) && nowTime.isBefore(fourthLesson))
    {
      System.out.println("Long Break");
      lesson = 0;
    }
    else if (nowTime.isAfter(fourthLesson) && nowTime.isBefore(fifthLesson))
    {
      System.out.println("fourth Lesson");
      lesson = 4;
    }
    else if (nowTime.isAfter(fifthLesson) && nowTime.isBefore(sixthLesson))
    {
      System.out.println("fith Lesson");
      lesson = 5;
    }
    else if (nowTime.isAfter(sixthLesson) && nowTime.isBefore(seventhLesson))
    {
      System.out.println("sixth Lesson");
      lesson = 6;
    }
    else if (nowTime.isAfter(seventhLesson) && nowTime.isBefore(eigthLesson))
    {
      System.out.println("Seventh Lesson");
      lesson = 7;
    }
    else if (nowTime.isAfter(eigthLesson) && nowTime.isBefore(shortBreak))
    {
      System.out.println("Eigth Lesson");
      lesson = 8;
    }
    else if (nowTime.isAfter(shortBreak) && nowTime.isBefore(ninthLesson))
    {
      System.out.println("Short Break");
      lesson = 0;
    }
    else if (nowTime.isAfter(ninthLesson) && nowTime.isBefore(tenthLesson))
    {
      System.out.println("Ninth Lesson");
      lesson = 9;
    }
    else if (nowTime.isAfter(tenthLesson) && nowTime.isBefore(eleventhLesson))
    {
      System.out.println("Tenth Lesson");
      lesson = 10;
    }
    else if (nowTime.isAfter(eleventhLesson) && nowTime.isBefore(twelthethLesson))
    {
      System.out.println("Eleventh Lesson");
      lesson = 11;
    }
    else if (nowTime.isAfter(twelthethLesson) && nowTime.isBefore(twelthethLesson.plusMinutes(45)))
    {
      System.out.println("Twelveth Lesson");
      lesson = 12;
    }
    else
    {
      System.out.println("Not in school Anymore");
      lesson = 13;
    }
  }
  
  public void checkWeekDay(){
    switch(nowDate.toString()){
      case "MONDAY": weekday = 1; break;
      case "TUESDAY": weekday = 2; break;
      case "WEDNESDAY": weekday = 3; break;
      case "THURSDAY": weekday = 4; break;
      case "FRIDAY": weekday =  5; break;
      case "SATURDAY": weekday = 6; break;
      case "SUNDAY": weekday = 7; break;
    }
  }
  
  public String checkWhichLesson(String kuerzel){
    checkTime();
    checkWeekDay();
    
    text.append("Der Lehrer mit dem Kürzel " + kuerzel + " ist");
    
    if(weekday == 6 || weekday == 7){
      text.append(" nicht in der Schule, da es Wochenende ist.");
    }
    
    
    return text.toString();
  }

}
