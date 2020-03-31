package com.bcloud.server.common.pojo;

/**
 * @author zr
 * @description 全局常量
 * @date 27/12/2017
 */
public class GlobalConstant {

    /**
     * 生产环境默认配置目录
     */
    public static final String SYS_DEFAULT_CONFIG_PATH = System.getProperty("user.dir");

    /**
     * 生产环境文件默认下载目录
     */
    public static final String SYS_DEFAULT_DOWNLOAD_PATH = SYS_DEFAULT_CONFIG_PATH + "/temp/";

    /**
     * 用户默认头像的key
     */
    public static final String SYS_DEFAULT_AVATAR_KEY = "default_avatar";

    /**
     * 默认的普通角色key
     */
    public static final String SYS_DEFAULT_ROLE_KEY = "default_normal_role";

    /**
     * 默认的管理员角色key
     */
    public static final String SYS_ADMIN_ROLE_KEY = "default_admin_role";
}
