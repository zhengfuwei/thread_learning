package com.cdxt.batchinsert.service;

import com.cdxt.batchinsert.entity.TestInfo;

import java.util.List;

public interface TestService {

    void batchInsert(List<TestInfo> test);

    List<TestInfo> findAll();

    void batchInsertByThread(List<TestInfo> list) throws Exception;
}
