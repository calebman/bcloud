package com.bcloud.common.dao;


/**
 * @author JianhuiChen
 * @description 类之间的抽象转化类 多用于传输模型 -> 业务模型的转化
 * @date 2019/5/27
 */
public interface IConverter<A, B> {

    /**
     * 逆向转换
     *
     * @param a 类A
     * @return 类B
     */
    default B doForward(A a) {
        throw new RuntimeException("Not support do forward method");
    }

    /**
     * 正向转换
     *
     * @param b 类B
     * @return 类A
     */
    A doBackward(B b);
}
