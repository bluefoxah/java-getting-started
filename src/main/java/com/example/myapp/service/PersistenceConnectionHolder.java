package com.example.myapp.service;

import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 长连接holder, 维护和钉钉开放平台外联网关的Stream长连接
 * @author dingtalk
 */
public class PersistenceConnectionHolder {


    //main

    /**
     * 卡片回调请求
     */
    @Data
    public static class CardCallbackRequest{

        /**
         * 回调类型,actionCallback
         */
        private String type;
        /**
         * 发起事件回调卡片的ID
         */
        private String outTrackId;
        /**
         * 回调内容,ActionCallbackContent的jsonString格式
         */
        private String content;
        /**
         * 卡片归属的企业id
         */
        private String corpId;
        /**
         * 用户userId
         */
        private String userId;
        /**
         * 回调按钮的内容信息
         */
        public static class ActionCallbackContent {
            private PrivateCardActionData cardPrivateData;

            public static class PrivateCardActionData {
                //点击按钮的id
                private List<String> actionIds;

                //给按钮配置的额外参数
                private Map<String, Object> params;

            }
        }
    }

    /**
     * 卡片回调响应
     */
    @Data
    public static class CardCallbackResponse {

        //卡片公有数据
        private CardDataDTO cardData;

        //触发回调用户的私有数据
        private CardDataDTO privateCardData;

        @Data
        public static class CardDataDTO{

            //卡片参数
            private Map<String, String> cardParamMap;
        }
    }

}
