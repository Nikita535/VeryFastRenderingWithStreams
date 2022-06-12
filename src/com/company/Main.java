package com.company;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class Main{
    static long numOfOperations = 10_000_000_000L;
    static long numOfThreads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        System.out.println(new Date());
        ForkJoinPool pool =new ForkJoinPool((int)numOfThreads);
        System.out.println(pool.invoke(new ThreadExample(0,numOfOperations)));
        System.out.println(new Date());
    }
    static class ThreadExample extends RecursiveTask<Long>{
        long from, to;

        public ThreadExample(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if ((to - from) <= numOfOperations/numOfThreads){
                long j=0;
                for (long i = from; i <to; i++) {
                    j+=i;
                }
                return j;
            }else {
                long middle =(to + from)/2;
                ThreadExample firstHalf =new ThreadExample(from,middle);
                firstHalf.fork();
                ThreadExample secondHalf =new ThreadExample(middle+1,to);
                long secondValue = secondHalf.compute();
                return firstHalf.join() + secondValue;
            }
        }
    }
}
