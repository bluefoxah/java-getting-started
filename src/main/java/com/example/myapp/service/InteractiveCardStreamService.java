package com.example.myapp.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author yuanghao.zyh
 * @Datte 2023/10/13
 **/

@Service
@Slf4j
public class InteractiveCardStreamService {

    @Value("${app.appKey}")
    private String appKey;

    @Value("${app.appSecret}")
    private String appSecret;


    @PostConstruct
    public void init() {
        startStream();
    }

    protected void startStream() {
        try {
            OpenDingTalkCallbackListener<PersistenceConnectionHolder.CardCallbackRequest, PersistenceConnectionHolder.CardCallbackResponse> yourListener
                    = new OpenDingTalkCallbackListener<PersistenceConnectionHolder.CardCallbackRequest, PersistenceConnectionHolder.CardCallbackResponse>() {
                @Override
                public PersistenceConnectionHolder.CardCallbackResponse execute(PersistenceConnectionHolder.CardCallbackRequest request) {
                    log.info("receive call back request = " + JSON.toJSONString(request));
                    // your code is here


                    return new PersistenceConnectionHolder.CardCallbackResponse();
                }
            };
            OpenDingTalkStreamClientBuilder
                    .custom()
                    .credential(new AuthClientCredential(appKey, appSecret))
                    .registerCallbackListener("/v1.0/card/instances/callback", yourListener)
                    .build().start();
        } catch (Exception e) {
            log.info("startStream get exception = " + e);
        }
    }
}
