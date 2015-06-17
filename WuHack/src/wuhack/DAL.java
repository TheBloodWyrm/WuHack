/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

/**
 *
 * @author Paul Petritsch
 */
public class DAL {

    public static void download() {
        for (int j = 0; j < 2; j++) {
            int z = 0;
            String format = null;
            if (j == 0) {
                z = getCalendarWeek();
                format = String.format("%02d", z);
            } else {

                z = getCalendarWeek() + 1;
                format = String.format("%02d", z);
            }
            for (int i = 1;; i++) {
                try {
                    String formatted = String.format("%02d", i);
                    URL findurl = new URL("https://supplierplan.htl-kaindorf.at/supp_neu/" + format + "/c/c000" + formatted + ".htm");
                    BufferedReader in = new BufferedReader(new InputStreamReader(findurl.openStream()));
                    BufferedWriter docwriter = new BufferedWriter(new FileWriter(format + "outputfile" + formatted + ".html"));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        try {
                            docwriter.write(inputLine);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    System.out.println("it still works" + i + j);
                    in.close();
                    docwriter.close();
                } catch (IOException e) {
                    System.out.println("Reached end");
                    System.out.println(e.getMessage());
                    System.out.println(e.getCause());
                    break;
                }
            }
        }
    }

    private static int getCalendarWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        return week;
    }
    
    public static String getFileContent(String file) {
        StringBuilder sb = new StringBuilder();
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(DAL.class.getResourceAsStream(file)));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("File Not Found: "+ex.getMessage());
        }
        
        return sb.toString();
    }
}
