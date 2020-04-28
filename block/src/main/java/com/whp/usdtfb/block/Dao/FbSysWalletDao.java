package com.whp.usdtfb.block.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/23 22:26
 * @descrpition :
 */
@Mapper
public interface FbSysWalletDao {

    /**
     * @param codeid
     * @return
     */
    public Map<String, Object> FbSysWalletSelect(@Param("codeid") int codeid);


    /**
     * @param map
     */
    public void FbSysWalletInsert(Map<String, Object> map);
}
