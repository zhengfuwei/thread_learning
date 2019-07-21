package com.cdxt.batchinsert.service.Impl;

import com.cdxt.batchinsert.dao.TestDao;
import com.cdxt.batchinsert.entity.TestInfo;
import com.cdxt.batchinsert.service.TestService;
import com.cdxt.thread.TestBatchInsertThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * 测试单线程与多线程数据插入
 */
@Service
public class TestServiceImpl implements TestService {
    /**
     *
     */
    @Autowired
    private TestDao testDao;

    /***
     * 单线程数据插入
     * @param test
     */
    @Override
    public void batchInsert(List<TestInfo> test) {
        if (test == null || test.isEmpty()) {
            return;
        }
        List<TestInfo> tempList = new LinkedList<>();
        for (int i = 0; i < test.size(); i++) {

            tempList.add(test.get(i));

            if (i % 1000 == 0) {
                testDao.batchInsert(tempList);
                tempList.clear();
            }
        }
        testDao.batchInsert(tempList);
    }

    @Override
    public List<TestInfo> findAll() {
        return testDao.findAll();
    }

    /***
     * 多线程数据插入
     * @param list
     * @throws Exception
     */
    @Override
    @Transactional
    public void batchInsertByThread(List<TestInfo> list) throws Exception{
        if (list == null || list.isEmpty()) {
            return;
        }
        // 一个线程处理1000条数据
        int count = 5000;
        // 数据集合大小
        int listSize = list.size();
        // 开启的线程数
        int runSize = (listSize / count) + 1;
        // 存放每个线程的执行数据
        List<TestInfo> newList = null;
        // 创建一个线程池，数量和开启线程的数量一样
        ExecutorService executor = Executors.newFixedThreadPool(runSize);
        // 创建两个个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runSize);

        for (int i = 0; i < runSize; i++) {
            /* 计算每个线程执行的数据 */
            if ((i + 1) == runSize) {
                int startIdx = (i * count);
                int endIdx = list.size();

                newList = list.subList(startIdx, endIdx);
            } else {
                int startIdx = (i * count);
                int endIdx = (i + 1) * count;

                newList = list.subList(startIdx, endIdx);
            }
            TestBatchInsertThread thread = new TestBatchInsertThread(newList, begin, end, testDao);

            executor.execute(thread);
        }
        begin.countDown();
        end.await();

        executor.shutdown();
    }
}
