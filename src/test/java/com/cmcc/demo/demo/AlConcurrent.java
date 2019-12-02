package com.cmcc.demo.demo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: dyliu
 * @Description: 循环顺序打印10次ABC
 * @Date: 17:52 2019/12/2
 **/
public class AlConcurrent {

    public static void main(String[] args) throws InterruptedException {
        Instance instance = new Instance();
        Thread threadA = new Thread(new AlRunable(instance, "thread_a"));
        Thread threadB = new Thread(new AlRunable(instance, "thread_b"));
        Thread threadC = new Thread(new AlRunable(instance, "thread_c"));

        threadA.start();
        threadB.start();
        threadC.start();
    }

    @Data
    static class Instance {
        //请在这里补全代码
        int countnum = 0;
        Lock lock = new ReentrantLock();
        Map<String, Condition> thread4Condition = new LinkedHashMap<>();
        LinkedHashMap<String, String> nextThreadName = new LinkedHashMap<String, String>() {
        };

        Integer getCountNum() {
            return countnum;
        }

        void setCountnum(int countnum) {
            this.countnum = countnum;
        }

        Instance() {
            //线程级联绑定
            nextThreadName.put("thread_a", "thread_b");
            nextThreadName.put("thread_b", "thread_c");
            nextThreadName.put("thread_c", "thread_a");
            //线程与自己的condition绑定
            thread4Condition.put("thread_a", lock.newCondition());
            thread4Condition.put("thread_b", lock.newCondition());
            thread4Condition.put("thread_c", lock.newCondition());
        }

        Condition getConditionsByThreadName(String threadName) {
            return thread4Condition.get(threadName);
        }

    }

    static class AlRunable implements Runnable {
        //请在这里补全代码
        static int count;
        Lock lock;
        Condition condition;
        Condition conditionNext;
        Instance instance;
        String threadName;

        AlRunable(Instance instance, String name) {
            this.threadName = name;
            this.instance = instance;
        }

        @Override
        public void run() {
            //请在这里补全代码
            count = instance.getCountNum();
            condition = instance.getConditionsByThreadName(threadName);
            conditionNext = instance.getConditionsByThreadName(instance.getNextThreadName().get(threadName));
            lock = instance.getLock();
            while (true) {
                lock.lock();
                //last unlock
                if (count == 10) {
                    conditionNext.signal();
                    lock.unlock();
                    break;
                }
                //await currentThread && sigal next Thread
                try {
                    System.out.println(Thread.currentThread().getName() + "-ABC-" + count);
                    instance.setCountnum(++count);
                    conditionNext.signal();
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


}
