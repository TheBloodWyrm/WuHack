package wuhack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLModel
{
  public static Document convertToHTML(Lesson[][] schedule, boolean klasse) throws IOException, ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();
    
    Element nodecenter = doc.createElement("center");
    
    
    
    doc.appendChild(nodecenter);
    
    System.out.println(doc.toString());
    
    
    return doc;
  }
}
