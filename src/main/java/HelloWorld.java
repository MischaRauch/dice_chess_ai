import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloWorld{

    public static void main(String []args){

        System.out.println("Hello World");

        getPossibleStates(4);

    }

    static List<Integer> getPossibleStates(int noOfBonesInHeap) {
        return IntStream.rangeClosed(1, 3).boxed()
                .map(i -> noOfBonesInHeap - i)
                .filter(newHeapCount -> newHeapCount >= 0)
                .collect(Collectors.toList());
    }
}