/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilter;

import network.Layer;
import network.Network;
import network.Neuron;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N
 */
public class NewClass {

    public static void main(String[] args) {
        ArrayList<Neuron> inputLayer = new ArrayList<>();
        ArrayList<Neuron> hiddenLayer = new ArrayList<>();
        ArrayList<Neuron> outputLayer = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < 57; i++) {
            Neuron n = new Neuron(57);
            n.getWeights()[i] = 1;
            inputLayer.add(n);
        }
        for (int i = 0; i < 4; i++) {
            Neuron n = new Neuron(57);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
            }
            n.setBias(1 + rnd.nextDouble() * -2);
            hiddenLayer.add(n);
        }

        for (int i = 0; i < 1; i++) {
            Neuron n = new Neuron(4);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
            }
            n.setBias(1 + rnd.nextDouble() * -2);
            outputLayer.add(n);
        }
        Layer input = new Layer(inputLayer);
        Layer hidden = new Layer(hiddenLayer);
        Layer output = new Layer(outputLayer);

        Network n = new Network(0.1, input, hidden, output);

        ArrayList<String[]> trainData = loadData("data\\testData.txt");

        rnd = new Random();
        int correct = 0;
        int counter = 0;
        double networkOutput;

        while (correct < 100) {

            double[] testCase = toDouble(trainData.get(rnd.nextInt(trainData.size())));
            double expected = testCase[testCase.length - 1];

            testCase = Arrays.copyOf(testCase, testCase.length - 1);
            networkOutput = n.calculate(testCase);
            n.performLearning(expected - networkOutput);
            System.out.println(counter + "." + "expected: " + expected + " output: " + networkOutput);

            if (Math.abs(networkOutput - expected) < 0.3) {
                correct++;
                System.out.println(networkOutput + " - " + expected + " | " + counter + " : " + correct);
            } else {
                correct = 0;
            }
            counter++;
        }

        System.out.println("---------------------- Testing -----------------------------");
        ArrayList<String[]> testData = loadData("data\\trainData.txt");
        correct = 0;
        double expected;
        for (String[] strings : testData) {
            double[] testCase = toDouble(strings);
            expected = testCase[testCase.length - 1];
            testCase = Arrays.copyOf(testCase, testCase.length - 1);

            networkOutput = n.calculate(testCase);

            if (Math.abs(Math.round(networkOutput) - expected) < 0.3) {
                System.out.println("expected: " + expected + " output: " + networkOutput);
                correct++;
            } else {
                System.err.println("expected: " + expected + " output: " + networkOutput);
            }

        }
        System.out.println("correct: " + correct + " all: " + testData.size());

    }

    private static ArrayList<String[]> loadData(String path) {
        ArrayList<String[]> list = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNext()) {
                list.add(sc.nextLine().split(","));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private static double[] toDouble(String[] get) {
        double[] tmp = new double[get.length];
        for (int i = 0; i < get.length; i++) {
            tmp[i] = Double.parseDouble(get[i]);
        }
        return tmp;
    }
}
