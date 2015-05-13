/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wuhack;

import java.util.LinkedList;
import javafx.scene.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Julian
 */
public class ScheduleModel {
    
 public LinkedList<Lesson> analyzeDoc(Document d)
 {
   NodeList centers = d.getElementsByTagName("center");
   NodeList centerChilds = centers.item(0).getChildNodes();
   
   for (int i = 0; i < centerChilds.getLength(); i++)
   {
     System.out.println(centerChilds.item(i).toString());
   }
   
   
   
   
   return null;
 }
  
}