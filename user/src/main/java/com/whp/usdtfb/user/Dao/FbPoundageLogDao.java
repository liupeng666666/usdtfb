package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/3/5 23:41
 * @descrpition :
 */
@Mapper
public interface FbPoundageLogDao {

    public void FbPoundageLogInsert(Map<String, Object> map);

    /**
     * @param currencyid
     * @param userid
     * @return
     */
    public List<Map<String, Object>> FbPoundageSelect(@Param("currencyid") String currencyid, @Param("userid") String userid);
}
