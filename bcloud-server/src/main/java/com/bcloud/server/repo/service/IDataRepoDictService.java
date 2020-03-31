package com.bcloud.server.repo.service;


import com.bcloud.server.repo.out.RepoDictVO;
import com.bcloud.server.repo.in.RepoDictDTO;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库字典集合操作服务接口
 * @date 2020-03-26
 */
public interface IDataRepoDictService {

    /**
     * 创建字典集合
     *
     * @param belongRepo  所属数据仓库
     * @param repoDictDTO 字典集合传输层实体
     * @return 操作结果
     */
    RepoDictVO createDict(String belongRepo, RepoDictDTO repoDictDTO);

    /**
     * 编辑字典集合信息
     *
     * @param dictId      字典集合ID
     * @param repoDictDTO 字典集合传输层实体
     * @return 操作结果
     */
    RepoDictVO updateDict(String dictId, RepoDictDTO repoDictDTO);

    /**
     * 删除字典集合
     *
     * @param dictId 字典集合ID
     */
    void deleteDict(String dictId);

    /**
     * 查询指定字典集合
     *
     * @param dictId 字典集合ID
     * @return 字典集合信息
     */
    RepoDictVO getDictById(String dictId);

    /**
     * 查询字典集合信息
     *
     * @param belongRepo 所属数据仓库
     * @return 字典集合信息列表
     */
    List<RepoDictVO> getDictsByRepo(String belongRepo);
}
