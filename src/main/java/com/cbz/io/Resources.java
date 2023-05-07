package com.cbz.io;

import java.io.InputStream;

public class Resources {

    /**
     * 加载配置文件成数据流存在内存中
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
