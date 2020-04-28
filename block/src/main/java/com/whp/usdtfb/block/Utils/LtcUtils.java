package com.whp.usdtfb.block.Utils;

import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbSysWalletDao;
import com.whp.usdtfb.block.Dao.FbWalletDao;
import com.whp.usdtfb.block.Interface.LtcInterface;
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
public class LtcUtils {
    @Autowired
    private FbSysWalletDao fbSysWalletDao;
    @Autowired
    private LtcInterface ltcInterface;
    @Autowired
    private FbWalletDao fbWalletDao;
    @Value("${ltc.height}")
    private int blockParseedCount;

    /**
     * USDT处理
     */
    // @Scheduled(initialDelay = 2000, fixedRate = 300000)
    public void usdtJob() {
        System.out.println("进入LTC处理");
        //获取系统归集地址
        String gj_address = null;
        Map<String, Object> map = fbSysWalletDao.FbSysWalletSelect(5);
        if (map == null) {
            gj_address = ltcInterface.getNewAddress();
            Map<String, Object> mapz = new HashMap<>();
            mapz.put("address", gj_address);
            mapz.put("codeid", 5);
            fbSysWalletDao.FbSysWalletInsert(mapz);
        } else {
            gj_address = map.get("address").toString();
        }

        if (!ltcInterface.vailedAddress(gj_address)) {
            return;
        }
        String height = RedisUtils.get("LTC", 7);
        if (height == null || height.equals("")) {

        } else {
            blockParseedCount = Integer.parseInt(height);
        }

        if (blockParseedCount == 0) return;
        //获取钱包的高度
        int blockCount = ltcInterface.getBlockCount();
        List<Map<String, Object>> list = fbWalletDao.FbWalletList(4);
        if (blockCount > blockParseedCount) {
            //还有没有处理完的区块， 继续处理
            int index = blockParseedCount + 1;
            while (index <= blockCount) {
                System.out.println("高度：" + index);
                try {
                    if (ltcInterface.parseBlock(index, gj_address, list)) {
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
