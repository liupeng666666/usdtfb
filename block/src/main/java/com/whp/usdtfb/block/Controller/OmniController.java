package com.whp.usdtfb.block.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.block.Dao.FbWalletDao;
import com.whp.usdtfb.block.Interface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/10 21:40
 * @descrpition :
 */
@RestController
@RequestMapping("/omni")
public class OmniController {
    @Autowired
    private OmniInterface omniInterface;
    @Autowired
    private FbWalletDao fbWalletDao;
    @Autowired
    private EthInterface ethInterface;
    @Autowired
    private EtcInterface etcInterface;
    @Autowired
    private BthInterface bthInterface;
    @Autowired
    private LtcInterface ltcInterface;

    @PostMapping("/btc")
    public JSONObject btc(String userid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codeid", 3);
            map.put("userid", userid);
            Map<String, Object> maps = fbWalletDao.FbWalletSelect(map);
            if (maps == null) {
                String address = omniInterface.getNewAddress();

                map.put("address", address);
                fbWalletDao.FbWalletInsert(map);
                json.put("address", address);
            } else {
                json.put("address", maps.get("address"));
            }

            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;

    }

    @PostMapping("eth")
    public JSONObject EthAddress(String userid) {
        JSONObject json = ethInterface.new_address(userid);
        return json;
    }

    @PostMapping("etc")
    public JSONObject EtcAddress(String userid) {
        JSONObject json = etcInterface.new_address(userid);
        return json;
    }

    @PostMapping("/bch")
    public JSONObject bch(String userid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codeid", 4);
            map.put("userid", userid);
            Map<String, Object> maps = fbWalletDao.FbWalletSelect(map);
            if (maps == null) {
                String address = bthInterface.getNewAddress();

                map.put("address", address);
                fbWalletDao.FbWalletInsert(map);
                json.put("address", address);
            } else {
                json.put("address", maps.get("address"));
            }

            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;

    }

    @PostMapping("/ltc")
    public JSONObject ltc(String userid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codeid", 5);
            map.put("userid", userid);
            Map<String, Object> maps = fbWalletDao.FbWalletSelect(map);
            if (maps == null) {
                String address = ltcInterface.getNewAddress();

                map.put("address", address);
                fbWalletDao.FbWalletInsert(map);
                json.put("address", address);
            } else {
                json.put("address", maps.get("address"));
            }

            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;

    }

}
