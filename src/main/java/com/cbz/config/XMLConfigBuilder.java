package com.cbz.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.cbz.io.Resources;
import com.cbz.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 使用dom4j和xpath解析配置文件
     *
     * @param inputStream
     * @return
     */
    public Configuration parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        List<Element> list = rootElement.selectNodes("//property");

        Properties properties = new Properties();

        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        //创建数据源对象
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driverClassName"));
        druidDataSource.setUrl(properties.getProperty("url"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));

        //封装到Configuration中
        configuration.setDataSource(druidDataSource);

        //Mapper配置文件的解析
        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);

            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);

        }

        return configuration;
    }
}
