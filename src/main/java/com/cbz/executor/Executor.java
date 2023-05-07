package com.cbz.executor;

import com.cbz.pojo.Configuration;
import com.cbz.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object parameter) throws Exception;

    void close();
}

