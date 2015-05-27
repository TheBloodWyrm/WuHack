package wuhack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLModel
{
  public static Document convertToHTML(Lesson[][] schedule, String title, int calweek) throws IOException, ParserConfigurationException
  {
    String[] days = getDays(calweek);
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();
    
    Element stylenode = doc.createElement("stylesheets");
    Element urlnode = doc.createElement("URL");
    urlnode.setAttribute("value", "@schedule.css");
    stylenode.appendChild(urlnode);
    doc.appendChild(stylenode);
    
     
    
    Element centernode = doc.createElement("center");
    
    Element titlenode = doc.createElement("font");
    titlenode.setTextContent(title);
    titlenode.setAttribute("id", "title");
    centernode.appendChild(titlenode);
    
    Element datenode = doc.createElement("font");
    datenode.setTextContent(days[0] + " - " + days[1] + "<br>");
    datenode.setAttribute("id", "date");
    centernode.appendChild(datenode);
    
    Element table = doc.createElement("table");
    table.setAttribute("id", "table");
    
    Element tablebody = doc.createElement("tablebody");
    
    
    Element firstline = doc.createElement("tr");
    firstline.setAttribute("id", "firstline");
    firstline.appendChild(doc.createElement("td"));
    
    
    for (WeekDay w : WeekDay.values())
    {
      Element td = doc.createElement("td");
      td.setAttribute("id", "weekday");
      td.setTextContent(w.toString());
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
        // TODO
      }
      
      tablebody.appendChild(tr);
    }
    
    table.appendChild(tablebody);
    centernode.appendChild(table);
    doc.appendChild(centernode);
        
    
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
