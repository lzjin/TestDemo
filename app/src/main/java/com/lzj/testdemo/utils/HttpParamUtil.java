package com.lzj.testdemo.utils;

import com.lzj.testdemo.R;
import com.lzj.testdemo.model.PostonRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 网络请求参数
 */
public class HttpParamUtil {

    private String getPostParam(int type, int currentEnvironment) {

//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Map<String, String> params = new HashMap<String, String>();
//        String orderId = getPostOrderId();
//        params.put("tid", mTerminerId.getText().toString());
//        params.put("msgSrc", mEditMsgSrc.getText().toString());
//        params.put("requestTimestamp", sdf.format(new Date()));
//        params.put("merOrderId", mMerOrderId.getText().toString());
//        params.put("totalAmount", mAmountText.getEditableText().toString());
//        params.put("mid", mMerchantId.getText().toString());
//        params.put("msgType", "qmf.order"); // 机器ip地址
//        params.put("instMid", "APPDEFAULT");
//        params.put("mobile", mMobileId.getText().toString());
//        params.put("msgId", getPostOrderId());
//        params.put("orderSource", "NETPAY");
//        params.put("merchantUserId", mMerchantUserId.getText().toString());
//        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
//        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
//        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");
//        params.put("sign", sign);
//
//
//        PostonRequest req = new PostonRequest();
//        req.tid = params.get("tid");
//        req.msgSrc = params.get("msgSrc");
//        req.requestTimestamp = params.get( sdf.format(new Date()) );
//        req.merOrderId = params.get("merOrderId");
//        req.totalAmount = params.get("totalAmount");
//        req.mid = params.get("mid");
//        req.msgType = params.get("msgType");
//        req.instMid = params.get("instMid");
//        req.mobile = params.get("mobile");
//        req.msgId = params.get("msgId");
//        req.orderSource = params.get("orderSource");
//        req.merchantUserId = params.get("merchantUserId");
//        req.sign = sign;
//        req.secureTransaction = params.get("secureTransaction");
//        req.srcReserve = params.get("srcReserve");
//        return req.toString();

        return  "";
    }

    /**
     * 随机生成   商户订单号
     * @return  11位
     */
    private String getPostOrderId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("3028"); //weixin
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
