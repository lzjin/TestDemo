package com.lzj.testdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzj.testdemo.activity.DemoActivity;
import com.lzj.testdemo.bean.WXPayBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;
    @BindView(R.id.bt4)
    Button bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ToastUtils.init(getApplication());
        requestPermission();
    }


    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3,R.id.bt4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                PayData( UnifyPayRequest.CHANNEL_WEIXIN);
                break;
            case R.id.bt2:
                PayData( UnifyPayRequest.CHANNEL_ALIPAY);
                break;
            case R.id.bt3:
                PayData( UnifyPayRequest.CHANNEL_UMSPAY);
                break;
            case R.id.bt4:
                Intent intent = new Intent(this, DemoActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void PayData(String channel) {

        /*
         * 获取综合支付SDK对象
         * 参数：Context
         * */
        UnifyPayPlugin payPlugin = UnifyPayPlugin.getInstance(this);
        /*
         * 新建统一支付请求类
         *
         */
        UnifyPayRequest payRequest = new UnifyPayRequest();

        /*
         * 初始化支付渠道(如：微信支付)
         * //请求返回的支付唤起参数
         * */
        payRequest.payChannel = channel;
        /*
         * 设置下单接口中返回的数据(appRequestData)
         * 只需要appPayRequest键值对应的json字符串）
         * */
        WXPayBean bean=new WXPayBean();
        bean.setAppid("wxa0c4da2b17dd9c8c");
        bean.setNoncestr("8i9hddnmipu40m3w3hsssqfx3k7u6w92");
        bean.setOutTradeNo("873067993339265024");
        bean.setPackageX("Sign=WXPay");
        bean.setPartnerid("1482006732");
        bean.setSign("D07F6525628FF6C96742F6127F7ECFD3");
        bean.setPrepayid("wx2017061210342313520f617b0631712346");
        bean.setTimestamp(1497234853);

        Gson gson=new Gson();
        String json=gson.toJson(bean);

        Log.i("test","--------------------="+json);
        payRequest.payData = json;



        /*
         * 设置支付结果监听
         */
        payPlugin.setListener(new UnifyPayListener() {
            @Override
            public void onResult(String s, String s1) {
                ToastUtils.show(s+"="+s1);
                Log.e("test","--------------1="+s);
                Log.e("test","--------------2="+s1);
            }

//            @Override
//            public void onResult(int resultCode, String resultInfo) {
//                /*
//                 * 根据返回的支付结果进行处理
//                 */
//                if (resultCode = “0000”) {
//                    //支付成功
//                } else {
//                    //其他
//                }
//            }
        });


        //开始支付
        payPlugin.sendPayRequest(payRequest);

    }


    /**
     * 允许权限成功后触发
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        ToastUtils.show("授权成功");
    }

    /**
     * 禁止权限后触发
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.show("您已拒绝相关权限，可到设置里自行开启");
    }

    /**
     * 请求授权
     */
    @AfterPermissionGranted(1001)
    private void requestPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //授权成功后
            ToastUtils.show("授权成功");
        } else {
            //没有权限的话，先提示，点确定后弹出系统的授权提示框
            EasyPermissions.requestPermissions(this, "为了更好的用户体验需要获取以下权限", 1001, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
