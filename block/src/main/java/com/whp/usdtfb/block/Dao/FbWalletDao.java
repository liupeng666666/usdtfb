package com.whp.usdtfb.block.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/19 2:51
 * @descrpition :
 */
@Mapper
public interface FbWalletDao {

    /**
     * @param map
     * @return
     */
    public Map<String, Object> FbWalletSelect(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbWalletInsert(Map<String, Object> map);

    /**
     * @param codeid
     * @return
     */
    public List<Map<String, Object>> FbWalletList(@Param("codeid") int codeid);
}
