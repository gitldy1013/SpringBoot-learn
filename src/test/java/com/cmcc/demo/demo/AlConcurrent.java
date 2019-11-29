package com.cmcc.demo.demo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印10次ABC
 */
public class AlConcurrent {

    public static void main(String[] args) {
        Instance instance = new Instance();
        Thread threadA = new Thread(new AlRunable(instance, "thread_a"));
        Thread threadB = new Thread(new AlRunable(instance, "thread_b"));
        Thread threadC = new Thread(new AlRunable(instance, "thread_c"));

        threadA.start();
        threadB.start();
        threadC.start();
    }

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

        public void setCountnum(int countnum) {
            this.countnum = countnum;
        }

        public Instance() {
            nextThreadName.put("thread_a", "thread_b");
            nextThreadName.put("thread_b", "thread_c");
            nextThreadName.put("thread_c", "thread_a");

            thread4Condition.put("thread_a", lock.newCondition());
            thread4Condition.put("thread_b", lock.newCondition());
            thread4Condition.put("thread_c", lock.newCondition());
        }

        public Lock getLock() {
            return lock;
        }

        public Condition getConditionsByThreadName(String threadName) {
            return thread4Condition.get(threadName);
        }

        public LinkedHashMap<String, String> getNextThreadName() {
            return nextThreadName;
        }
    }

    static class AlRunable implements Runnable {
        //请在这里补全代码
        int count;
        Lock lock;
        Condition condition;
        Condition conditionNext;
        Instance instance;
        String threadName;

        public AlRunable(Instance instance, String name) {
            this.threadName = name;
            this.instance = instance;
        }

        @Override
        public void run() {
            //请在这里补全代码
            this.count = instance.getCountNum();
            lock = instance.getLock();
            condition = instance.getConditionsByThreadName(threadName);
            conditionNext = instance.getConditionsByThreadName(instance.getNextThreadName().get(threadName));
            while (true) {
                lock.lock();
                try {
                    while (count % 3 != 2) {
                        condition.await();
                    }
                    instance.setCountnum(count++);
                    System.out.println(Thread.currentThread().getName() + "ABC");
                    conditionNext.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }

    }


}
