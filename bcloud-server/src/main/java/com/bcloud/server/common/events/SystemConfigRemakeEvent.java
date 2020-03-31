package com.bcloud.server.common.events;

import com.bcloud.server.common.sys.SystemDaoConf;
import com.bcloud.server.common.sys.SystemFileConf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author JianhuiChen
 * @description dao层的配置信息重置
 * @date 2020-03-28
 */
@Getter
@Setter
public class SystemConfigRemakeEvent extends ApplicationEvent {

    /**
     * 数据持久化类型
     */
    private final SystemDaoConf systemDaoConf;

    /**
     * 文件持久化类型
     */
    private final SystemFileConf systemFileConf;

    /**
     * Spring上下文信息
     */
    private final ApplicationContext applicationContext;

    public SystemConfigRemakeEvent(SystemDaoConf systemDaoConf, SystemFileConf systemFileConf,
                                   ApplicationContext context) {
        super(context);
        this.systemDaoConf = systemDaoConf;
        this.systemFileConf = systemFileConf;
        this.applicationContext = context;
    }

    @Override
    public String toString() {
        return "SystemConfigRemakeEvent{" +
                "systemDaoConf=" + systemDaoConf +
                ", systemFileConf=" + systemFileConf +
                '}';
    }
}
