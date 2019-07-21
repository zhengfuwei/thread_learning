package com.cdxt.threadpool.threadpool;

import com.cdxt.utils.MysqlConnection;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/***
 * 自定义线程类
 */
public class MyThread implements Runnable {
    private List<String> list;
    private CountDownLatch begin;
    private CountDownLatch end;

    //创建个构造函数初始化 list,和其他用到的参数
    public MyThread(List<String> list, CountDownLatch begin, CountDownLatch end) {
        this.list = list;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        PreparedStatement ps=null;
        Connection conn=null;
        try {

            for (int i = 0; i < list.size(); i++) {
                //这里还要说一下，，由于在实质项目中，当处理的数据存在等待超时和出错会使线程一直处于等待状态
                //这里只是处理简单的，
                //分批 批量插入
//                System.out.println("当前线程名称："+Thread.currentThread().getName()+" 执行结果："+i);
                try {
                    conn= MysqlConnection.getConn();
                    if (conn!=null){
                        conn.setAutoCommit(false);
                    }
                    String sql = "insert into test (name,time) values(?,?)"; // 生成一条sql语句
                    // 创建一个Statment对象
                    ps = conn.prepareStatement(sql);

                    // 为sql语句中第一个问号赋值
                    ps.setString(1, "测试"+i);
                    // 为sql语句中第二个问号赋值
                    ps.setTimestamp(2, new Timestamp(new Date().getTime()));
                    //批量添加数据
                    ps.addBatch();
                    // 执行sql语句
                    ps.executeUpdate();
                    //批量提交
                    conn.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //执行完让线程直接进入等待
            begin.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //这里要主要了，当一个线程执行完 了计数要减一不然这个线程会被一直挂起
            //end.countDown()，这个方法就是直接把计数器减一的
            end.countDown();
        }

    }

}

