/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFontElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author Julian
 */
public class ScheduleModel
{

  private static LinkedList<String> classes = new LinkedList<>();
  private Lesson[][][] timetable = new Lesson[32][5][12];
  private List<String> kuerzel = new ArrayList<>(90);

  public void addClassname(String classname, int index)
  {
    index--;

    if (index >= classes.size())
    {
      index = classes.size() - 1;
      classes.add(index, classname);
    }
  }

  public Lesson[][] analyzeDoc(Document d, int calweek, int index)
  {
    Lesson[][] schedule = new Lesson[5][12];

    HTMLFontElement f = (HTMLFontElement) d.getElementsByTagName("font").item(1);

    System.out.println("FontText: " + f.getTextContent());
    String klasse = f.getTextContent();

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
      int doneCells = 0;

      HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);

      HTMLCollection cells = row.getCells();

      for (int j = 1; j < cells.getLength(); j++)
      {
        if (schedule[j - 1 + doneCells][((i - 1) / 2)] == null)
        {
          System.out.println("Lesson:");
          //LinkedList<Kürzel> teachers = new LinkedList<>();
          LinkedList<String> teachers = new LinkedList<>();
          String subject = "???";
          LinkedList<String> classrooms = new LinkedList<>();
          int hour;
          WeekDay weekday;
          boolean isLesson = true;

          weekday = WeekDay.values()[j - 1];
          hour = (i - 1) / 2 + 1;

          HTMLTableCellElement cell = (HTMLTableCellElement) cells.item(j);

          HTMLTableElement inTable = (HTMLTableElement) cell.getChildNodes().item(0);
          HTMLCollection inRows = inTable.getRows();

          for (int k = 0; k < inRows.getLength(); k++)
          {
            HTMLTableRowElement inRow = (HTMLTableRowElement) inRows.item(k);
            HTMLCollection inCells = inRow.getCells();

            if (k == 0)
            {
              if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 5)
              {
                // TODO!! (derweil wird nur gelöscht)
                isLesson = false;

                System.out.println("No lesson: " + inRow.getTextContent());
              }
              else
              {
                subject = inCells.item(0).getTextContent().trim();
                System.out.println(" " + subject);
              }
            }
            else
            {
              if (removeSigns(inCells.item(0).getTextContent().trim()).length() > 3)
              {
                // TODO!! (derweil wird nur gelöscht)
                isLesson = false;

                System.out.println("No lesson: " + inRow.getTextContent());
              }
              else
              {
                String classroom;

                if (inCells.getLength() == 1)
                {
                  classroom = "???";
                }
                else
                {
                  classroom = inCells.item(1).getTextContent().trim();
                }

                System.out.println(" " + removeSigns(inCells.item(0).getTextContent().trim()) + " - - - " + classroom);
                String kuerzel = removeSigns(inCells.item(0).getTextContent().trim()); //.replace("---", "NIEMAND");
                //teachers.add(Kürzel.valueOf(kuerzel));
                teachers.add(kuerzel);
                if(!this.kuerzel.contains(kuerzel))
                {
                  this.kuerzel.add(kuerzel);
                }                
                
                classrooms.add(classroom);
              }
            }
          }

          for (int k = 0; k < (Integer.parseInt(cell.getAttribute("rowspan")) / 2); k++)
          {
            System.out.println("rowspan: " + (Integer.parseInt(cell.getAttribute("rowspan"))/2));
            if (isLesson && schedule[j - 1 + doneCells][((i - 1) / 2) + k] == null)
            {
              //Lesson l = new Lesson(convertTeachers(teachers), subject, klasse, convertClassrooms(classrooms), hour + k, calweek, weekday);
              Lesson l = new Lesson(teachers.toArray(new String[0]), subject, klasse, classrooms.toArray(new String[0]), hour + k, calweek, weekday);
              schedule[j - 1][((i - 1) / 2) + k] = l;
            }
          }
        }
        else
        {
          System.out.println("longer lesson!");
          doneCells++;
          j--;
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

  public Lesson[][] getTeacherLessons(String ku, Lesson[][][] le)
  {
    Lesson[][] table = new Lesson[12][12];

    for (int i = 0; i < le.length; i++)
    {
      for (int j = 0; j < le[i].length; j++)
      {
        for (int k = 0; k < le[i][j].length; k++)
        {
          Lesson l = le[i][j][k];

          if (l != null && containsKürzel(l.getTeachers(), ku))
          {
            int day = l.getWeekDay().ordinal();
            int hour = l.getHour() - 1;

            table[day][hour] = l;
          }
        }
      }
    }

    return table;
  }

  private boolean containsKürzel(String[] a, String k)
  {
    boolean b = false;

    for (int i = 0; i < a.length; i++)
    {
      if (a[i].equals(k))
      {
        b = true;
      }
    }

    return b;
  }

  private String removeSigns(String s)
  {
    String ret = "";
    char[] ignorableChars =
    {
      '.', ',', '\'', '\"', '!', '?', '§', '$',
      '%', '&', '/', '(', ')', '=', '\\', ']',
      '[', '{', '}', '#', '+', '*', '~'
    };
    for (int i : s.chars().toArray())
    {
      boolean fits = true;
      char c = (char) i;

      for (char ignorableChar : ignorableChars)
      {
        if (c == ignorableChar)
        {
          fits = false;
        }
      }

      if (fits)
      {
        ret += c;
      }
    }

    return ret;

  }
}
