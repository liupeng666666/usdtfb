package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2018/12/31 22:33
 * @descrpition :
 */
@Mapper
public interface LFbSellDao {

    /**
     * @param map
     * @return
     */
    public List<Map<String, Object>> LFbSellSelect(@Param("map") Map<String, Object> map, @Param("page") int page, @Param("num") int num);

    /**
     * @param map
     * @return
     */
    public int LFbSellCount(@Param("map") Map<String, Object> map);

}
