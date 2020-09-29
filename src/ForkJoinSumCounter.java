import java.util.concurrent.RecursiveTask;

/**
 * 数组求和
 */
public class ForkJoinSumCounter extends RecursiveTask<Long> {

    /**
     * 用于存放要求和的数组
     */
    private final long[] numbers;
    /**
     * 起始位置
     */
    private final int start;
    /**
     * 结束位置
     */
    private final int end;
    /**
     * 查分的粒度大小 这里将每一百个数据分为一组进行处理 如果超过100会在进一步分组
     */
    private static final long THRESHOLD = 100;

    public ForkJoinSumCounter(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForkJoinSumCounter(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        //计算当前数组内的数据是否超出设置处理的最大数据 多余则进行拆分 不多则进行sum
        int length = end - start;
        if (length <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
            }
            return sum;
        }

        //如果大于设置的阈值 则进行拆分 拆分后会据需调用这个方法 一直到拆分不了进行sum操作
        ForkJoinSumCounter leftTask = new ForkJoinSumCounter(numbers, start, start + length / 2);
        //这里实际上是提交给了线程池去处理了 然后下面的接着走
        leftTask.fork();
        //接着掉用compute方法 据需走上面的逻辑 一直分解到能算sum拿到返回值为止
        ForkJoinSumCounter rightTask = new ForkJoinSumCounter(numbers, start  + length / 2, end);
        Long compute = rightTask.compute();
        //拿出上面提交到线程池的结果
        Long join = leftTask.join();
        //最后进行sum返回
        return compute + join;
    }

}
