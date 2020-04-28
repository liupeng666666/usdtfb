package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Dao.FbUserDao;
import com.whp.usdtfb.user.Interface.FbUserInterface;
import com.whp.usdtfb.user.Interface.LFbSellInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2018/12/31 22:37
 * @descrpition :
 */
@RestController
@RequestMapping("LFbSell")
public class LFbSellController {

    @Autowired
    private LFbSellInterface lfbSellInterface;
    @Autowired
    private FbUserDao fbUserDao;

    @PostMapping("LFbSellSelect")
    public JSONObject LFbSellSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        JSONObject json = lfbSellInterface.LFbSellSelect(map);
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        if (pid == null) {
            json.put("state", 0);
        } else {
            Map<String, Object> maps = fbUserDao.FbUserSelect(pid);
            if (maps == null) {
                json.put("state", 0);
            } else if (maps.containsKey("contract")) {
                json.put("state", maps.get("contract"));
            } else {
                json.put("state", 0);
            }
        }

        return json;
    }

}
