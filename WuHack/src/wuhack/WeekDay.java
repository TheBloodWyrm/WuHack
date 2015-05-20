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
public enum WeekDay {
    MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG;
    
  @Override    
  public String toString()
  {
    String s = "???";
    String myenum = this.toString();
    
    s = myenum.charAt(0) + myenum.substring(1, myenum.length()-1).toLowerCase();
    
    return s;
  }
}
