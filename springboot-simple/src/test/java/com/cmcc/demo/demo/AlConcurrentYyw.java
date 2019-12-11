package com.cmcc.demo.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印10次ABC
 */
public class AlConcurrentYyw {

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
        private int COUNT = 10;
        //请在这里补全代码
        private ReentrantLock lock = new ReentrantLock();
        private volatile boolean aHasRun = false;
        private volatile boolean bHasRun = false;
        private Condition a = lock.newCondition();
        private Condition b = lock.newCondition();
        private Condition c = lock.newCondition();

        public Instance() {
        }

    }

    static class AlRunable implements Runnable {

        //请在这里补全代码
        private Instance instance;
        private String name;

        AlRunable(Instance instance, String name) {
            this.instance = instance;
            this.name = name;
        }

        @Override
        public void run() {
            //请在这里补全代码
            ReentrantLock lock = instance.lock;
            lock.lock();
            try {
                for (int i = 0; i < instance.COUNT; i++) {
                    switch (name) {
                        case "thread_a":
                            System.out.print("A");
                            instance.aHasRun = true;

                            instance.b.signal();
                            if (i < instance.COUNT - 1) {
                                instance.a.await();
                            }
                            break;
                        case "thread_b":
                            if (!instance.aHasRun) {
                                instance.b.await();
                            }
                            System.out.print("B");
                            instance.bHasRun = true;

                            instance.c.signal();
                            if (i < instance.COUNT - 1) {
                                instance.b.await();
                            }
                            break;
                        case "thread_c":
                            if (!instance.bHasRun) {
                                instance.c.await();
                            }
                            System.out.print("C");
                            System.out.print("|");

                            instance.a.signal();
                            if (i < instance.COUNT - 1) {
                                instance.c.await();
                            }
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

}