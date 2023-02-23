package com.example.demo.util;

import java.net.URL;

public class CustomUtil {

    public static String getRootPath() {
        URL r = CustomUtil.class.getResource("");
        String path = r.getPath();
        return path.split("/build")[0];
    }
}
