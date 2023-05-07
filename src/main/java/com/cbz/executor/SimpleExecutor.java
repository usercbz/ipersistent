package com.cbz.executor;

import com.cbz.config.BoundSql;
import com.cbz.pojo.Configuration;
import com.cbz.pojo.MappedStatement;
import com.cbz.utils.ParameterMapping;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {


    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object parameter) throws Exception {

        //获取连接
        connection = configuration.getDataSource().getConnection();

        //获取preparedStatement对象
        String sql = mappedStatement.getSql();

        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterClass = Class.forName(parameterType);


        BoundSql boundSql = getBoundSql(sql);

        String finalSql = boundSql.getFinalSql();

        preparedStatement = connection.prepareStatement(finalSql);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();

        for (int i = 0; i < parameterMappingList.size(); i++) {

            ParameterMapping parameterMapping = parameterMappingList.get(i);
            //获取参数名
            String parameterName = parameterMapping.getContent();
            //反射
            Field declaredField = parameterClass.getDeclaredField(parameterName);
            //暴力访问
            declaredField.setAccessible(true);
            Object value = declaredField.get(parameter);
            //替换占位符
            preparedStatement.setObject(i + 1, value);

        }

        //执行sql,得到结果集
        resultSet = preparedStatement.executeQuery();

        //获取结果集对象的类字节码信息
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = Class.forName(resultType);


        //获取元数据信息
        ResultSetMetaData metaData = resultSet.getMetaData();
        //结果集的列值
        int columnCount = metaData.getColumnCount();

        List<E> list = new ArrayList<>();

        //处理返回结果集
        while (resultSet.next()) {

            //封装结果类对象

            //获取构造方法，创建结果类对象
            Object o = resultTypeClass.getConstructor().newInstance();

            //循环给结果类对象成员赋值
            for (int i = 0; i < columnCount; i++) {

                //列名
                String columnName = metaData.getColumnName(i + 1);
                //对应列的值
                Object columnValue = resultSet.getObject(i + 1);

                //反射 获取结果类成员变量信息
                Field declaredField = resultTypeClass.getDeclaredField(columnName);
                //暴力访问
                declaredField.setAccessible(true);
                //设置值
                declaredField.set(o, columnValue);

            }
            //封装成list
            list.add((E) o);

        }

        return list;
    }


    private BoundSql getBoundSql(String sql) {

        List<ParameterMapping> parameterMappingList = new ArrayList<>();

        String tempSql = sql;

        while (tempSql.contains("#{") && tempSql.contains("}")) {

            int beginIdx = tempSql.indexOf("#{");
            int endIdx = tempSql.indexOf("}");

            String substring = tempSql.substring(beginIdx, endIdx + 1);

            tempSql = tempSql.replace(substring, " ? ");

            ParameterMapping parameterMapping = new ParameterMapping(substring.substring(2, substring.length() - 1));

            parameterMappingList.add(parameterMapping);

        }

        return new BoundSql(tempSql, parameterMappingList);
    }

    @Override
    public void close() {

        try {
            if (resultSet != null) {
                this.resultSet.close();
            }
            if (preparedStatement != null) {

                this.preparedStatement.close();
            }
            if (connection != null) {
                this.connection.close();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
