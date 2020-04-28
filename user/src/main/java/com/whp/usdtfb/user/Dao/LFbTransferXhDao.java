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
public interface LFbTransferXhDao {

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> LFbTransferXhSelect(@Param("map") Map<String, Object> map,
                                                         @Param("userid") String userid);

}
