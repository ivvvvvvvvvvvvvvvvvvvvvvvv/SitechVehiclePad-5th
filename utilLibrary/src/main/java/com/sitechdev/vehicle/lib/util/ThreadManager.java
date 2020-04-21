package com.sitechdev.vehicle.lib.util;

import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wujinling on 2016/9/30.
 */

public class ThreadManager {
    // 饿汉式单例 天生线程安全
    private static final ThreadManager single = new ThreadManager();
    public static ThreadManager getInstance(){
        return single;
    }

    private int mThreadNum = Runtime.getRuntime().availableProcessors() * 3 ;
    private ExecutorService mExecutorService ;
    private List<Runnable> taskQueen ;
    private Thread poolThread ;
    private QueryRunning queryRunning ;

    private ThreadManager(){
        mExecutorService = Executors.newFixedThreadPool(mThreadNum) ;
        taskQueen = new LinkedList<>() ;
    }


    public void start(){
        if(poolThread == null){
            queryRunning = new QueryRunning() ;
            poolThread = new Thread(queryRunning) ;
            poolThread.start();
        }
    }

    public void addTask(Runnable r){
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Boolean>() {
            @Nullable
            @Override
            public Boolean doInBackground() throws Throwable {
                return null;
            }
        });
        synchronized (taskQueen) {
            taskQueen.add(r);
        }
        synchronized (poolThread) {
            poolThread.notify();
        }
    }

    public Runnable getTask(){
        synchronized (taskQueen) {
            if(!taskQueen.isEmpty()){
                return taskQueen.remove(0) ;
            }
        }
        return null ;
    }

    public void destory(){
        poolThread.interrupt();
        poolThread = null ;
    }

    private class QueryRunning implements Runnable{

        @Override
        public void run() {
            try {
                // 一直跑
                while (!Thread.currentThread().isInterrupted()){
                    synchronized (poolThread) {
                        // 如果任务栏为空，则让线程等待
                        if(taskQueen.isEmpty()){
                            poolThread.wait() ;
                        }
                        // 不为空的话则取出第一个任务线程给线程池处理
                        Runnable r = getTask() ;
                        if(r != null){
                            mExecutorService.execute(r);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
}
