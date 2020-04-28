package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/2/1 1:56
 * @descrpition :
 */
@Mapper
public interface LFbSysWalletDao {
    /**
     * @param codeid
     * @return
     */
    public Map<String, Object> FbSysWalletSelect(@Param("codeid") int codeid);
}
