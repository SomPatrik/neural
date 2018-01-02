/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.ArrayList;

/**
 *
 * @author N
 */
public class Layer {

    private int layerSize;
    private ArrayList<Neuron> neurons;
    private double[] output;
    private double[] delta;
    private Layer previousLayer;
    private Layer nextLayer;

//    public Layer(int layerSize) {
//        this.layerSize = layerSize;
//        this.neurons = new ArrayList<>();
//        this.output = new double[layerSize];
//    }
    public Layer(ArrayList<Neuron> neurons) {
        this.layerSize = neurons.size();
        this.neurons = neurons;
        this.output = new double[layerSize];
        this.delta = new double[layerSize];
    }

    protected Neuron getNeuron(int index) {
        return neurons.get(index);
    }

    protected void calculateOutput(double[] input) {
        Neuron n;
        for (int i = 0; i < layerSize; i++) {
            n = neurons.get(i);
            output[i] = n.calculateOuput(input);
        }
    }

    public double[] getOutput() {
        return output;
    }

    public double[] getDelta() {
        return delta;
    }

    protected void performBackPropagation(double[] deltaNextLayer) {
        Neuron n;
        double errorFactor;
        for (int i = 0; i < delta.length; i++) {
            n = neurons.get(i);
            errorFactor = 0;

            for (int j = 0; j < deltaNextLayer.length; j++) {
                if (nextLayer == null) {
                    errorFactor += deltaNextLayer[j];
                } else {
                    errorFactor += deltaNextLayer[j] * nextLayer.getNeuron(j).getWeights()[i];
                }
            }
            delta[i] = errorFactor * n.getOutput() * (1.0 - n.getOutput());
        }
    }

    protected void updateWeights(double step) {
        Neuron n;
        for (int i = 0; i < neurons.size(); i++) {
            n = neurons.get(i);
            for (int j = 0; j < n.getWeights().length; j++) {
                n.getWeights()[j] += step * delta[i] * previousLayer.getNeuron(j).getOutput();
            }
            n.setBias(n.getBias() + step * delta[i]);
        }
    }

    public Layer getPreviousLayer() {
        return previousLayer;
    }

    protected void connectLayer(Layer layer) {
        this.nextLayer = layer;
        layer.previousLayer = this;
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public Layer getNextLayer() {
        return nextLayer;
    }
    
    

}
