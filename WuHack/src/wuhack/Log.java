/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Julian
 */
public class Log {
    
    private static StringProperty log = new SimpleStringProperty("");
    
    public static void log(String text) {
        log.set(log.get().concat(text+"\n"));
        System.out.println(text);
    }
    
    public static void clear() {
        log.set("");
    }
    
    public static String getLog() {
        return log.get();
    }
    
    public static StringProperty get() {
        return log;
    }
}
