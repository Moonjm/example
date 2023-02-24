package com.example.demo.util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomUtil {

    public static String getRootPath() {
        URL r = CustomUtil.class.getResource("");
        String path = r.getPath();
        return path.split("/build")[0];
    }

    public static String getRandName(String fileName) {
        String realFileName;
        String now = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
        realFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + now + fileName.substring(fileName.lastIndexOf("."));  //현재시간과 확장자 합치기
        return realFileName;
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
