package com.cdxt.thread;

import com.cdxt.batchinsert.dao.TestDao;
import com.cdxt.batchinsert.entity.TestInfo;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestBatchInsertThread implements Runnable {

    private TestDao testDao;

    /** 数据集合 */
    private List<TestInfo> list;
    /** 每个线程处理的起始数据 */
    private CountDownLatch begin;
    /** 每个线程处理的结束数据 */
    private CountDownLatch end;

    public TestBatchInsertThread() {
    }

    public TestBatchInsertThread(List<TestInfo> list, CountDownLatch begin, CountDownLatch end,
                                 TestDao testDao) {
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.testDao = testDao;
    }

    @Override
    public void run() {
        try {
            if (list != null && !list.isEmpty()) {
                testDao.batchInsert(list);
            }
            // 执行完让线程直接进入等待
            begin.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 当一个线程执行完 了计数要减一不然这个线程会被一直挂起
            end.countDown();
        }
    }
}
