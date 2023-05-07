package com.cbz.config;

import com.cbz.utils.ParameterMapping;

import java.util.List;

public class BoundSql {

    private String finalSql;
    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String sql, List<ParameterMapping> parameterMappingList) {
        this.finalSql = sql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getFinalSql() {
        return finalSql;
    }

    public void setFinalSql(String finalSql) {
        this.finalSql = finalSql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
