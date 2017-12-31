/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilter;

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
public class Main {

    static double STEP = 0.01;

    public static void main(String[] args) {
        ArrayList<Neuron> inputLayer = new ArrayList<>();
        ArrayList<Neuron> hiddenLayer = new ArrayList<>();
        ArrayList<Neuron> hiddenLayer2 = new ArrayList<>();
        ArrayList<Neuron> outputLayer = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < 57; i++) {
            Neuron n = new Neuron(57);
            //for (int j = 0; j < n.getWeights().length; j++) {
            n.getWeights()[i] = 1;//+ rnd.nextDouble() * -2;
            //}
            //n.setBias(1 + rnd.nextDouble() * -2);
            inputLayer.add(n);
        }
        for (int i = 0; i < 57; i++) {
            Neuron n = new Neuron(57);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
            }
            n.setBias(1 + rnd.nextDouble() * -2);
            hiddenLayer.add(n);
        }

        for (int i = 0; i < 4; i++) {
            Neuron n = new Neuron(57);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
            }
            n.setBias(1 + rnd.nextDouble() * -2);
            hiddenLayer2.add(n);
        }
        for (int i = 0; i < 1; i++) {
            Neuron n = new Neuron(4);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
            }
            n.setBias(1 + rnd.nextDouble() * -2);
            outputLayer.add(n);
        }

        ArrayList<String[]> trainData = loadData("data\\testData.txt");
        rnd = new Random();
        int correct = 0;
        int counter = 0;
        while (correct < 120) {

            double[] testCase = toDouble(trainData.get(rnd.nextInt(trainData.size())));
            double expected = testCase[testCase.length - 1];

            testCase = Arrays.copyOf(testCase, testCase.length - 1);

            double[] inputToHidden = new double[inputLayer.size()];
            int i = 0;
            for (Neuron neuron : inputLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(testCase)));
                inputToHidden[i++] = neuron.getOutput();
            }

            double[] inputToHidden2 = new double[inputLayer.size()];
            i = 0;
            for (Neuron neuron : hiddenLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputToHidden)));
                inputToHidden2[i++] = neuron.getOutput();
            }

            i = 0;
            double[] inputFromHidden2 = new double[hiddenLayer2.size()];

            for (Neuron neuron : hiddenLayer2) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputToHidden2)));
                inputFromHidden2[i++] = neuron.getOutput();
            }

            i = 0;
            for (Neuron neuron : outputLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputFromHidden2)));
            }

            double networkOutput = outputLayer.get(0).getOutput();
            double deltaOutputLayer = (expected - networkOutput) * derivative(networkOutput);

            System.out.println(counter + "." + "expected: " + expected + " output: " + networkOutput);


            if (Math.abs(networkOutput - expected) < 0.3) {
                correct++;
                System.out.println(networkOutput + " - " + expected + " | " + counter + " : " + correct);
            } else {
                correct = 0;
            }
            counter++;

            double[] deltaHiddenLayer2 = new double[hiddenLayer2.size()];
            double neuronOutput;
            
            //errors for hidden2 layer
            for (int j = 0; j < deltaHiddenLayer2.length; j++) {
                neuronOutput = hiddenLayer2.get(j).getOutput();
                deltaHiddenLayer2[j] = deltaOutputLayer * outputLayer.get(0).getWeights()[j] * derivative(neuronOutput);
            }

            double[] deltaHiddenLayer = new double[hiddenLayer.size()];
            double tmp;
            //errors for hidden layer
            for (int j = 0; j < deltaHiddenLayer.length; j++) {
                neuronOutput = inputLayer.get(j).getOutput();
                tmp = 0;
                for (int l = 0; l < deltaHiddenLayer2.length; l++) {
                    tmp += deltaHiddenLayer2[l] * hiddenLayer2.get(l).getWeights()[j];
                }
                deltaHiddenLayer[j] = tmp * derivative(neuronOutput);
            }

            i = 0;
            for (Neuron neuron : hiddenLayer) {
                for (int j = 0; j < neuron.getWeights().length; j++) {
                    neuron.getWeights()[j] += STEP * inputLayer.get(j).getOutput() * deltaHiddenLayer[i];
                }
                neuron.setBias(neuron.getBias() - STEP * deltaHiddenLayer[i]);
                i++;
            }
            i = 0;
            for (Neuron neuron : hiddenLayer2) {
                for (int j = 0; j < neuron.getWeights().length; j++) {
                    neuron.getWeights()[j] += STEP * hiddenLayer.get(j).getOutput() * deltaHiddenLayer2[i];
                }
                neuron.setBias(neuron.getBias() - STEP * deltaHiddenLayer2[i]);
                i++;
            }
            for (Neuron neuron : outputLayer) {
                for (int j = 0; j < neuron.getWeights().length; j++) {
                    neuron.getWeights()[j] += STEP * hiddenLayer2.get(j).getOutput() * deltaOutputLayer;
                }
                neuron.setBias(neuron.getBias() - STEP * deltaOutputLayer);
            }

        }
        System.out.println("---------------------- Testing -----------------------------");
        ArrayList<String[]> testData = loadData("data\\trainData.txt");
        correct = 0;
        for (String[] strings : testData) {

            double[] testCase = toDouble(strings);
            double expected = testCase[testCase.length - 1];
            testCase = Arrays.copyOf(testCase, testCase.length - 1);

            double[] inputToHidden = new double[inputLayer.size()];

            int i = 0;
            for (Neuron neuron : inputLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(testCase)));
                inputToHidden[i++] = neuron.getOutput();
            }
            
            double[] inputFromHidden = new double[hiddenLayer.size()];
            i = 0;
            for (Neuron neuron : hiddenLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputToHidden)));
                inputFromHidden[i++] = neuron.getOutput();
            }
            
            i = 0;
            double[] inputFromHidden2 = new double[hiddenLayer2.size()];
            for (Neuron neuron : hiddenLayer2) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputFromHidden)));
                inputFromHidden2[i++] = neuron.getOutput();
            }

            for (Neuron neuron : outputLayer) {
                neuron.setOutput(sigmoid(neuron.calculatePerceptonValue(inputFromHidden2)));
            }

            double networkOutput = outputLayer.get(0).getOutput();
            if (Math.abs(networkOutput - expected) < 0.3) {
                correct++;
            }
            System.out.println("expected: " + expected + " output: " + networkOutput);
        }
        System.out.println("correct: " + correct + " all: " + testData.size());
    }

    /**
     * sigmoid function -> 1 / (1+e^-x)
     *
     * @param x vstup
     * @return
     */
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * derrivation of sigmoid -> f'(x) = f(x) * (1 - f(x))
     *
     * @param x
     * @return
     */
    public static double derivative(double x) {
        return x * (1.0 - x);
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
