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
            data2.setFloat(scaled_input[i]);
        }

        SavedModelBundle model = SavedModelBundle.load("C:\\Users\\sanch\\Documents\\Projects\\DiceChess2\\src\\main\\resources\\model\\dicechess_NN", "serve");

       /* FloatNdArray data = NdArrays.ofFloats(Shape.of(1,70))
                .setFloat(input[0])
                .setFloat(input[1])
                .setFloat(input[2])
                .setFloat(input[3])
                .setFloat(input[4])
                .setFloat(input[5])
                .setFloat(input[6])
                .setFloat(input[7])
                .setFloat(input[8])
                .setFloat(input[9])
                .setFloat(input[10])
                .setFloat(input[11])
                .setFloat(input[12])
                .setFloat(input[13])
                .setFloat(input[14])
                .setFloat(input[15])
                .setFloat(input[16])
                .setFloat(input[17])
                .setFloat(input[18])
                .setFloat(input[19])
                .setFloat(input[20])
                .setFloat(input[21])
                .setFloat(input[22])
                .setFloat(input[23])
                .setFloat(input[24])
                .setFloat(input[25])
                .setFloat(input[26])
                .setFloat(input[27])
                .setFloat(input[28])
                .setFloat(input[29])
                .setFloat(input[30])
                .setFloat(input[31])
                .setFloat(input[32])
                .setFloat(input[33])
                .setFloat(input[34])
                .setFloat(input[35])
                .setFloat(input[36])
                .setFloat(input[37])
                .setFloat(input[38])
                .setFloat(input[39])
                .setFloat(input[40])
                .setFloat(input[41])
                .setFloat(input[42])
                .setFloat(input[43])
                .setFloat(input[44])
                .setFloat(input[45])
                .setFloat(input[46])
                .setFloat(input[47])
                .setFloat(input[48])
                .setFloat(input[49])
                .setFloat(input[50])
                .setFloat(input[51])
                .setFloat(input[52])
                .setFloat(input[53])
                .setFloat(input[54])
                .setFloat(input[55])
                .setFloat(input[56])
                .setFloat(input[57])
                .setFloat(input[58])
                .setFloat(input[59])
                .setFloat(input[60])
                .setFloat(input[61])
                .setFloat(input[62])
                .setFloat(input[63])
                .setFloat(input[64])
                .setFloat(input[65])
                .setFloat(input[66])
                .setFloat(input[67])
                .setFloat(input[68])
                .setFloat(input[69])
                ;

        */


        try(TFloat32 tensor = Tensor.of(TFloat32.class, Shape.of(1,70), data2::copyTo)){
            System.out.println(tensor.get().shape());
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

    /*
    public static float[] scale(float[] input)
    {
        float min =-6;
        float max =6;
        float[] scaled_input = new float[input.length];
        for(int i=0;i<input.length;i++)
        {
            scaled_input[i] = (input[i]-min)/max-min;

        }
        return scaled_input;
    }
    */

}




