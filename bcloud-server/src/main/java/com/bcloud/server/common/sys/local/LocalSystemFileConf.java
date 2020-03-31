package com.bcloud.server.common.sys.local;

import com.bcloud.server.common.sys.SystemFileConf;
import com.bcloud.server.common.valid.Path;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.beans.BeanMap;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 基于本地磁盘文件持久化配置
 * @date 2020-03-29
 */
@Getter
@Setter
@ToString
public class LocalSystemFileConf implements SystemFileConf {

    /**
     * 文件存储在本地磁盘的路径
     */
    @Path(message = "文件存储的本地磁盘路径不合法")
    @NotBlank(message = "文件存储的本地磁盘路径不可为空")
    private String folder;

    @Override
    public StorageType getStorageType() {
        return StorageType.LOCAL;
    }

    @Override
    public Map<String, Object> getProperties() {
        return BeanMap.create(this);
    }

    @Override
    public void test() throws RuntimeException {

    }
}
