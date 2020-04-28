package com.whp.usdtfb.socket.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.socket.Dao.FbSocketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 1:10
 * @descrpition :
 */
@Repository
public class WebSocketUtils implements WebSocketHandler {

    @Autowired
    private FbSocketDao fbSocketDao;
    private static final Map<String, WebSocketSession> users;


    static {
        users = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String id = webSocketSession.getUri().toString().split("ID=")[1];
        System.out.println("id:" + id);
        if (id != null) {
            users.put(id, webSocketSession);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            webSocketSession.sendMessage(new TextMessage(json.toString()));

        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonobject = JSONObject.parseObject(webSocketMessage.getPayload().toString());

            if (jsonobject.getInteger("state") == 1) {
                List<Map<String, Object>> list = fbSocketDao.FbSocketSelect(jsonobject);
                fbSocketDao.FbSocketUpdate(jsonobject);
                json.put("state", 1);
                json.put("socket", list);
                webSocketSession.sendMessage(new TextMessage(json.toString()));
            } else if (jsonobject.getInteger("state") == 2) {
                List<Map<String, Object>> list = fbSocketDao.FbSocketWeiDu(jsonobject);
                json.put("state", 2);
                json.put("socket", list);
                webSocketSession.sendMessage(new TextMessage(json.toString()));
            } else if (jsonobject.getInteger("state") == 3) {
                fbSocketDao.FbSocketInsert(jsonobject);
                if (users.get(jsonobject.getString("to")) == null) {
                    json.put("code", 301);
                    webSocketSession.sendMessage(new TextMessage(json.toString()));
                } else {
                    WebSocketSession session = users.get(jsonobject.getString("to"));
                    System.out.println("session:" + session);
                    if (session != null) {
                        session.sendMessage(new TextMessage(jsonobject.toString()));
                    } else {
                        json.put("code", 301);
                        webSocketSession.sendMessage(new TextMessage(json.toString()));
                    }

                }

            } else if (jsonobject.getInteger("state") == 4) {
                fbSocketDao.FbSocketUpdate(jsonobject);
                json.put("state", 4);
                json.put("socket", jsonobject);
                webSocketSession.sendMessage(new TextMessage(json.toString()));
            } else {
                json.put("code", 301);
                webSocketSession.sendMessage(new TextMessage(json.toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 301);
            webSocketSession.sendMessage(new TextMessage(json.toString()));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭：" + closeStatus);
        users.remove(getClientId(webSocketSession));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Integer getClientId(WebSocketSession session) {
        try {
            Integer clientId = (Integer) session.getAttributes().get("WEBSOCKET_USERID");
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
}
