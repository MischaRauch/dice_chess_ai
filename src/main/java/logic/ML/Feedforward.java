package logic.ML;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.List;



//Eclipse Deeplearning4j Development Team. Deeplearning4j: Open-source distributed deep learning for the JVM, Apache Software Foundation License 2.0.
// code for network configuration from https://deeplearning4j.konduit.ai/v/en-1.0.0-beta7/models/multilayernetwork
// based on https://www.youtube.com/watch?v=PEEsve1Xv4s&t=987s

public class Feedforward {

public static void main(String[] args) throws IOException,NullPointerException {

    //setting the Parameters

   final int rows = 20;
   final int cols = 20;
   int outputNumber = 10;
   int size = 100; // input feed per learning step
   int times = 10; // no. of time pass through the entire data
    int initializeSeed = 100; //initialization

   //load data into training data and test data

    DataSetIterator train = new MnistDataSetIterator(size,true,initializeSeed);
    DataSetIterator test  = new MnistDataSetIterator(size,false,initializeSeed);

    // Network Configuration

    MultiLayerConfiguration con = new NeuralNetConfiguration.Builder()
              .seed(initializeSeed)
            .updater(new AdaGrad(0.5))
            .weightInit(WeightInit.XAVIER)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .activation(Activation.RELU)
            .l2(0.002)
            .list()
    //Addition of Layers
            //hidden layer
            .layer(0,new DenseLayer.Builder()
                    .nIn(rows*cols).nOut(200)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU)
                    .build())
            //Output layer
            .layer(1,new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                            .nIn(200).nOut(outputNumber)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.SOFTMAX)
                    .build())
            .build();

    // initialization of  the network
    MultiLayerNetwork neuralNetwork = new MultiLayerNetwork(con);
    neuralNetwork.init();
    System.out.println(neuralNetwork.summary());
    for(int i=0;i<times;i++)
    {
        neuralNetwork.fit(train);
    }
     // Evaluating the Network
    Evaluation evaluate = new Evaluation(outputNumber);
    while(test.hasNext())
    {
        DataSet data = test.next();
        INDArray output = neuralNetwork.output(data.getFeaturesMaskArray());
        evaluate.eval(data.getLabels(),output);
    }
    evaluate.stats();
}


}
