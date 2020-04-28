package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/26 15:01
 * @descrpition :
 */
@Mapper
public interface FbSellDao {

    /**
     * @param map
     */
    public void FbSellInsert(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public List<Map<String, Object>> FbSellSelect(@Param("map") Map<String, Object> map, @Param("page") int page, @Param("num") int num);

    /**
     * @param map
     * @return
     */
    public int FbSellCount(@Param("map") Map<String, Object> map);

    /**
     * @param pid
     * @return
     */
    public Map<String, Object> FbSellDan(@Param("pid") String pid);

    /**
     * @param map
     */
    public void FbSellUpdate(Map<String, Object> map);

    public int FbSellPassword(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbSellDanUpdate(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbSellStateUpdate(Map<String, Object> map);
}
