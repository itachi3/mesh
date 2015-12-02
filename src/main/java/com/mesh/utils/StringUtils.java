package com.mesh.utils;

/**
 * Created by G on 27/11/15.
 */
public class StringUtils {
    public static boolean isEmptyorNull(String str) {
        if(str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyorNull(String[] str) {
        if(str == null || str.length == 0) {
            return true;
        }
        return false;
    }
}
