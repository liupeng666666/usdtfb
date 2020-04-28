package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:33
 * @descrpition :
 */
@Mapper
public interface LFbRechargeDao {

    /**
     * @param map
     * @return
     */
    public List<Map<String, Object>> LFbRechargeSelect(@Param("userid") String userid,
                                                       @Param("map") Map<String, Object> map, @Param("page") int page,
                                                       @Param("num") int num);

    /**
     * @param map
     * @return
     */
    public int LFbRechargeCnt(@Param("userid") String userid, @Param("map") Map<String, Object> map);

}
