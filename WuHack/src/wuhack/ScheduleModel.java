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
public class ScheduleModel
{

  public Lesson[][] analyzeDoc(Document d, int calweek)
  {
    Lesson[][] schedule = new Lesson[5][12];
    
    NodeList centers = d.getElementsByTagName("center");
    NodeList centerChilds = centers.item(0).getChildNodes();

//    for (int i = 0; i < centerChilds.getLength(); i++)
//    {
//      System.out.println(centerChilds.item(i).toString());
//    }

    NodeList tables = d.getElementsByTagName("table");

    HTMLTableElement table = (HTMLTableElement) tables.item(0);
//    System.out.println(tables.item(0).getTextContent());

    HTMLCollection rows = table.getRows();

    for (int i = 1; i < rows.getLength(); i = i + 2)
    {
      HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);

      HTMLCollection cells = row.getCells();

      for (int j = 1; j < cells.getLength(); j++)
      {
        LinkedList<Kürzel> teachers = new LinkedList<>();
        String subject = "???";
        LinkedList<String> classrooms = new LinkedList<>();
        int hour;
        WeekDay weekday;
        boolean isLesson = true;

        weekday = WeekDay.values()[j-1];
        hour = (i-1)/2 + 1;

        HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);
//        System.out.println("--------");
//        System.out.println("Index: " + i + " " + j
//                + " " + "rowspan: " + cell.getAttribute("rowspan")
//                + " colspan: " + cell.getAttribute("colspan")
//                + "~~~~~~~~~~\n" + cell.getTextContent());

        HTMLTableElement inTable = (HTMLTableElement) cell.getChildNodes().item(0);
        HTMLCollection inRows = inTable.getRows();
        
    //  subject = ((HTMLTableRowElement)inRows.item(0)).getCells().item(0).getTextContent().trim();

        for (int k = 0; k < inRows.getLength(); k++)
        {
          HTMLTableRowElement inRow = (HTMLTableRowElement) inRows.item(k);
          HTMLCollection inCells = inRow.getCells();

          if (k == 0)
          {
            subject = inCells.item(0).getTextContent().trim();
          }
          else
          {
            if(inCells.item(0).getTextContent().trim().length() > 2)
            {
              // TODO!! (derweil wird nur gelöscht)
              isLesson = false;
              
              System.out.println("No lesson: " + inRow.getTextContent());
            }
            else
            {
              System.out.println(inCells.item(0).getTextContent().trim() + " - - - " + inCells.item(1).getTextContent().trim());
              teachers.add(Kürzel.valueOf(inCells.item(0).getTextContent().trim()));
              classrooms.add(inCells.item(1).getTextContent().trim());
            }
            
            try
            {
              
            }
            catch(Exception ex)
            {
              // TODO! (derweil wird gelöscht)
              isLesson = false;
              // System.out.println("no lesson!");
            }
          }

        }
        
        for (int k = 0; k < (Integer.parseInt(cell.getAttribute("rowspan")) / 2); k++)
        {          
          if(isLesson && schedule[j-1][((i-1)/2) + k] == null)
          {
            Lesson l = new Lesson(convertTeachers(teachers), subject, convertClassrooms(classrooms), hour + k, calweek, weekday);
            
            schedule[j-1][((i-1)/2) + k] = l;
          }
        }
      }

    }

    return schedule;
  }
  
  private Kürzel[] convertTeachers(LinkedList<Kürzel> teachers)
  {
    Kürzel[] array = new Kürzel[teachers.size()];
    
    for (int i = 0; i < teachers.size(); i++)
    {
      array[i] = teachers.get(i);
    }
    
    return array;
  }
  
  private String[] convertClassrooms(LinkedList<String> classrooms)
  {
    String[] array = new String[classrooms.size()];
    
    for (int i = 0; i < classrooms.size(); i++)
    {
      array[i] = classrooms.get(i);
    }
    
    return array;
  }
}
