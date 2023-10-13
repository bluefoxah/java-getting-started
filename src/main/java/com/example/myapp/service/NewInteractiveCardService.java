package com.example.myapp.service;

import com.aliyun.dingtalkcard_1_0.Client;
import com.aliyun.dingtalkcard_1_0.models.CreateAndDeliverHeaders;
import com.aliyun.dingtalkcard_1_0.models.CreateAndDeliverRequest;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 新的卡片接口 参见：https://open.dingtalk.com/document/orgapp/overview-card
 * @Author yuanghao.zyh
 * @Datte 2023/10/13
 **/

@Slf4j
@Service
public class NewInteractiveCardService {
    private Client client;
    private final AccessTokenService accessTokenService;

    @Value("${card.messageCardTemplateId001}")
    private String cardTemplateId;

    @Value("${robot.code}")
    private String robotCode;




    @Autowired
    public NewInteractiveCardService(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostConstruct
    public void init() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        client = new Client(config);
    }

    public void createAndDeliverToIMWithStream(String openConvId) {
        try {
            CreateAndDeliverHeaders headers
                    = new CreateAndDeliverHeaders();
            headers.xAcsDingtalkAccessToken = accessTokenService.getAccessToken();


            Map<String, String> cardDataMap = new HashMap<>(1);
            // your cardData

            CreateAndDeliverRequest.CreateAndDeliverRequestCardData cardData =
                    new CreateAndDeliverRequest.CreateAndDeliverRequestCardData();
            cardData.setCardParamMap(cardDataMap);
                /*
                // private data example

                Map<String, PrivateDataValue> privateDatas = new HashMap<>();

                PrivateDataValue privateDataValue = new PrivateDataValue();
                Map<String, String> privateData = new HashMap<>(1);
                // your private data

                privateDataValue.setCardParamMap(privateData);
                privateDatas.put("your staffId", privateDataValue);
                */
            CreateAndDeliverRequest.CreateAndDeliverRequestImGroupOpenDeliverModel imGroupOpenDeliverModel =
                    new CreateAndDeliverRequest.CreateAndDeliverRequestImGroupOpenDeliverModel()
                            .setRobotCode(robotCode);


            Map<String, String> lastMsgI18n = TeaConverter.buildMap(
                    new TeaPair("ZH_CN", "卡片"),
                    new TeaPair("EN_US", "card")
            );

            CreateAndDeliverRequest.CreateAndDeliverRequestImGroupOpenSpaceModel imGroupOpenSpaceModel =
                    new CreateAndDeliverRequest.CreateAndDeliverRequestImGroupOpenSpaceModel()
                            .setLastMessageI18n(lastMsgI18n)
                            .setSupportForward(true);

            String outTrackId = "myoutTrackId" + UUID.randomUUID().toString();

            CreateAndDeliverRequest request
                    = new CreateAndDeliverRequest()
                    .setOutTrackId(outTrackId)
                    .setCardTemplateId(cardTemplateId)
                    .setCardData(cardData)
//                        .setPrivateData(privateDatas)
                    .setCallbackType("stream")
                    .setImGroupOpenSpaceModel(imGroupOpenSpaceModel)
                    .setImGroupOpenDeliverModel(imGroupOpenDeliverModel)
                    .setOpenSpaceId(buildOpenSpaceId("group", openConvId));

            client.createAndDeliverWithOptions(request, headers, new RuntimeOptions());
        } catch (Exception e) {
            log.warn("createAndDeliverToIM get exception", e);
        }
    }

    /**
     *
     * 构建方法参考：
     * https://open.dingtalk.com/document/orgapp/delivery-card-interface 的接口调用说明
     * @param type
     * @param openConvId
     * @return
     */
    private String buildOpenSpaceId(String type, String openConvId) {
        if ("group".equals(type)) {
            return buildGroupOpenSpaceId(openConvId);
        }
        return "";
    }

    private String buildGroupOpenSpaceId(String openConvId) {
        return "dtv1.card//im_group." + openConvId;
    }
}
