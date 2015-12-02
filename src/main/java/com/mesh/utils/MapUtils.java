package com.mesh.utils;

import java.util.Map;

/**
 * Created by G on 27/11/15.
 */
public class MapUtils {
    public static boolean isEmptyorNull(Map map) {
        if(map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }
}
