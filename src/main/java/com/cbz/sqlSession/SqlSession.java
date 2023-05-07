package com.cbz.sqlSession;

import java.util.List;

public interface SqlSession {

     <E> List<E> selectList(String statementId,Object parameter);

     <T> T selectOne(String statementId,Object parameter);

}
