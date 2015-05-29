package wuhack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLModel
{
    private static final String titleSheet = "font-size:23pt;";
    private static final String bodySheet = "font-family: 'Segoe UI', sans-serif;font-size:14pt";
    private static final String firstLineSheet = "background-color:#353A3E;color:#FFFFFF;padding:5px;text-align: center;";
    private static final String numbercolSheet = "background-color:#555A5E;color:#FFFFFF;padding:5px;text-align: center;";
    
  public static Document convertToHTML(Lesson[][] schedule, String title, int calweek)
  {
      //"Stylesheets"
      
      
    String[] days = getDays(calweek);
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document doc = null;
    try
    {
      builder = factory.newDocumentBuilder();
      doc = builder.newDocument();
    }
    catch (ParserConfigurationException ex)
    {
      System.out.println("DocumentBuilder exception");
      return null;
    }
    
    Element htmltag = doc.createElement("html");
    doc.appendChild(htmltag);
    
    Element head = doc.createElement("head");
    htmltag.appendChild(head);
    
    Element body = doc.createElement("body");
    body.setAttribute("style", bodySheet);
    htmltag.appendChild(body);
    
    
    //stylesheet
    //<link rel="stylesheet" type="text/css" href="mystyle.css">
//    Element stylenode = doc.createElement("stylesheets");
//    Element urlnode = doc.createElement("URL");
//    urlnode.setAttribute("value", "@schedule.css");
//    stylenode.appendChild(urlnode);
//    head.appendChild(stylenode);
    
    Element stylenode = doc.createElement("link");
    stylenode.setAttribute("rel", "stylesheet");
    stylenode.setAttribute("type", "text/css");
    stylenode.setAttribute("href", "schedule.css");
    
    head.appendChild(stylenode);
    
     
    
    Element centernode = doc.createElement("center");
    
    Element titlenode = doc.createElement("font");
    titlenode.setTextContent(title);
    titlenode.setAttribute("id", "title");
    titlenode.setAttribute("style", titleSheet);
    centernode.appendChild(titlenode);
    
    Element datenode = doc.createElement("font");
    datenode.setTextContent(days[0] + " - " + days[1]);
    datenode.setAttribute("id", "date");
    centernode.appendChild(datenode);
    
    Element table = doc.createElement("table");
    table.setAttribute("id", "table");
    table.setAttribute("border", "1");
    table.setAttribute("cellpadding", "1");
    table.setAttribute("cellspacing", "1");
    table.setAttribute("rules", "all");
    
    Element tablebody = doc.createElement("tablebody");
    
    
    Element firstline = doc.createElement("tr");
    firstline.setAttribute("id", "firstline");
    firstline.setAttribute("style", firstLineSheet);

    firstline.appendChild(doc.createElement("td"));
    
    
    for (WeekDay w : WeekDay.values())
    {
      Element td = doc.createElement("td");
      td.setAttribute("id", "weekday");
      td.setTextContent(w.toString());
      System.out.println(w.toString());
      firstline.appendChild(td);
    }
    
    tablebody.appendChild(firstline);
    
    int i = 1;
    for (Lesson[] day : schedule)
    {
      Element tr = doc.createElement("tr");
      tr.setAttribute("id", "otherline");
      Element numbertd = doc.createElement("td");
      numbertd.setAttribute("id", "numbercol");
      numbertd.setTextContent(i + "");
      numbertd.setAttribute("style", numbercolSheet);
      tr.appendChild(numbertd);
      i++;
      
      for (int k = 0; k<schedule.length; k++)
      for (int j = 0; j<schedule[k].length; j++)
      for (int l = 0; l<schedule.length; l++)
      {
        Lesson lesson = schedule[l][j];
        
        System.out.println(lesson);
        
        Element td = doc.createElement("td");
        Element t = doc.createElement("table");
        
        Element row1 = doc.createElement("tr");
        Element subject = doc.createElement("td");
        Element classes = doc.createElement("td");
        
        Element row2 = doc.createElement("tr");
        Element teachers = doc.createElement("td");
        Element classroom = doc.createElement("td");
        
        if(lesson != null) {
          System.out.println(lesson.getSubject()+" "+lesson.getKlasse()+" "+lesson.getTeachers()+" "+lesson.getClassrooms());
          
          subject.setTextContent(lesson.getSubject());
          
          classes.setTextContent(lesson.getKlasse());
          
          for(String str : lesson.getTeachers()) {
            teachers.setTextContent(teachers.getTextContent()+str+" \n");
          }
          
          for(String str : lesson.getClassrooms()) {
            classroom.setTextContent(classroom.getTextContent()+str+" \n");
          }
        }
        
        row1.appendChild(subject);
        row1.appendChild(classes);
        
        row2.appendChild(teachers);
        row2.appendChild(classroom);
        
        t.appendChild(row1);
        t.appendChild(row2);
        td.appendChild(t);
        tr.appendChild(td);
      }
      
      tablebody.appendChild(tr);
    }
    
    table.appendChild(tablebody);
    centernode.appendChild(table);
    body.appendChild(centernode);
        
    
    return doc;
  }
  
  private static String[] getDays(int calweek)
  {
    String[] days = new String[2];
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.");
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.WEEK_OF_YEAR, 23); 
    
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);    
    days[0] = sdf.format(cal.getTime());
    
    cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
    days[1] = sdf.format(cal.getTime());
    
    return days;
  }
}
