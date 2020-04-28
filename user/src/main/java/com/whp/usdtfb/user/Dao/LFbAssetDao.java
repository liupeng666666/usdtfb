package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:33
 * @descrpition :
 */
@Mapper
public interface LFbAssetDao {

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbAssetSelect(@Param("userid") String userid);

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbAssetUsdtSelect(@Param("userid") String userid);

    /**
     * @param map
     */
    public void LFbAssetUsdtInsert(Map<String, Object> map);

}
