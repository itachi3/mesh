package com.mesh.utils;

import java.util.List;

/**
 * Created by G on 01/12/15.
 */
public class ListUtils {
    public static boolean isEmptyorNull(List str) {
        if(str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }
}
