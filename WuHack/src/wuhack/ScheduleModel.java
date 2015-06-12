/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFontElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author Julian
 */
public class ScheduleModel {

    private List<String> classes = new ArrayList<>();
    private Lesson[][][] timetable = new Lesson[32][5][12];
    private List<String> kuerzel = new ArrayList<>(90);

    private static ScheduleModel instance = new ScheduleModel();
    
    private ScheduleModel() {
        
    }
    
    public static ScheduleModel getInstance() {
        return instance;
    }
    
    public Lesson[][] analyzeDoc(Document d, int calweek, int index) {
        Lesson[][] schedule = new Lesson[5][12];
        
        if(d == null) {
            System.out.println("No Document");
            return null;
        }
        
        //WebBrowserTest.printAllNodes(d);
        
        HTMLFontElement f = (HTMLFontElement) d.getElementsByTagName("font").item(1);

        //System.out.println("FontText: " + f.getTextContent());
        String klasse = f.getTextContent();
        
        if(!this.classes.contains(klasse)) {
            this.classes.add(klasse);
        }

        NodeList tables = d.getElementsByTagName("table");

        HTMLTableElement table = (HTMLTableElement) tables.item(0);

        HTMLCollection rows = table.getRows();

        for (int i = 1; i < rows.getLength(); i = i + 2) {
            int doneCells = 0;

            HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);

            HTMLCollection cells = row.getCells();

            for (int j = 1; j < cells.getLength(); j++) {
                if (schedule[j - 1 + doneCells][((i - 1) / 2)] == null) {
                    //System.out.println("Lesson:");
                    
                    LinkedList<String> teachers = new LinkedList<>();
                    String subject = "???";
                    LinkedList<String> classrooms = new LinkedList<>();
                    int hour;
                    WeekDay weekday;
                    boolean isLesson = true;

                    weekday = WeekDay.values()[j - 1];
                    hour = (i - 1) / 2 + 1;

                    HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);

                    HTMLTableElement inTable = (HTMLTableElement) cell.getChildNodes().item(0);
                    HTMLCollection inRows = inTable.getRows();

                    for (int k = 0; k < inRows.getLength(); k++) {
                        HTMLTableRowElement inRow = (HTMLTableRowElement) inRows.item(k);
                        HTMLCollection inCells = inRow.getCells();

                        if (k == 0) {
                            if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 5) {
                                // TODO!! (derweil wird nur gelöscht)
                                isLesson = false;

                                //System.out.println("No lesson: " + inRow.getTextContent());
                            } else {
                                subject = removeSigns(inCells.item(0).getTextContent().trim());
                                //System.out.println(" " + subject);
                            }
                        } else {
                            if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 3) {
                                // TODO!! (derweil wird nur gelöscht)
                                isLesson = false;

                                //System.out.println("No lesson: " + inRow.getTextContent());
                            } else {
                                String classroom;

                                if (inCells.getLength() == 1) {
                                    classroom = "???";
                                } else {
                                    classroom = inCells.item(1).getTextContent().trim();
                                }

                                //System.out.println(" " + removeSigns(inCells.item(0).getTextContent().trim()) + " - - - " + classroom);
                                String kuerzel = removeSigns(inCells.item(0).getTextContent().trim()); //.replace("---", "NIEMAND");
                                //teachers.add(Kürzel.valueOf(kuerzel));
                                teachers.add(kuerzel);
                                if (!this.kuerzel.contains(kuerzel)) {
                                    this.kuerzel.add(kuerzel);
                                }

                                classrooms.add(classroom);
                            }
                        }
                    }

