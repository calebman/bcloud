package com.bcloud.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author JianhuiChen
 * @description 基础实体
 * @date 2020-03-25
 */
@Getter
@Setter
public class BaseEntity {

    public BaseEntity() {
        this.creatAt = new Date();
        this.updateAt = new Date();
    }

    @Schema(title = "唯一标志")
    private String id;

    @Schema(title = "创建时间")
    private Date creatAt;

    @Schema(title = "更新时间")
    private Date updateAt;
}
