/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

/**
 *
 * @author N
 */
public class Neuron {

    private int inputs;
    private double bias;
    private double[] weights;
    private double perceptonValue;
    private double output;

    public Neuron(int inputs) {
        this.inputs = inputs;
        this.weights = new double[inputs];
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getPerceptonValue() {
        return perceptonValue;
    }
    
    public double calculatePerceptonValue(double[] input) {
        
        if(input.length != weights.length) {
            throw new Error("Input size and weights size differ!");
        }
        perceptonValue = 0;
        for (int i = 0; i < weights.length; i++) {
            perceptonValue += weights[i] * input[i];
        }
        perceptonValue -= bias;

        return perceptonValue;
    }
    
    protected double calculateOuput(double[] input){
        double calculatePerceptonValue = calculatePerceptonValue(input);
        return output = sigmoid(calculatePerceptonValue);
    }
    
    /**
     * sigmoid function -> 1 / (1+e^-x)
     *
     * @param x vstup
     * @return
     */
    protected static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }
    
    public void setBias(double bias) {
        this.bias = bias;
    }
}
