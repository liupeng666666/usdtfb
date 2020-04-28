package com.whp.usdtfb.block.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/23 22:58
 * @descrpition :
 */
@Mapper
public interface FbUsdtDao {

    /**
     * @param map
     */
    public void FbUsdtInsert(Map<String, Object> map);

    /**
     * @param address
     * @return
     */
    public Map<String, Object> FbUsdtDan(@Param("address") String address);
}
