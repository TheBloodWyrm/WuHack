/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.time.LocalDate;
import java.util.Calendar;

/**
 *
 * @author Julius
 */
public class Tester
{
  public static void main(String[] args)
  {
    int[][] ints = new int[1][2];
    System.out.println(ints.length);
    
    int wl = LocalDate.now().getDayOfYear()/7+1;
    int wc = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    System.out.println(wl+" "+wc);
  }
}
