package com.cdxt.threadpool.test;

import com.cdxt.utils.MysqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdxt.threadpool.threadpool.InsertData.exec;

public class TestInsertData {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static int insertNum =50000;
    public static void main(String[] args) {
//        test1();
//        testMysqlConnection();
    }

    private static void testMysqlConnection() {

        try {
            Connection conn = MysqlConnection.getConn();
            if (conn!=null){
                System.out.println("mysql连接成功!");
            }
            String sql = "insert into test (name,time) values(?,?)"; // 生成一条sql语句
            // 创建一个Statment对象
            PreparedStatement ps = conn.prepareStatement(sql);

            // 为sql语句中第一个问号赋值
            ps.setString(1, "测试");
            // 为sql语句中第二个问号赋值
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            // 执行sql语句
            ps.executeUpdate();
            // 关闭数据库连接对象
            conn.close();
            System.out.println("数据插入完毕！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 测试数据
     */
    private static void test1() {
        List<String> list = new ArrayList<>();
        //数据越大线程越多
        for (int i = 0; i < 30000; i++) {
            list.add("hello"+i);
        }
        try {
            Long beginTime = new Date().getTime();
            exec(list);
            Long timeOut=new Date().getTime()-beginTime;
            System.out.println("3w数据插入耗时："+timeOut/1000+"s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
