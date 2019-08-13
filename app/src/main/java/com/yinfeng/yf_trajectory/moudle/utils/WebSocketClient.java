package com.yinfeng.yf_trajectory.moudle.utils;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：WebSocketClient
 * 创建人：liuguodong
 * 创建时间：2019/8/13 22:05
 * ============================================
 **/
class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("JWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.i("JWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.i("JWebSocketClient", "onError()");
    }
}