                    for (int k = 0; k < (Integer.parseInt(cell.getAttribute("rowspan")) / 2); k++) {
                        //System.out.println("rowspan: " + (Integer.parseInt(cell.getAttribute("rowspan")) / 2));
                        if (isLesson && schedule[j - 1 + doneCells][((i - 1) / 2) + k] == null) {
                            //Lesson l = new Lesson(convertTeachers(teachers), subject, klasse, convertClassrooms(classrooms), hour + k, calweek, weekday);
                            Lesson l = new Lesson(teachers.toArray(new String[0]), subject, klasse, classrooms.toArray(new String[0]), hour + k, calweek, weekday);
                            schedule[j - 1 + doneCells][((i - 1) / 2) + k] = l;
                        }
                    }
                } else {
                    //System.out.println("longer lesson!");
                    doneCells++;
                    j--;
                }
            }

        }

        return schedule;
    }

    private String[] convertClassrooms(List<String> classrooms) {
        String[] array = new String[classrooms.size()];

        for (int i = 0; i < classrooms.size(); i++) {
            array[i] = classrooms.get(i);
        }

        return array;
    }

    public Lesson[][] getTeacherLessons(String ku) {
      //System.out.println("Kürzel: " + ku);
        Lesson[][] table = new Lesson[5][12];
        

        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                for (int k = 0; k < timetable[i][j].length; k++) {
                    Lesson l = timetable[i][j][k];

                    if (l != null && contains(l.getTeachers(), ku)) {
//                        int day = l.getWeekDay().ordinal();
//                        int hour = l.getHour() - 1;
                        //System.out.println("found: " + j + " " + k);
                        table[j][k] = l;
                    }
                    else
                    {
//                      if(l != null)
//                        table[j][k] = new Lesson(new String[] {"---"}, "---", "---", new String[] {"---"}, k, 0, WeekDay.values()[j]);
                    }
                }
            }
        }

        WebBrowserTest.printLessons(table);
        
        return table;
    }
    
    public Lesson[][] getClassLessons(String cl) {
        Lesson[][] table = new Lesson[5][12];
        
        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                for (int k = 0; k < timetable[i][j].length; k++) {
                    Lesson l = timetable[i][j][k];

                    if (l != null && l.getKlasse().equals(cl)) {
                        int day = l.getWeekDay().ordinal();
                        int hour = l.getHour() - 1;

                        table[day][hour] = l;
                    }
                }
            }
        }
        
        WebBrowserTest.printLessons(table);
        
        return table;
    }
    
    public Lesson[][] getClassroomsLessons(String cl) {
        Lesson[][] table = new Lesson[5][12];
        
        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                for (int k = 0; k < timetable[i][j].length; k++) {
                    Lesson l = timetable[i][j][k];

                    if (l != null && contains(l.getClassrooms(), cl)) {
                        int day = l.getWeekDay().ordinal();
                        int hour = l.getHour() - 1;

                        table[day][hour] = l;
                    }
                }
            }
        }
        
        WebBrowserTest.printLessons(table);
        
        return table;
    }

    public void loadAllLessons(WebEngine we, int week) {
        classes.clear();
        kuerzel.clear();
        
        we.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

            int counter = 1;
            
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
                
                if(newValue == State.SUCCEEDED) {
                    timetable[counter-1] = analyzeDoc(we.getDocument(), week, counter);

                    if(counter < timetable.length-1) {
                        counter++;
                        we.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + (week) + "/c/c" + String.format("%05d", counter) + ".htm");
                    } else {
                        WebBrowserTest.printAllLessons(timetable);
                        we.getLoadWorker().stateProperty().removeListener(this);
                    }
                }
            }
        });
        
        we.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + (week) + "/c/c" + String.format("%05d", 1) + ".htm");
        
//        for (i = 1; i < timetable.length; i++) {
//            we.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + (week) + "/c/c" + String.format("%05d", i) + ".htm");

//            while (we.getLoadWorker().getState() != State.SUCCEEDED) {
//                System.out.println(we.getLoadWorker().getState() + " " + i);
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                    System.out.println("");
//                }
//            }
//
//            timetable[i] = analyzeDoc(we.getDocument(), week, i);
//        }
    }

    private boolean contains(String[] a, String k) {
        boolean b = false;

        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(k)) {
                b = true;
            }
        }

        return b;
    }

    private String removeSigns(String s) {
        String ret = "";
        char[] ignorableChars
                = {
                    '.', ',', '\'', '\"', '!', '?', '§', '$',
                    '%', '&', '/', '(', ')', '=', '\\', ']',
                    '[', '{', '}', '#', '+', '*', '~'
                };
        for (int i : s.chars().toArray()) {
            boolean fits = true;
            char c = (char) i;

            for (char ignorableChar : ignorableChars) {
                if (c == ignorableChar) {
                    fits = false;
                }
            }

            if (fits) {
                ret += c;
            }
        }

        return ret;

    }

  public List<String> getClasses()
  {
    return classes;
  }

  public List<String> getKuerzel()
  {
    return kuerzel;
  }

  public Lesson[][][] getTimetable()
  {
    return timetable;
  } 
    
}
