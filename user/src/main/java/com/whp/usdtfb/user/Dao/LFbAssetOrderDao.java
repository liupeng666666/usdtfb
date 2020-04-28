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
public interface LFbAssetOrderDao {

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbAssetOrderSelect(@Param("userid") String userid);

    /**
     * @param userid
     * @return
     */
    public Map<String, Object> LFbAssetOrderUsdtSelect(@Param("userid") String userid);

    /**
     * @param map
     */
    void LFbAssetOrderUpd1(@Param("map") Map<String, Object> map, @Param("userid") String userid);

    /**
     * @param map
     */
    void LFbAssetOrderUpd2(@Param("map") Map<String, Object> map, @Param("userid") String userid);

    /**
     * @param map
     */
    void LFbAssetOrderUpd3(@Param("map") Map<String, Object> map, @Param("userid") String userid);

    /**
     * @param map
     */
    void LFbAssetOrderUpd4(@Param("map") Map<String, Object> map, @Param("userid") String userid);

    /**
     * @param userid
     * @return
     */
    public int LFbAssetOrderCnt(@Param("currencyid") String currencyid, @Param("userid") String userid);

    /**
     * @param userid
     */
    void LFbAssetOrderIns(@Param("name") String name, @Param("surplus") BigDecimal surplus,
                          @Param("currencyid") String currencyid, @Param("userid") String userid);

    /**
     * @param userid
     */
    void LFbAssetTransferIns(@Param("from_currency") String from_currency, @Param("to_currency") String to_currency,
                             @Param("from_money") BigDecimal from_money, @Param("to_money") BigDecimal to_money,
                             @Param("userid") String userid, @Param("type") String type);

}
