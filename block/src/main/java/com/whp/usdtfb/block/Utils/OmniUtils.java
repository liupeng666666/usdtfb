package com.whp.usdtfb.block.Utils;

import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbSysWalletDao;
import com.whp.usdtfb.block.Dao.FbWalletDao;
import com.whp.usdtfb.block.Interface.OmniInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/25 13:56
 * @descrpition :
 */
public class OmniUtils {
    @Autowired
    private FbSysWalletDao fbSysWalletDao;
    @Autowired
    private OmniInterface omniInterface;
    @Autowired
    private FbWalletDao fbWalletDao;
    @Value("${omni.height}")
    private int blockParseedCount;

    /**
     * USDT处理
     */
    // @Scheduled(initialDelay = 2000, fixedRate = 300000)
    public void usdtJob() {
        System.out.println("进入USDT处理");
        //获取系统归集地址
        String gj_address = null;
        Map<String, Object> map = fbSysWalletDao.FbSysWalletSelect(3);
        gj_address = map.get("address").toString();
        if (!omniInterface.vailedAddress(gj_address)) {
            return;
        }
        String height = RedisUtils.get("omni.height", 7);
        if (height == null || height.equals("")) {

        } else {
            blockParseedCount = Integer.parseInt(height);
        }

        if (blockParseedCount == 0) return;
        //获取钱包的高度
        int blockCount = omniInterface.getBlockCount();
        List<Map<String, Object>> list = fbWalletDao.FbWalletList(3);
        if (blockCount > blockParseedCount) {
            //还有没有处理完的区块， 继续处理
            int index = blockParseedCount + 1;
            while (index <= blockCount) {
                try {
                    if (omniInterface.parseBlock(index, gj_address, list)) {
                        index++;
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-------------已结束----------------");
        }
    }
}
