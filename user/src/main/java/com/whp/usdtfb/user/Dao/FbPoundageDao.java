package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/3/5 22:15
 * @descrpition :
 */
@Mapper
public interface FbPoundageDao {

    /**
     * @param currencyid
     * @return
     */
    public List<Map<String, Object>> FbPoundageSelect(@Param("currencyid") String currencyid);
}
