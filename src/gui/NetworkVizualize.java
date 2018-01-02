/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import network.Layer;
import network.Network;
import network.Neuron;
import spamfilter.NewClass;

/**
 *
 * @author N
 */
public class NetworkVizualize {

    private Network network;
    private PCanvas canvas;
    PLayer layerhrany;
    PLayer layervrcholy;
    int maxNeurons;

    public NetworkVizualize(Network n) {
        init();
        maxNeurons = 57;
        this.network = n;
        canvas = new PCanvas();
        layerhrany = new PLayer();
        layervrcholy = new PLayer();
        canvas.getCamera().addLayer(0, layerhrany);
        canvas.getCamera().addLayer(1, layervrcholy);
        canvas.requestFocus();
        show();
    }

    public PCanvas getCanvas() {
        return canvas;
    }

    private void init() {
//        ArrayList<Neuron> inputLayer = new ArrayList<>();
//        ArrayList<Neuron> hiddenLayer = new ArrayList<>();
//        ArrayList<Neuron> hiddenLayer2 = new ArrayList<>();
//        ArrayList<Neuron> outputLayer = new ArrayList<>();
//
//        Random rnd = new Random();
//        for (int i = 0; i < 57; i++) {
//            Neuron n = new Neuron(57);
//            n.getWeights()[i] = 1;
//            inputLayer.add(n);
//        }
//        for (int i = 0; i < 57; i++) {
//            Neuron n = new Neuron(57);
//            for (int j = 0; j < n.getWeights().length; j++) {
//                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
//            }
//            n.setBias(1 + rnd.nextDouble() * -2);
//            hiddenLayer.add(n);
//        }
//
//        for (int i = 0; i < 4; i++) {
//            Neuron n = new Neuron(57);
//            for (int j = 0; j < n.getWeights().length; j++) {
//                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
//            }
//            n.setBias(1 + rnd.nextDouble() * -2);
//            hiddenLayer2.add(n);
//        }
//
//        for (int i = 0; i < 1; i++) {
//            Neuron n = new Neuron(4);
//            for (int j = 0; j < n.getWeights().length; j++) {
//                n.getWeights()[j] = 1 + rnd.nextDouble() * -2;
//            }
//            n.setBias(1 + rnd.nextDouble() * -2);
//            outputLayer.add(n);
//        }

//        Layer input = new Layer(inputLayer);
//        //Layer hidden = new Layer(hiddenLayer);
//        Layer hidden2 = new Layer(hiddenLayer2);
//        Layer output = new Layer(outputLayer);
        //network = new Network(0.1, input/*, hidden*/, hidden2, output);
    }

    private void show() {
        int xFloat = 4000;
        int yFloat = 100;
        Layer l;
        Neuron n;
        double y = 0;
        double prevY = 0;
        for (int i = 0; i < network.getLayers().size(); i++) {
            l = network.getLayers().get(i);
            y = (yFloat * (maxNeurons - l.getNeurons().size())) / 2.0;
            for (int j = 0; j < l.getNeurons().size(); j++) {
                n = l.getNeurons().get(j);
                PPath p = PPath.createEllipse(i * xFloat - 30, (float) (y + j * yFloat) - 30, 60, 60);

                ((PNode) p).setPickable(false);
                p.setPaint(Color.BLACK);
                layervrcholy.addChild(p);

                if (l.getPreviousLayer() != null) {
                    prevY = (yFloat * (maxNeurons - l.getPreviousLayer().getNeurons().size())) / 2.0;

                    for (int k = 0; k < n.getWeights().length; k++) {
                        PPath edge = PPath.createLine(i * xFloat, (float) (y + j * yFloat), (i - 1) * xFloat, (float) (prevY + k * yFloat));

                        ((PNode) edge).setPickable(false);
                        ((PNode) edge).addAttribute("neuron", n);
                        if (n.getWeights()[k] < 0) {
                            edge.setStroke(new BasicStroke((float) Math.pow(-1.0 * n.getWeights()[k], 2)));
                            edge.setStrokePaint(Color.RED);
                        } else {
                            edge.setStroke(new BasicStroke((float) Math.pow(n.getWeights()[k], 2)));
                            edge.setStrokePaint(Color.BLACK);
                        }
                        layerhrany.addChild(edge);
                    }
                }

            }
        }
        canvas.repaint();
    }

    public void refresh() {
        PPath edge;
        Neuron n;
        for (Object allNode : layerhrany.getChildrenReference()) {
            edge = (PPath) allNode;
            n = (Neuron) edge.getAttribute("neuron");
            for (int k = 0; k < n.getWeights().length; k++) {
                if (n.getWeights()[k] < 0) {
                    edge.setStroke(new BasicStroke((float) Math.pow(-1.0 * n.getWeights()[k], 2)));
                    edge.setStrokePaint(Color.RED);
                } else {
                    edge.setStroke(new BasicStroke((float) Math.pow(n.getWeights()[k], 2)));
                    edge.setStrokePaint(Color.BLACK);
                }
            }
        }
        canvas.repaint();
    }

    public Network getNetwork() {
        return network;
    }
    
    

}
