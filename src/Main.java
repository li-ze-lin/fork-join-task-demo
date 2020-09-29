import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) {
        //生成1-1000的数组
        long[] longs = LongStream.rangeClosed(1, 9999L).toArray();

        //计算1+2+...+1000的值
        RecursiveTask<Long> recursiveTask = new ForkJoinSumCounter(longs);
        Long invoke = new ForkJoinPool().invoke(recursiveTask);

        //计算1+2+...+1000的值
        long forSum = 0;
        for (long aLong : longs) {
            forSum += aLong;
        }

        //对比看计算出来的值是否相同
        System.out.println("ForkJoinSum:" + invoke + ", for:" + forSum + ", check:" + (invoke == forSum));
    }

}
