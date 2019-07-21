package com.cdxt.batchinsert;

import com.cdxt.batchinsert.entity.TestInfo;
import com.cdxt.batchinsert.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchInsertApplicationTests {

    private final static int insertNum =1000000;

    @Autowired
    private TestService testService;

    /***
     * 多线程的批量插入数据也比较快  可以采用
     * 测试结果  100w数据  41s
     *          50w数据  18s
     *          10w数据  6s
     *          1w数据   2s
     *
     * ====================鲜明的对比==================
     *
     * 原生的批量插入数据也比较快  可以采用
     *  测试结果  100w数据  40s
     *           50w数据  21s
     *           10w数据  7s
     *           1w数据   1s
     *
     * 总结： 1w数据以内 两者都可以采用 时间差不多
     *       超过1w数据 采用多线程批量插入  时间要快很多。
     */
    @Test
    public void batchInsertByThread() {

        long startTime = System.currentTimeMillis();

        try {
            List<TestInfo> list = new LinkedList<>();

            TestInfo info = null;

            for (int i = 0; i < insertNum; i++) {

                info = new TestInfo();
                info.setName("test名称_" + i);
                info.setTime(new Date());

                list.add(info);
            }

            testService.batchInsertByThread(list);

            System.out.println("------Batch Insert Success------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("耗时(ms):" + (System.currentTimeMillis() - startTime)/1000+"s");
    }

    @Test
    public void batchInsert() {

        long startTime = System.currentTimeMillis();

        try {
            List<TestInfo> list = new LinkedList<>();

            TestInfo info = null;

            for (int i = 0; i < insertNum; i++) {

                info = new TestInfo();
                info.setName("test名称_" + i);
                info.setTime(new Date());

                list.add(info);
            }

            testService.batchInsert(list);

            System.out.println("------Batch Insert Success------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("耗时(ms):" + (System.currentTimeMillis() - startTime)/1000+"s");

    }

}
