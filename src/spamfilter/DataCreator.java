/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author N
 */
public class DataCreator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("data\\archive.ics.uci.edu.txt"));
        ArrayList<String> spam = new ArrayList<>();
        ArrayList<String> notSpam = new ArrayList<>();
        ArrayList<String> trainData = new ArrayList<>();
        int c = 0;
        String line;
        String[] linetmp;
        while (sc.hasNext()) {
            c++;
            line = sc.nextLine();
            linetmp = line.split(",");
            if (linetmp[linetmp.length - 1].equals("1")) {
                spam.add(line);
            } else {
                notSpam.add(line);
            }
        }
        System.out.println(c);
        System.out.println("----");
        System.out.println(spam.size());
        System.out.println(notSpam.size());

        Random rnd = new Random();
        for (int i = 0; i < 250; i++) {
            trainData.add(spam.remove(rnd.nextInt(spam.size())));
        }
        
        for (int i = 0; i < 250; i++) {
            trainData.add(notSpam.remove(rnd.nextInt(notSpam.size())));
        }
        
        
     
        PrintWriter pw = new PrintWriter(new File("data\\trainData.txt"));
        
        for (String string : trainData) {
            pw.println(string);
        }
        pw.close();
        
        pw = new PrintWriter(new File("data\\testData.txt"));
        
        for (String string : spam) {
            pw.println(string);
        }
        
        for (String string : notSpam) {
            pw.println(string);
        }
        pw.close();
    }

}
