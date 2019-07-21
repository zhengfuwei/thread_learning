package com.cdxt.threadpool.batchInsert;

import com.cdxt.utils.MysqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 原生的批量插入数据也比较快  可以采用
 * 测试结果  100w数据  40s
 *          50w数据  21s
 *          10w数据  7s
 *          1w数据   1s
 */
public class BatchInsert {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static int insertNum =10000;

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = MysqlConnection.getConn();
            if (conn!=null){
                conn.setAutoCommit(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 开始时间
        Long begin = new Date().getTime();
        System.out.println("插入开始时间："+sdf.format(new Date()));
        // sql
        String sql = "INSERT INTO test (name,time) VALUES (?,?)";
        try {
            // 比起st，pst会更好些
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);// 准备执行语句
            for (int i = 0; i <insertNum ; i++) {
                pst.setString(1,"测试"+i);
                pst.setTimestamp(2,new Timestamp(new Date().getTime()));
                // 添加执行SQL
                pst.addBatch();
            }
            // 执行操作
            pst.executeBatch();
            // 提交事务
            conn.commit();

            //关闭
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 结束时间
        Long end = new Date().getTime();
        System.out.println("插入结束时间："+sdf.format(new Date()));
        // 耗时
        System.out.println(""+insertNum+"条数据 总计耗时 : " + (end - begin) / 1000 + " s"+ "  插入完成");
    }
}
