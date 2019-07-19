package com.cdxt.threadpool.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdxt.threadpool.threadpool.InsertData.exec;

public class TestInsertData {
    public static void main(String[] args) {
        test1();
    }

    /***
     * 测试数据
     */
    private static void test1() {
        List<String> list = new ArrayList<>();
        //数据越大线程越多
        for (int i = 0; i < 3000000; i++) {
            list.add("hello"+i);
        }
        try {
            Long beginTime = new Date().getTime();
            exec(list);
            Long timeOut=new Date().getTime()-beginTime;
            System.out.println("耗时："+timeOut+"ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
