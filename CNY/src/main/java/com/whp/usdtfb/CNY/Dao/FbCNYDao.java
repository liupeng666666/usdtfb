package com.whp.usdtfb.CNY.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author : 张吉伟
 * @data : 2018/12/19 9:49
 * @descrpition :
 */
@Mapper
public interface FbCNYDao {

    /**
     * @param money
     */
    public void FbCnyInsert(@Param("money") BigDecimal money);
}
