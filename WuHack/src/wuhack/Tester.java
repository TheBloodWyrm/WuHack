/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;

/**
 *
 * @author Julius
 */
public class Tester {

    public static void main(String[] args) {
        int[][] ints = new int[1][2];
        System.out.println(ints.length);

        int wl = LocalDate.now().getDayOfYear() / 7 + 1;
        int wc = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        System.out.println(wl + " " + wc);

        try {
            URL url = new URL("https://supplierplan.htl-kaindorf.at/supp_neu/" + wc + "/c/c00000" + 1 + ".htm");
            url.openConnection().getInputStream().read();

            System.out.println("end");
        } catch (HttpRetryException ex) {
            System.out.println("Http");
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            System.out.println("Protocol");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IO");
            System.out.println(ex.toString());
            System.out.println(ex.getClass());
            ex.printStackTrace();
        }
    }
}
