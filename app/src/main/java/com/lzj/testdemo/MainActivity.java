package com.lzj.testdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                initData();
                break;
            case R.id.bt2:
                break;
            case R.id.bt3:
                break;
        }
    }

    private void initData() {

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
        payRequest.payChannel = UnifyPayRequest.CHANNEL_ALIPAY;
        /*
         * 设置下单接口中返回的数据(appRequestData)
         * 只需要appPayRequest键值对应的json字符串）
         * */
        payRequest.payData = "";

        /*
         * 设置支付结果监听
         */
        payPlugin.setListener(new UnifyPayListener() {
            @Override
            public void onResult(String s, String s1) {

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


}
