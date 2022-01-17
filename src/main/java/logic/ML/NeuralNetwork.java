package logic.ML;

import org.tensorflow.*;
import org.tensorflow.ndarray.*;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TFloat32;
import org.tensorflow.types.TFloat64;
import org.tensorflow.types.TInt32;

import javax.naming.Name;
import java.util.List;

public class NeuralNetwork {
    //SavedModelBundle model;

    public static void main(String[] args) throws Exception {


        float[] input = {4,2,3,5,6,3,2,4,1,1,1,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,
                -1,0,0,0,0,0,0,0,-4,0,0,0,0,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,0,-2,-3,-5,-6,-3,-2,-4,0,1,0,0,0,0};

        float min =-6;
        float max =6;
        float[] scaled_input = new float[input.length];
        FloatNdArray data2 = NdArrays.ofFloats(Shape.of(1,70));

        for(int i=0;i<input.length;i++)
        {
            scaled_input[i] = (input[i]-min)/max-min;
            System.out.println(scaled_input[i]);
            data2.setFloat(scaled_input[i]);
        }

        System.out.println(data2);


        SavedModelBundle model = SavedModelBundle.load("C:\\Users\\sanch\\Documents\\Projects\\DiceChess2\\src\\main\\resources\\model\\dicechess_NN", "serve");

        try(TFloat32 tensor = Tensor.of(TFloat32.class, Shape.of(1,70), data2::copyTo)){
            System.out.println(tensor.get());
           /*  System.out.println(model.metaGraphDef());*/
            List<Tensor> output = model.session().runner()
                    .feed( "serving_default_dense_3_input:0", tensor)
                    .fetch("StatefulPartitionedCall:0").run();

            System.out.println(output.get(0).asRawTensor().data().asFloats().getFloat(0));

        }


    }


    public NeuralNetwork() throws Exception {
       // model = SavedModelBundle.load("C:\\Users\\sanch\\Documents\\Projects\\DiceChess2\\src\\main\\resources\\model\\dicechess_NN", "serve");

    }



}




