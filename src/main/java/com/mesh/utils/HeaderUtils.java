package com.mesh.utils;


import org.apache.log4j.Logger;

/**
 * Created by G on 27/11/15.
 */
public class HeaderUtils {
    private final static String acceptedAgent = "agent-php";
    private final static Logger log = Logger.getLogger(HeaderUtils.class);

    public static boolean validate(String userAgent) {
        if (!acceptedAgent.equals(userAgent)) {
            log.error("Invalid User agent UA :" + userAgent);
            return false;
        }
        return true;
    }
}
