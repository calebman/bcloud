package com.bcloud.server.modules.file;

import com.bcloud.common.file.FileSaveResult;
import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.impl.local.LocalFileService;
import com.bcloud.common.file.impl.local.LocalFileServiceConfig;
import com.bcloud.common.util.DelayedTaskScheduled;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author JianhuiChen
 * @description 本地文件存储服务测试
 * @date 2020-03-23
 */
@SpringBootTest
@Slf4j
public class LocalFileServiceTests {

    private static IFileService fileService;

    @BeforeAll
    public static void injectionFileService() {
        LocalFileServiceConfig localFileServiceConfig = new LocalFileServiceConfig();
        localFileServiceConfig.setFolder("/Users/chee/Desktop/test-file-folder/");
        fileService = new LocalFileService(localFileServiceConfig);
    }

    @Test
    public void saveFile() {
        FileSaveResult result = fileService.saveAsUrl("https://port.chenjianhui.site/static/images/main/header.jpg");
        log.info("Save result {}", fileService.get(result.getKey()));
        DelayedTaskScheduled.schedule(() -> fileService.remove(result.getKey()), 60, TimeUnit.SECONDS);
        try {
            Thread.sleep(65 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
