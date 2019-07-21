package com.cdxt.batchinsert.dao;

import com.cdxt.batchinsert.entity.TestInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDao {

    void insert(TestInfo test);

    List<TestInfo> findAll();

    void batchInsert(List<TestInfo> list);
}
