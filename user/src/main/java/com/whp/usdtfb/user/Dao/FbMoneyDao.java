package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.security.PermitAll;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/24 14:16
 * @descrpition :
 */
@Mapper
public interface FbMoneyDao {

    public Map<String, Object> FbMoneySelect(@Param("currencyid") String currencyid, @Param("userid") String userid, @Param("lock") String lock);

    /**
     * @param map
     */
    public void FbMoneyUpdate(Map<String, Object> map);

    /**
     * @param currencyid
     * @param userid
     */
    public void FbMoneyInsert(@Param("currencyid") String currencyid, @Param("userid") String userid);
}
