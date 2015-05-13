/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wuhack;

import java.util.LinkedList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author Julian
 */
public class ScheduleModel {
    
    private Lesson[][] schedule = new Lesson[5][12];
    
 public LinkedList<Lesson> analyzeDoc(Document d)
 {
   NodeList centers = d.getElementsByTagName("center");
   NodeList centerChilds = centers.item(0).getChildNodes();
   
   for (int i = 0; i < centerChilds.getLength(); i++)
   {
     System.out.println(centerChilds.item(i).toString());
   }
   
   NodeList tables = d.getElementsByTagName("table");
   
     HTMLTableElement table = (HTMLTableElement) tables.item(0);
     System.out.println(tables.item(0).getTextContent());
     
     HTMLCollection rows = table.getRows();
     
     for (int i = 1; i < rows.getLength(); i = i+2) {
         HTMLTableRowElement row = (HTMLTableRowElement)rows.item(i);
         
         HTMLCollection cells = row.getCells();
         
         for(int j = 0; j < cells.getLength(); j++) {
             HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);
             System.out.println("--------");
             System.out.println("Index: "+i+" "+j+" "+"rowspan: "+cell.getAttribute("rowspan")+" colspan: "+cell.getAttribute("colspan")+"~~~~~~~~~~\n"+cell.getTextContent());
         }
        
     }
   
   
   return null;
 }
  
}