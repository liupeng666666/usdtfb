package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/22 22:37
 * @descrpition :
 */
@Mapper
public interface FbCurrencyDao {

    public List<Map<String, Object>> FbCurrencySelect();

    /**
     * @param name
     * @return
     */
    public Map<String, Object> FbCurrencyDan(@Param("name") String name);

    /**
     * @param pid
     * @return
     */
    public Map<String, Object> FbCurrencyUsdt(@Param("pid") String pid);
}
