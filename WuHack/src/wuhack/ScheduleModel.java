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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFontElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author Julian
 */
public class ScheduleModel {

    private Lesson[][][] timetable = new Lesson[32][5][12];
    private List<String> kuerzel = new ArrayList<>(90);
    private List<String> classes = new ArrayList<>(30);
    private List<String> classrooms = new ArrayList<>(50);

    private static final ScheduleModel instance = new ScheduleModel();

    private ScheduleModel() {

    }

    public static ScheduleModel getInstance() {
        return instance;
    }

    public Lesson[][] analyzeDoc(Document d, int calweek, int index) {
        Lesson[][] schedule = new Lesson[5][12];
        int[] doneCells = new int[12];

        for (int i = 0; i < doneCells.length; i++) {
            doneCells[i] = 0;
        }

        if (d == null) {
            Log.log("Not a document");
            return null;
        }

        HTMLFontElement f = (HTMLFontElement) d.getElementsByTagName("font").item(1);

        String klasse = removeLineBreak(f.getTextContent());

        if (!this.classes.contains(klasse)) {
            this.classes.add(klasse);
        }

        NodeList tables = d.getElementsByTagName("table");

        HTMLTableElement table = (HTMLTableElement) tables.item(0);

        HTMLCollection rows = table.getRows();

        for (int i = 1; i < rows.getLength(); i = i + 2) {
            HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);

            HTMLCollection cells = row.getCells();

            for (int j = 1; j < cells.getLength(); j++) {
                if (schedule[j - 1 + doneCells[(i - 1) / 2]][((i - 1) / 2)] == null) {

                    LinkedList<String> teachers = new LinkedList<>();
                    String subject = "???";
                    LinkedList<String> classrooms = new LinkedList<>();
                    boolean isLesson = true;

                    HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);

                    HTMLTableElement inTable = (HTMLTableElement) cell.getChildNodes().item(0);
                    HTMLCollection inRows = inTable.getRows();

                    for (int k = 0; k < inRows.getLength(); k++) {
                        HTMLTableRowElement inRow = (HTMLTableRowElement) inRows.item(k);
                        HTMLCollection inCells = inRow.getCells();

                        if (k == 0) {
                            if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 5) {
                                // (todo, derweil wird nur gelöscht)
                                isLesson = false;
                            } else {
                                subject = removeSigns(inCells.item(0).getTextContent().trim());
                            }

                        } else {
                            if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 3) {
                                // (todo, derweil wird nur gelöscht)
                                isLesson = false;
                            } else {
                                String classroom;

                                if (inCells.getLength() == 1) {
                                    classroom = "???";
                                } else {
                                    classroom = removeSigns(inCells.item(1).getTextContent().trim());
                                }

                                String kuerzel = removeSigns(inCells.item(0).getTextContent().trim());
                                teachers.add(kuerzel);

                                if (!this.kuerzel.contains(kuerzel) && !kuerzel.equals("---")) {
                                    this.kuerzel.add(kuerzel);
                                }

                                classrooms.add(classroom);

                                if (!this.classrooms.contains(classroom) && !classroom.equals("???") && !classroom.equals("---")) {
                                    this.classrooms.add(classroom);
                                }
                            }
                        }
                    }

