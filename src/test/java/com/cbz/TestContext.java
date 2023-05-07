package com.cbz;

import com.cbz.utils.ParameterMapping;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestContext {
    @Test
    public void test01() {

        String tempSql = "id=#{id} and name=#{name}";
        List<ParameterMapping> parameterMappingList = new ArrayList<>();

        while (tempSql.contains("#{") && tempSql.contains("}")) {

            int beginIdx = tempSql.indexOf("#{");
            int endIdx = tempSql.indexOf('}');


            String substring = tempSql.substring(beginIdx, endIdx + 1);

            tempSql = tempSql.replace(substring, "?");

            ParameterMapping parameterMapping = new ParameterMapping(substring.substring(2, substring.length() - 1));

            parameterMappingList.add(parameterMapping);

        }

        System.out.println(tempSql);
        System.out.println(parameterMappingList);
    }

    @Test
    public void  test03(){
        System.out.println("test03");
    }

    @Test
    public void test04(){

    }
}
