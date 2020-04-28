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
public interface LFbAssetOrderDetailDao {

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbAssetOrderDetailSelect(@Param("map") Map<String, Object> map,
                                                               @Param("userid") String userid, @Param("type") String type);

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbTransferDetailSelect(@Param("map") Map<String, Object> map,
                                                             @Param("userid") String userid);

}