                    for (int k = 0; k < (Integer.parseInt(cell.getAttribute("rowspan")) / 2); k++) {
                        int spalte = j - 1 + doneCells[(i - 1) / 2];
                        int zeile = (i - 1) / 2 + k;

            //System.out.println("Spalte: " + spalte + ", Zeile: " + zeile);
                        if (isLesson && schedule[spalte][zeile] == null) {
                            Lesson l = new Lesson(teachers.toArray(new String[0]), subject, klasse, classrooms.toArray(new String[0]), calweek);
                            schedule[spalte][zeile] = l;

                        }
                    }
                } else {
                    //System.out.println("longer lesson!");
                    doneCells[(i - 1) / 2]++;
                    j--;
                }
            }

        }

        return schedule;
    }

    public Integer[] readWeeks(Document d) {
        List<Integer> weeks = new ArrayList<>(3);
        NodeList nl = d.getElementsByTagName("a");
        
        for (int i = 0; i < nl.getLength(); i++) {
            HTMLAnchorElement l = (HTMLAnchorElement) nl.item(i);
            String href = l.getHref();
            String n = href.substring(href.length()-3, href.length()-1);
            
            if (isNumeric(n)) {
                weeks.add(Integer.valueOf(n));
            }
        }
        
        return weeks.toArray(new Integer[0]);
    }
    
    private boolean isNumeric(String str) {
        boolean b = true;
        
        for (char c : str.toCharArray()) {
            
            if(!Character.isDigit(c)) {
                b = false;
            }
        }
        
        return b;
    }
    
    private String[] convertClassrooms(List<String> classrooms) {
        String[] array = new String[classrooms.size()];

        for (int i = 0; i < classrooms.size(); i++) {
            array[i] = classrooms.get(i);
        }

        return array;
    }

    public Lesson[][] getTeacherLessons(String ku) {
        Log.log("Retrieve lessons of selected teacher...");
        Lesson[][] table = new Lesson[5][12];

        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                for (int k = 0; k < timetable[i][j].length; k++) {
                    Lesson l = timetable[i][j][k];

                    if (l != null && contains(l.getTeachers(), ku)) {
                        table[j][k] = l;
                    }
                }
            }
        }

        return table;
    }

    public Lesson[][] getClassLessons(String cl) {
        Log.log("Retrieve lessons of selected class...");
        Lesson[][] table = new Lesson[5][12];

        boolean classfound = false;
        int i = 0;
        while (!classfound && i < timetable.length) {
            int j = 0;
            int k = 0;
            boolean found = false;
            while (!found && j < timetable[0].length) {
                while (!found && k < timetable[0][0].length) {
                    if (timetable[i][j][k] != null) {
                        found = true;

                        if (timetable[i][j][k].getKlasse().equals(cl)) {
                            table = timetable[i];
                            classfound = true;
                        }
                    }

                    k++;
                }
                j++;
            }
            i++;
        }

        return table;
    }

    public Lesson[][] getClassroomLessons(String cl) {
        Log.log("Retrieve lessons of selected room...");
        Lesson[][] table = new Lesson[5][12];

        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                for (int k = 0; k < timetable[i][j].length; k++) {
                    Lesson l = timetable[i][j][k];

                    if (l != null && contains(l.getClassrooms(), cl)) {
                        table[j][k] = l;
                    }
                }
            }
        }
        return table;
    }

    public void loadAllLessons(WebEngine we, int week) {
        Log.log("Start loading all lessons...");
        classes.clear();
        kuerzel.clear();
        classrooms.clear();

        we.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

            int counter = 0;

            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {

                if (newValue == State.SUCCEEDED) {
                    if (counter < timetable.length) {
                        Log.log("Analyzing and parsing HTML document...");
                        
                        timetable[counter] = analyzeDoc(we.getDocument(), week, counter);
                        counter++;
                        String url = "https://supplierplan.htl-kaindorf.at/supp_neu/" + (week) + "/c/c" + String.format("%05d", counter) + ".htm";
                        we.load(url);
                        
                        Log.log("Browsing to " + url);
                    } else {
                        //WebBrowserTest.printAllLessons(timetable);
                        we.getLoadWorker().stateProperty().removeListener(this);
                        
                        Log.log("Finished loading");
                    }
                }
            }
        });

        we.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + (week) + "/c/c" + String.format("%05d", 1) + ".htm");
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
        char[] ignorableChars
                = {
                    '.', ',', '\'', '\"', '!', '?', '§', '$',
                    '%', '&', '/', '(', ')', '=', '\\', ']',
                    '[', '{', '}', '#', '+', '*', '~', '\n'
                };

        for (char ignorableChar : ignorableChars) {
            s = s.replace(ignorableChar + "", "");
        }

        return s;
    }

    public String removeLineBreak(String s) {
        String s1 = "";

        s1 = s.replace("\n", "");

        return s1;
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<String> getKuerzel() {
        return kuerzel;
    }

    public Lesson[][][] getTimetable() {
        return timetable;
    }

    public List<String> getClassrooms() {
        return classrooms;
    }

}
