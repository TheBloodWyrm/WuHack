/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Authenticator;
import java.util.Calendar;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author alle
 */
public class WebBrowserTest extends Application {

    private Lesson[][] schedule;
    private Lesson[][][] timetable = new Lesson[32][5][12];
    private int counter = 1;
    private WebEngine webEngine;
    private boolean first = true;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        WebView browser = new WebView();
        webEngine = browser.getEngine();
        root.getChildren().add(browser);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("gayer Scheiß");
        primaryStage.setScene(scene);
        primaryStage.show();

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                System.out.println(newValue);
                if (first) {
                    if (newValue == Worker.State.SUCCEEDED) {
                        ScheduleModel m = ScheduleModel.getInstance(); //new ScheduleModel();
                        schedule = m.analyzeDoc(webEngine.documentProperty().get(), getCalendarWeek(), Integer.parseInt("00001"));
                        printSchedule();
                        printAllNodes(webEngine.getDocument());
//            System.out.println("Teachers");
//            timetable[counter - 1] = schedule;
//            schedule = m.getTeacherLessons("RI", timetable);
//            printSchedule();
//            System.out.println("Counter: "+counter);
//          if(counter <= 31)
//            loadNext();
                        webEngine.loadContent(convertToString(HTMLModel.convertToHTML(schedule, "1AHIF", getCalendarWeek())));
                        first = false;
                    }
                }

            }
        });

        // Popup führt Anmeldung durch
        FXMLPopupController pop = new FXMLPopupController();
        FXMLLoader l = new FXMLLoader(WebBrowserTest.class.getResource("FXMLPopup.fxml"));
        l.setController(pop);
        Popup auth = new Popup();
        auth.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("" + pop.isReady());
                Authenticator.setDefault(new AuthenticatorTest(pop.getUserName(), pop.getPassword()));
                webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + (getCalendarWeek() + 1) + "/c/c00001.htm");
                //loadNext();
            }
        });
        try {
            auth.getContent().add(l.load());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        auth.show(primaryStage);

    }

    private void loadNext() {
        webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + getCalendarWeek() + "/c/c" + String.format("%05d", counter) + ".htm");
        counter++;
    }

    private int getCalendarWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        return week;
    }

    private void printSchedule() {
        System.out.print("     ");
        for (int i = 0; i < 5; i++) {
            System.out.print(String.format("%1$30s", WeekDay.values()[i]));
        }
        System.out.println();

        String[] lines = new String[24];

        for (int i = 0; i < lines.length; i++) {
            String line = "";
        }

        for (int i = 0; i < lines.length; i += 2) {
            lines[i] = 1 + i / 2 + "    ";
        }

        for (int i = 0; i < schedule.length; i++) {
            Lesson[] day = schedule[i];

            // System.out.println("lines.length = " + lines.length);
            // System.out.println("day.length = " + day.length);
            for (int j = 0; j < day.length; j++) {
                Lesson lesson = day[j];
                // System.out.println("j = " + j);

                if (lesson != null) {
                    if (lesson.getTeachers().length > 0 && lesson.getClassrooms().length > 0) {
                        lines[j * 2] += String.format("%1$25s", lesson.getSubject()) + "     ";
                        lines[j * 2 + 1] += "        " + String.format("%1$7s", lesson.getTeachers()[0]);
                        lines[j * 2 + 1] += String.format("%1$7s", lesson.getClassrooms()[0]) + "        ";
                    }
                }
            }
        }

        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String convertToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    public static void printAllNodes(Document d) {

        System.out.println("WebBrowserTest.printAllNodes: Start printing...");
        System.out.println(d);

        printNodes(d.getChildNodes(), 0);

        System.out.println("WebBrowserTest.printAllNodes: End");
    }

    public static void printNodes(NodeList nl, int depth) {

        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);

            System.out.print("WebBrowserTest.printNodes: ");
            System.out.print(depth + " ");
            for (int j = 0; j < depth; j++) {
                System.out.print("\t");
            }

            System.out.print(n.getNodeName() + " " + n.getClass().getName());

            if (n.hasAttributes()) {
                System.out.print("\t");

                NamedNodeMap a = n.getAttributes();
                for (int j = 0; j < a.getLength(); j++) {
                    Node na = a.item(j);
                    System.out.print(na.getNodeName() + ":" + na.getTextContent() + " ");
                }
            }

            System.out.println();

            if (n.hasChildNodes()) {
                printNodes(n.getChildNodes(), depth++);
            }
        }

        depth--;
    }

    private static void printLesson(Lesson l) {
        if (l == null) {
            System.out.println("---");
            return;
        }

        String teachers = "";
        for (String t : l.getTeachers()) {
            teachers = teachers + "\t" + t.trim();
        }

        String classrooms = "";
        for (String t : l.getClassrooms()) {
            classrooms = classrooms + "\t" + t.trim();
        }

        System.out.println(l.getSubject().trim() + "\t" + l.getKlasse().replace("\n", "").trim() + "\t" + teachers + "\t" + classrooms + " ");
    }

    public static void printAllLessons(Lesson[][][] l) {
        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < l[i].length; j++) {
                System.out.println("WebBrowserTest.printAllLessons: ");
                for (int k = 0; k < l[i][j].length; k++) {
                    Lesson lesson = l[i][j][k];

                    printLesson(lesson);

                }
                System.out.println();
            }
        }
    }

    public static void printLessons(Lesson[][] l) {
        for (int i = 0; i < l.length; i++) {
            System.out.println("WebBrowserTest.printLessons: ");
            for (int j = 0; j < l[i].length; j++) {
                Lesson lesson = l[i][j];

                printLesson(lesson);
            }
            System.out.println();
        }
    }
    
    public static void analyzeCell(String str) {
        String[] a = str.split("\n");
        
        System.out.print(a.length+"_");
        for (String s : a) {
            String[] a1 = s.split(" ");
            
            for (String s1 : a1) {
                System.out.print(s1+"_");
            }
        }
        System.out.println();
    }
    
    public static void analyzeCell(HTMLTableElement table) {
        HTMLCollection rows = table.getRows();
        
        System.out.println("WebBrowserTest.analyzeCell: size: "+rows.getLength());
        
        for (int i = 0; i < rows.getLength(); i++) {
            HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);
            
            HTMLCollection cells = row.getCells();
            
            for (int j = 0; j < cells.getLength(); j++) {
                HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);
                if(cell != null)
                System.out.print(cell.getTextContent().trim()+"_");
            }
        }
        
        System.out.println();
    }
}
