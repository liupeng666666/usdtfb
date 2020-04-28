package com.whp.usdtfb.block.Utils;

import com.whp.usdtfb.block.Interface.EtcInterface;
import com.whp.usdtfb.block.Interface.EthInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : 张吉伟
 * @data : 2019/1/28 18:01
 * @descrpition :
 */
@Component
public class EtcUtils {
    @Autowired
    private EtcInterface etcInterface;

    //@Scheduled(initialDelay = 2000, fixedRate = 300000)
    public void usdtJob() {
        etcInterface.XunHuan();
    }
}
