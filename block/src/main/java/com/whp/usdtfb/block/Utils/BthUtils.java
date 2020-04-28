package com.whp.usdtfb.block.Utils;

import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbSysWalletDao;
import com.whp.usdtfb.block.Dao.FbWalletDao;
import com.whp.usdtfb.block.Interface.BtcInterface;
import com.whp.usdtfb.block.Interface.BthInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/25 13:56
 * @descrpition :
 */
@Component
public class BthUtils {
    @Autowired
    private FbSysWalletDao fbSysWalletDao;
    @Autowired
    private BthInterface bthInterface;
    @Autowired
    private FbWalletDao fbWalletDao;
    @Value("${bth.height}")
    private int blockParseedCount;

    /**
     * USDT处理
     */
    // @Scheduled(initialDelay = 2000, fixedRate = 300000)
    public void usdtJob() {
        System.out.println("进入BTH处理");
        //获取系统归集地址
        String gj_address = null;
        Map<String, Object> map = fbSysWalletDao.FbSysWalletSelect(4);
        if (map == null) {
            gj_address = bthInterface.getNewAddress();
            Map<String, Object> mapz = new HashMap<>();
            mapz.put("address", gj_address);
            mapz.put("codeid", 4);
            fbSysWalletDao.FbSysWalletInsert(mapz);
        } else {
            gj_address = map.get("address").toString();
        }

        if (!bthInterface.vailedAddress(gj_address)) {
            return;
        }
        String height = RedisUtils.get("BTH", 7);
        if (height == null || height.equals("")) {

        } else {
            blockParseedCount = Integer.parseInt(height);
        }

        if (blockParseedCount == 0) return;
        //获取钱包的高度
        int blockCount = bthInterface.getBlockCount();
        List<Map<String, Object>> list = fbWalletDao.FbWalletList(4);
        if (blockCount > blockParseedCount) {
            //还有没有处理完的区块， 继续处理
            int index = blockParseedCount + 1;
            while (index <= blockCount) {
                System.out.println("高度：" + index);
                try {
                    if (bthInterface.parseBlock(index, gj_address, list)) {
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
