package logic.ML;
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
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Feedforward {

public static void main(String[] args) {

    INDArray x0 = Nd4j.linspace(-10, 10, 500).reshape(500,1);
    INDArray y0 = Nd4j.linspace(-10, 10, 500).reshape(300,2);
    System.out.println(y0);
    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(12345)
            .weightInit(WeightInit.XAVIER)
            .updater(new AdaGrad(0.5))
            .activation(Activation.RELU)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .l2(0.0001)
            .list()
            .layer(0, new DenseLayer.Builder().nIn(784).nOut(250).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //First hidden layer
                    .build())
            .layer(1, new OutputLayer.Builder().nIn(250).nOut(10).weightInit(WeightInit.XAVIER).activation(Activation.SOFTMAX) //Output layer
                    .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .build())
            .build();
    MultiLayerNetwork network = new MultiLayerNetwork(conf);
    network.init();
    network.setListeners(new ScoreIterationListener(1));
    network.fit(new DataSet(x0,y0));
    System.out.println(network.output(x0));

}


}
