package com.lzj.testdemo.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;


import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.lzj.testdemo.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "test";
    private IWXAPI api;
    private Handler handler;
    private boolean flag = true;
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        setContentView(R.layout.activity_wxpay_entry);
        button = (Button) findViewById(R.id.button);
        handler = new Handler();
        api = WXAPIFactory.createWXAPI(this, UnifyPayPlugin.getInstance(this).getAppId());
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("test","onPayFinish, errCode = " + resp.errCode);
        Log.d("test","resperrStr = " + resp.errStr);
        Log.d("test","openid = "+resp.openId);

//        if (flag) {
//            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//                flag = false;
//                if (resp.errCode == 0) {
//                    Log.d("test","支付成功");
//                } else {
//                    Log.d("test","支付失败 ");
//                }
//                this.finish();
//            }
//        }

        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            Log.d(TAG,"onResp   ---   " + extraData);
            String msg = "onResp   ---   errStr：" + resp.errStr + " --- errCode： " + resp.errCode + " --- transaction： "
                    + resp.transaction + " --- openId：" + resp.openId + " --- extMsg：" + launchMiniProResp.extMsg;
            Log.d(TAG,msg);
            button.setText(msg);

            UnifyPayPlugin.getInstance(this).getWXListener().onResponse(this, resp);
        }
    }
}