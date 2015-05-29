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
  public static Document convertToHTML(Lesson[][] schedule, String title, int calweek)
  {
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
    htmltag.appendChild(body);
    
    Element stylenode = doc.createElement("stylesheets");
    Element urlnode = doc.createElement("URL");
    urlnode.setAttribute("value", "@schedule.css");
    stylenode.appendChild(urlnode);
    head.appendChild(stylenode);
    
     
    
    Element centernode = doc.createElement("center");
    
    Element titlenode = doc.createElement("font");
    titlenode.setTextContent(title);
    titlenode.setAttribute("id", "title");
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
      tr.appendChild(numbertd);
      i++;
      
      for (Lesson lesson : day)
      {
//        Element td = doc.createElement("td");
//        Element table = doc.createElement("table");
//        td.setTextContent( (lesson != null ? lesson.getSubject() : "") );
//        System.out.println(""+lesson);
//        tr.appendChild(td);
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
