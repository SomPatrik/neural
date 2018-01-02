/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 *
 * @author N
 */
public class Network implements Observable {

    private ArrayList<Layer> layers;
    private ArrayList<InvalidationListener> observers;
    private double networkOutput;
    private double step;
    
    public Network(double step,Layer... layers) {
        this.observers = new ArrayList<>();
        this.layers = new ArrayList<>();
        init(layers);
        this.step = step;
    }

    private void init(Layer[] layers) {
        this.layers.add(layers[0]);
        for (int i = 0; i < layers.length - 1; i++) {
            layers[i].connectLayer(layers[i + 1]);
            this.layers.add(layers[i + 1]);
        }

    }

    public double calculate(double[] input) {
        for (Layer layer : layers) {
            layer.calculateOutput(input);
            input = layer.getOutput();
        }
        networkOutput = layers.get(layers.size() - 1).getOutput()[0];

        return networkOutput;
    }
    
    public void performLearning(double errorOutput){
        Layer l;
        double[] delta = new double[]{errorOutput};
        for (int i = layers.size() - 1; i >= 1; i--) {
            l = layers.get(i);
            l.performBackPropagation(delta);
            delta = l.getDelta();
        }
        
        for (int i = 1; i < layers.size(); i++) {
            l = layers.get(i);
            l.updateWeights(step);
        }
        notifyAllobservers();
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }
    
    private void notifyAllobservers(){
        for (InvalidationListener observer : observers) {
            observer.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        observers.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        observers.remove(listener);
    }
}
