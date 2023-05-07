package com.cbz.sqlSession;

import com.cbz.executor.Executor;
import com.cbz.pojo.Configuration;
import com.cbz.pojo.MappedStatement;

import java.util.List;

public class DefaultSqlSession implements SqlSession {


    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object parameter) {

        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        try {
            return executor.query(configuration,mappedStatement,parameter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {

        List<Object> list = this.selectList(statementId, parameter);
        if (list.size() == 1) {
            return (T) list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("返回结果太多");
        } else {
            return null;
        }

    }
}
