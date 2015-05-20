package wuhack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.Document;

public class HTMLModel
{
  public static Document convertToHTML(Lesson[][] schedule, boolean klasse) throws IOException
  {
    File file = new File("view.html");
    
    StringBuilder htmltext = new StringBuilder();
    
    if(klasse)
    {
      
    }
    
    BufferedWriter bw = new BufferedWriter(new FileWriter(file.toString()));
    bw.write("<html><head><title>" + schedule[0][0].getKlasse() + "</title></head><body><p>This is Body</p></body></html>");
    bw.close();
    
    return null;
  }
}
