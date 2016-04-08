package com.netease.channel.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthHelper {
    private static Logger logger = LoggerFactory.getLogger(AuthHelper.class);

    public static boolean isPermitted(String permission) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            logger.info("permission ============" + permission);
            logger.info("permission ============" + currentUser.isPermitted(permission));
            return currentUser.isPermitted(permission);
        }
        return false;
    }

}
