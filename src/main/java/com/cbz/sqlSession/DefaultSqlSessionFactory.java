package com.cbz.sqlSession;

import com.cbz.executor.Executor;
import com.cbz.executor.SimpleExecutor;
import com.cbz.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {

        Executor simpleExecutor = new SimpleExecutor();

        return new DefaultSqlSession(configuration,simpleExecutor);
    }
}
