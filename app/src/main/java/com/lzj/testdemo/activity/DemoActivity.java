package com.lzj.testdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaums.pppay.unify.SocketFactory;
import com.chinaums.pppay.unify.UnifyMd5;
import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;
import com.lzj.testdemo.R;
import com.lzj.testdemo.httpserver.ApiService;
import com.lzj.testdemo.httpserver.RetrofitClientManager;
import com.lzj.testdemo.model.PostonRequest;
import com.lzj.testdemo.model.WXRequest;
import com.lzj.testdemo.views.ClearEditText;
import com.unionpay.UPPayAssistEx;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 测试支付
 */
public class DemoActivity extends Activity implements UnifyPayListener {
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.widget38)
    RelativeLayout widget38;
    @BindView(R.id.tv_billsMID)
    TextView tvBillsMID;
    @BindView(R.id.merchantId)
    ClearEditText mMerchantId;//出账商户号
    @BindView(R.id.tv_billsTID)
    TextView tvBillsTID;
    @BindView(R.id.merchantUserId)
    ClearEditText mMerchantUserId;//出账商户用户号
    @BindView(R.id.tv_agentMer)
    TextView tvAgentMer;
    @BindView(R.id.agentMerchantId)
    ClearEditText agentMerchantId;
    @BindView(R.id.tv_merOrderID)
    TextView tvMerOrderID;
    @BindView(R.id.merOrderID)
    ClearEditText mMerOrderId;//商户订单号
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.amount)
    ClearEditText mAmountText; //交易金额
    @BindView(R.id.tv_mobileId)
    TextView tvMobileId;
    @BindView(R.id.mobileId)
    ClearEditText mMobileId;//用户手机号
    @BindView(R.id.tv_notifyUrlId)
    TextView tvNotifyUrlId;
    @BindView(R.id.notifyUrl)
    ClearEditText notifyUrl;
    @BindView(R.id.tv_terminerid)
    TextView tvTerminerid;
    @BindView(R.id.terminerid)
    ClearEditText mTerminerId;//终端号
    @BindView(R.id.tv_msgsrc)
    TextView tvMsgsrc; //消息来源
    @BindView(R.id.edit_msgsrc)
    ClearEditText mEditMsgSrc; //消息来源
    @BindView(R.id.tv_src_reserve)
    TextView tvSrcReserve;
    @BindView(R.id.edit_src_reserve)
    ClearEditText mEditSrcReserve;
    @BindView(R.id.tv_environment)
    TextView tvEnvironment;
    @BindView(R.id.spinner_environment)
    Spinner mSpinnerEnvironMent;
    @BindView(R.id.secure_transaction_true)
    RadioButton secureTransactionTrue;
    @BindView(R.id.secure_transaction_false)
    RadioButton secureTransactionFalse;
    @BindView(R.id.radio_group_secure_transaction)
    RadioGroup rgSecureTransaction;
    //支付方式选择
    @BindView(R.id.radio_group)
    RadioGroup rgvalue;
    @BindView(R.id.poston_pay)
    RadioButton zfbtype;
    @BindView(R.id.weixin_pay)
    RadioButton wxtype;
    @BindView(R.id.alibaba_pay)
    RadioButton alipay;
    @BindView(R.id.cloud_quick_pay)
    RadioButton cloudQuickPay;//云闪付
    /**
     * 分账信息
     */
    @BindView(R.id.cb_division)
    CheckBox cbDivision;//是否开启分帐复选框
    @BindView(R.id.tv_platform_amount)
    TextView tvPlatformAmount;
    @BindView(R.id.platform_amount)
    ClearEditText mPlatformAmount;//分帐平台商户分账金额
    @BindView(R.id.tv_sub_mid)
    TextView tvSubMid;
    @BindView(R.id.sub_mid)
    ClearEditText mSubMid; //分帐子商户号
    @BindView(R.id.tv_sub_mer_orderId)
    TextView tvSubMerOrderId;
    @BindView(R.id.sub_mer_orderId)
    ClearEditText mSubMerOrderId;//分帐商户子订单号
    @BindView(R.id.tv_sub_total_amount)
    TextView tvSubTotalAmount;
    @BindView(R.id.sub_total_amount)
    ClearEditText mSubTotalAmount; //分帐子商户分账金额
    @BindView(R.id.bt_add_division_info)
    ImageView addDivisionInfo;//分帐添加分帐参数按钮
    @BindView(R.id.et_division_info)
    ClearEditText divisionInfo;//分帐参数展示
    @BindView(R.id.layout_division)
    LinearLayout layoutDivision; //分账布局
    @BindView(R.id.linear_center1)
    LinearLayout linearCenter1;
    @BindView(R.id.btn_order_pay)
    Button btnOrderPay;
    @BindView(R.id.tv_callback)
    TextView mPayResult;
    @BindView(R.id.linear_bottom2)
    LinearLayout linearBottom2;

    private final static String TAG = "testz";
    private ProgressDialog dialog;
    private int typetag = 0;
    private String mNotifyUrl = "http://172.16.26.178:8080/connectDemo/NotifyOperServlet";
    private final int TYPE_POSTON = 0;
    private final int TYPE_WEIXIN = 1;
    private final int TYPE_ALIPAY = 2;
    /**
     * 云闪付
     */
    private final int TYPE_CLOUD_QUICK_PAY = 3;

    private final int ENV_TEST_ONE = 0;
    private final int ENV_TEST_TWO = 1;
    private final int ENV_NATIVE = 2;
    private final int ENV_PRODUCT = 3;
    /**
     * UAT支付环境
     */
    private final int ENV_ALIPAY_UAT = 4;
    private int mCurrentEnvironment = ENV_PRODUCT;
    private String mMd5SecretKey;
    private SharedPreferences mSharedPreferences;
    private Activity mActivity = null;
    /**
     * 分帐相关信息集合
     */
    private JSONArray divisionInfosArray = new JSONArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        initView();
        initListener();
    }



    /**
     * 初始化
     */
    private void initView() {
        layoutDivision.setVisibility(View.GONE);
        mSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        mMerOrderId.setText(getOrderId());
        //默认环境设置
        mSpinnerEnvironMent.setSelection(0, true);
        mCurrentEnvironment = ENV_PRODUCT;
        switchParam(typetag, mCurrentEnvironment);
        UnifyPayPlugin.getInstance(this).setListener(this);
        mActivity = this;
    }

    /**
     * 支付方式与环境切换
     */
    private void initListener() {
        //支付方式
        rgvalue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mCurrentEnvironment == ENV_ALIPAY_UAT && checkedId != alipay.getId()) {
                    group.check(alipay.getId());
                    Toast.makeText(getApplicationContext(), "UAT环境只支持支付宝", Toast.LENGTH_LONG).show();
                    return;
                }
                switch (checkedId) {
                    case R.id.weixin_pay:
                        typetag = 1;//微信
                        break;
                    case R.id.poston_pay:
                        typetag = 0;//poston
                        break;
                    case R.id.alibaba_pay:
                        typetag = 2;//zhifubao
                        break;
                    case R.id.cloud_quick_pay:
                        typetag = TYPE_CLOUD_QUICK_PAY;//云闪付;
                        break;
                }
                //配置对应支付参数
                switchParam(typetag, mCurrentEnvironment);
            }
        });
        //支付环境切换
        mSpinnerEnvironMent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private int lastPostion;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long l) {
                switch (postion) {
                    case 0:
                        mCurrentEnvironment = ENV_PRODUCT;
                        break;
                    case 1:
                        mCurrentEnvironment = ENV_TEST_ONE;
                        break;
                    case 2:
                        mCurrentEnvironment = ENV_TEST_TWO;
                        break;
                    case 3:
                        if (typetag != TYPE_ALIPAY) {
                            mSpinnerEnvironMent.setSelection(lastPostion, true);
                            Toast.makeText(getApplicationContext(), "UAT环境只支持支付宝", Toast.LENGTH_LONG).show();
                            return;
                        }
                        mCurrentEnvironment = ENV_ALIPAY_UAT;
                        break;
                }
                lastPostion = postion;

                switchParam(typetag, mCurrentEnvironment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 事件
     */
    @OnClick({R.id.cb_division, R.id.bt_add_division_info, R.id.btn_order_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_division://是否分账  显示分账布局
                if(cbDivision.isChecked()){
                    layoutDivision.setVisibility(View.VISIBLE);
                }else {
                    layoutDivision.setVisibility(View.GONE);
                }
                break;
            case R.id.bt_add_division_info://添加分账参数
                if (TextUtils.isEmpty(mSubMerOrderId.getText()) && TextUtils.isEmpty(mSubMid.getText())
                        && TextUtils.isEmpty(mSubTotalAmount.getText())) {
                    Toast.makeText(getApplication(), "商户分帐子信息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("mid", mSubMid.getText());
                    object.put("merOrderId", mSubMerOrderId.getText());
                    object.put("totalAmount", mSubTotalAmount.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                divisionInfosArray.put(object);
                divisionInfo.setText(divisionInfosArray.toString());
                break;
            case R.id.btn_order_pay://网络请求
                //new GetPrepayIdTask().execute();
                httpRxPost();
                break;
        }
    }

    /**
     * 支付方式对应参数 配置
     * @param type 支付方式
     * @param currentEnvironment 环境
     */
    private void switchParam(int type, int currentEnvironment) {
        Log.i(TAG, "----------------type=" + type + ", currentEnvironment=" + currentEnvironment);
        if (type == TYPE_POSTON) {
            switch (currentEnvironment) {
                case ENV_TEST_ONE:
                case ENV_TEST_TWO:
                case ENV_NATIVE:
                    mEditMsgSrc.setText("ERP_SCANPAY");
                    mMerchantId.setText("898460107800170");
                    mTerminerId.setText("00000170");
                    mMerOrderId.setText(getPostOrderId());
                    mMd5SecretKey = "EahB2xfpCCpaYtKw2yCWzcTfChTxXEYKCGwBEaMcDKbEHCpE";
                    break;
                case ENV_PRODUCT:
                    mEditMsgSrc.setText("ERP_SCANPAY");
                    mMerchantId.setText("898310058124024");
                    mTerminerId.setText("00000001");
                    mMerOrderId.setText(getPostOrderId());
                    mMd5SecretKey = "3ypmTzxdXhFty7HCrZynehjcjdcaAb3HDRwJQpTFYZfjWHEZ";
                    break;
            }
        } else if (type == TYPE_WEIXIN) {
            switch (currentEnvironment) {
                case ENV_TEST_ONE:
                    mEditMsgSrc.setText("ERP_SCANPAY");
                    mMerchantId.setText("898310052114003");//898310060514001
                    mTerminerId.setText("00000001");//"12345678"
                    mMerOrderId.setText(getCommonOrder("3028"));//"3194"//getProOrderId()
                    mSubMerOrderId.setText(getCommonOrder("3028"));
                    mMd5SecretKey = "EahB2xfpCCpaYtKw2yCWzcTfChTxXEYKCGwBEaMcDKbEHCpE";//"1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm";
                    break;
                case ENV_TEST_TWO:
                    mEditMsgSrc.setText("WWW.TEST.COM");//"NETPAY"
                    mMerchantId.setText("898310052114003");//898310060514001
                    mTerminerId.setText("00000001");//"12345678"
                    mMerOrderId.setText(getCommonOrder("3194"));//getProOrderId()
                    mSubMerOrderId.setText(getCommonOrder("3194"));
                    mMd5SecretKey = "fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR";//"1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm";
                    break;
                case ENV_NATIVE:
                    mEditMsgSrc.setText("ERP_SCANPAY");
                    mMerchantId.setText("898310060514001");
                    mTerminerId.setText("88880001");
                    mMerOrderId.setText(getPostOrderId());
                    mMd5SecretKey = "EahB2xfpCCpaYtKw2yCWzcTfChTxXEYKCGwBEaMcDKbEHCpE";
                    break;
                case ENV_PRODUCT:
                    mEditMsgSrc.setText("NETPAYTEST");
                    mMerchantId.setText("898310173992528");//898310060514001
                    mTerminerId.setText("70162265");//"12345678"
                    mMerOrderId.setText(getCommonOrder("1028"));//getProOrderId()
                    mSubMerOrderId.setText(getCommonOrder("1028"));
                    mMd5SecretKey = "BcNys5ix3zj4TTSz8HhrXWrZJZHWJBXzMSXdNWxPZ6B7JasS";
                    break;
            }
        } else if (type == TYPE_ALIPAY) {
            switch (currentEnvironment) {
                case ENV_NATIVE:
                case ENV_TEST_ONE:
                    mEditMsgSrc.setText("NETPAY_DEMO");
                    mMerchantId.setText("898310058124024");
                    mTerminerId.setText("12345678");
                    mMerOrderId.setText(getCommonOrder("1028"));// getPostOrderId()
                    mMd5SecretKey = "dwpRz2B6akcp8fwp6JJjenHCH7FKHFcCPE3NkiMJAQzhtD3W";
                    break;
                case ENV_TEST_TWO:
                    mEditMsgSrc.setText("NETPAY");
                    mMerchantId.setText("898310060514001");
                    mTerminerId.setText("12345678");
                    mMerOrderId.setText(getProOrderId());
                    mMd5SecretKey = "1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm";
                    break;
                case ENV_PRODUCT:
                    mEditMsgSrc.setText("WWW.PRODTEST.COM");
                    mMerchantId.setText("898310058124024");
                    mTerminerId.setText("12345678");
                    mMerOrderId.setText(getCommonOrder("5000"));
                    mMd5SecretKey = "AcZdi46z6GibDwi5WXQEdypEWt2WSdNH6RHT3YAwnmCWwQEG";
                    break;
                case ENV_ALIPAY_UAT:
                    mEditMsgSrc.setText("WWW.TEST.COM");
                    mMerchantId.setText("888888800004545");
                    mTerminerId.setText("88889999");
                    mMerOrderId.setText(getCommonOrder("3194"));
                    mMd5SecretKey = "fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR";
                    break;
            }
        } else if (type == TYPE_CLOUD_QUICK_PAY) {
            switch (currentEnvironment) {
                case ENV_PRODUCT:
                    mEditMsgSrc.setText("WWW.PRODTEST.COM");
                    mMerchantId.setText("898310148160568");
                    mTerminerId.setText("12345678");
                    mMerOrderId.setText(getCommonOrder("5000"));
                    mMd5SecretKey = "AcZdi46z6GibDwi5WXQEdypEWt2WSdNH6RHT3YAwnmCWwQEG";
                    break;
                case ENV_NATIVE:
                case ENV_TEST_ONE:
                    mEditMsgSrc.setText("NETPAY");
                    mMerchantId.setText("898310148160568");
                    mTerminerId.setText("12345678");
                    mMerOrderId.setText(getPostOrderId());
                    mMd5SecretKey = "1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm";
                    break;
                case ENV_TEST_TWO:
                    break;
            }
        }
        Log.d(TAG, "-------------mMd5SecretKey = " + mMd5SecretKey);
    }

    /**
     * 随机生成   商户订单号
     * @return  11位
     */
    private String getOrderId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("3028"); //weixin
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    /**
     * 随机生成微信 商户订单号
     * @return  11位
     */
    private String getOrderId4Weixin() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("3816"); //weixin 3028
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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

    /**
     *  //支付宝、微信生产环境
     *
     * 随机生成   商户订单号
     * @return  11位
     */
    private String getProOrderId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("3245"); //支付宝、微信生产环境
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    /**
     *  //支付宝、微信生产环境  通用
     *
     * 随机生成   商户订单号
     * @return  11位
     */
    private String getCommonOrder(String preFix) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(preFix); //支付宝、微信生产环境
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 支付结果回调
     * @param s
     * @param s1
     */
    @Override
    public void onResult(String s, String s1) {
        //Log.d(TAG, "onResult resultCode=" + resultCode + ", resultInfo=" + resultInfo);
        Log.d(TAG, "--------- s=" + s + ", s1=" + s1);
    }

    /**
     * 网络请求
     */
    private void  httpRxPost(){

        dialog = ProgressDialog.show(DemoActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));

        String url  = "https://qr.chinaums.com/netpay-route-server/api/";
        if (mCurrentEnvironment == 0) {  //测试一环境
            url = "https://qr-test1.chinaums.com/netpay-route-server/api/";//"https://qr-test1.chinaums.com/netpay-portal/test/tradeTest.do";//
        } else if (mCurrentEnvironment == 1) {//测试二环境
            url = "https://qr-test2.chinaums.com/netpay-route-server/api/";//"http://umspay.izhong.me/netpay-route-server/api/";
        } else if (mCurrentEnvironment == 2 && typetag != 0) {
            url = "https://mobl-test.chinaums.com/netpay-route-server/api/";
        } else if (mCurrentEnvironment == 3) {
            url = "https://qr.chinaums.com/netpay-route-server/api/";
        } else if (typetag == 0 && mCurrentEnvironment == 2) {
            url = "https://qr-test1.chinaums.com/netpay-route-server/api/";
        }

        if (typetag == TYPE_WEIXIN && mCurrentEnvironment == 0) {
            url = "https://qr-test3.chinaums.com/netpay-route-server/api/";//url = "https://mobl-test.chinaums.com/netpay-route-server/api/";
        }
        //2018-07-11
        if (typetag == TYPE_ALIPAY && mCurrentEnvironment == ENV_ALIPAY_UAT) {
            url = "https://qr-test5.chinaums.com/netpay-route-server/api/";
        }
        Log.i(TAG, "--------------------某支付方式typetag=" + typetag);

        String entity = null;
        switch (typetag){
            case 0:
                entity = getPostParam();
                break;
            case 1:
                divisionInfosArray = new JSONArray();
                entity = getWeiXinParams();
                break;
            case 2:
                if (mCurrentEnvironment == ENV_ALIPAY_UAT) {
                    entity = getAliPayUatParm();
                } else {
                    entity = getAliPayParm();
                }
                break;
            case 3:
                entity = getCloudQuickPayParm();
                break;
        }
        Log.i(TAG, "-------------------- url = " + url);
        Log.i(TAG, "--------------------entity = " + entity);

        RetrofitClientManager.getInstance(url).apiService.requestHttp(getWeiXinParams2()).compose(schedulersTransformer())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"--------------------onCompleted--------");
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,"--------------------onError--------");
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(Object obj) {
                        Log.i(TAG,"--------------------onNext--------");
//                        if(code!=200){
//                            Log.e(TAG, "-------SDK_Sample.Util--httpGet fail, status code = " + var5.getStatusLine().getStatusCode());
//                        }
//                        else {
//                            return EntityUtils.toByteArray(var5.getEntity());
//                        }
//
//                        try {
//
//                            ResponseBody body = (ResponseBody)obj;
//                            String jsonStr = body.string();
//
//                            JSONObject json = new JSONObject(jsonStr);
//                            String status = json.getString("errCode");
//                            if (status.equalsIgnoreCase("SUCCESS")) // 成功
//                            {
//
//                                Log.e(TAG, "appPayRequest=" + json.getString("appPayRequest"));
//                                if (json.isNull("appPayRequest")) {
//                                    Toast.makeText(DemoActivity.this, "服务器返回数据格式有问题，缺少“appPayRequest”字段", Toast.LENGTH_LONG).show();
//                                    return;
//                                } else {
//                                    Toast.makeText(DemoActivity.this, R.string.get_prepayid_succ, Toast.LENGTH_LONG).show();
//                                }
//
//                                if (typetag == 0) {
//                                    payUMSPay(json.getString("appPayRequest"));
//                                } else if (typetag == 1) {
//                                    payWX(json.getString("appPayRequest"));
//                                } else if (typetag == 2) {
//                                    payAliPay(json.getString("appPayRequest"));
//                                } else if (typetag == 3) {
//                                    payCloudQuickPay(json.getString("appPayRequest"));
//                                }
//                            } else {
//                                String msg = String.format(getString(R.string.get_prepayid_fail), json.getString("errMsg"));
//                                Toast.makeText(DemoActivity.this, msg, Toast.LENGTH_LONG)
//                                        .show();
//                                mPayResult.setText(getString(R.string.mpos_callback) + msg);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }
                });
    }


    /**
     *  1 异步后台的任务
     */
    private class GetPrepayIdTask extends AsyncTask<Void, Void, String> {
        public GetPrepayIdTask() {
        }
        //执行前
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(DemoActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }
        //结果回调
        @Override
        protected void onPostExecute(String result) {
            ApplicationInfo appInfo = null;
            try {
                appInfo = mActivity.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (dialog != null) {
                dialog.dismiss();
            }
            if (result == null) {
                Toast.makeText(DemoActivity.this, getString(R.string.get_prepayid_fail, "network connect error"), Toast.LENGTH_LONG).show();
                mPayResult.setText(getString(R.string.mpos_callback) + "network connect error");
            } else {
                Log.i(TAG, "---------老网络回调------onPostExecute-->" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    String status = json.getString("errCode");
                    if (status.equalsIgnoreCase("SUCCESS")) // 成功
                    {

                        Log.e(TAG, "----------------------appPayRequest=" + json.getString("appPayRequest"));
                        if (json.isNull("appPayRequest")) {
                            Toast.makeText(DemoActivity.this, "服务器返回数据格式有问题，缺少“appPayRequest”字段", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Toast.makeText(DemoActivity.this, R.string.get_prepayid_succ, Toast.LENGTH_LONG).show();
                        }

                        if (typetag == 0) {
                            payUMSPay(json.getString("appPayRequest"));
                        } else if (typetag == 1) {
                            payWX(json.getString("appPayRequest"));
                        } else if (typetag == 2) {
                            payAliPay(json.getString("appPayRequest"));
                        } else if (typetag == 3) {
                            payCloudQuickPay(json.getString("appPayRequest"));
                        }
                    } else {
                        String msg = String.format(getString(R.string.get_prepayid_fail), json.getString("errMsg"));
                        Toast.makeText(DemoActivity.this, msg, Toast.LENGTH_LONG)
                                .show();
                        mPayResult.setText(getString(R.string.mpos_callback) + msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        //取消
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        //请求
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://qr.chinaums.com/netpay-route-server/api/";
            if (mCurrentEnvironment == 0) {  //测试一环境
                url = "https://qr-test1.chinaums.com/netpay-route-server/api/";//"https://qr-test1.chinaums.com/netpay-portal/test/tradeTest.do";//
            } else if (mCurrentEnvironment == 1) {//测试二环境
                url = "https://qr-test2.chinaums.com/netpay-route-server/api/";//"http://umspay.izhong.me/netpay-route-server/api/";
            } else if (mCurrentEnvironment == 2 && typetag != 0) {
                url = "https://mobl-test.chinaums.com/netpay-route-server/api/";
            } else if (mCurrentEnvironment == 3) {
                url = "https://qr.chinaums.com/netpay-route-server/api/";
            } else if (typetag == 0 && mCurrentEnvironment == 2) {
                url = "https://qr-test1.chinaums.com/netpay-route-server/api/";
            }

            if (typetag == TYPE_WEIXIN && mCurrentEnvironment == 0) {
                url = "https://qr-test3.chinaums.com/netpay-route-server/api/";//url = "https://mobl-test.chinaums.com/netpay-route-server/api/";
            }
            //2018-07-11
            if (typetag == TYPE_ALIPAY && mCurrentEnvironment == ENV_ALIPAY_UAT) {
                url = "https://qr-test5.chinaums.com/netpay-route-server/api/";
            }

            String entity = null;
            Log.d(TAG, "typetag:" + typetag);

            if (typetag == 1) {
                divisionInfosArray = new JSONArray();
                entity = getWeiXinParams();
            } else if (typetag == 0) {
                entity = getPostParam();
            } else if (typetag == 2) {
                if (mCurrentEnvironment == ENV_ALIPAY_UAT) {
                    entity = getAliPayUatParm();
                } else {
                    entity = getAliPayParm();
                }
            } else if (typetag == 3) {
                entity = getCloudQuickPayParm();
            }

            Log.d(TAG, "doInBackground, url = " + url);
            Log.d(TAG, "doInBackground, entity = " + entity);

            //网络返回结果 转成byte 再字符
            byte[] buf = httpPost(url, entity);
            if (buf == null || buf.length == 0) {
                return null;
            }
            String content = new String(buf);
            Log.d(TAG, "doInBackground, content = " + content);
//            result.parseFrom(content);
            try {
                return content;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "doInBackground, Exception = " + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 2 请求客服端
     * @return
     */
    private static HttpClient newHttpClient() {
        try {
            KeyStore var0;
            (var0 = KeyStore.getInstance(KeyStore.getDefaultType())).load((InputStream) null, (char[]) null);
            SocketFactory var4;
            (var4 = new SocketFactory(var0)).setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams var1;
            HttpProtocolParams.setVersion(var1 = new BasicHttpParams(), HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(var1, "UTF-8");
            SchemeRegistry var2;
            (var2 = new SchemeRegistry()).register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            var2.register(new Scheme("https", var4, 443));
            ThreadSafeClientConnManager var5 = new ThreadSafeClientConnManager(var1, var2);
            return new DefaultHttpClient(var5, var1);
        } catch (Exception var3) {
            return new DefaultHttpClient();
        }
    }

    /**
     * 3 网络请求 结果
     * @param url
     * @param parm
     * @return
     */
    public static byte[] httpPost(String url, String parm) {
        if (url != null && url.length() != 0) {
            HttpClient var2 = newHttpClient();
            HttpPost var4 = new HttpPost(url);

            try {
                var4.setEntity(new StringEntity(parm, "utf-8"));
                var4.setHeader("Content-Type", "text/xml;charset=UTF-8");
                HttpResponse response;
                if ((response = var2.execute(var4)).getStatusLine().getStatusCode() != 200) {
                    Log.e(TAG, "-------SDK_Sample.Util--httpGet fail, status code = " + response.getStatusLine().getStatusCode());
                    return null;
                } else {
                    return EntityUtils.toByteArray(response.getEntity());
                }
            } catch (Exception var3) {
                Log.e(TAG,"SDK_Sample.Util-------httpPost exception, e = " + var3.getMessage());
                var3.printStackTrace();
                return null;
            }
        } else {
            Log.e(TAG,"SDK_Sample.Util-------------httpPost, url is null");
            return null;
        }
    }





    /**
     * 微信
     *
     * @param parms
     */
    private void payWX(String parms) {
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_WEIXIN;
        msg.payData = parms;
        UnifyPayPlugin.getInstance(this).sendPayRequest(msg);
    }

    /**
     * 支付宝
     *
     * @param parms
     */
    private void payAliPay(String parms) {
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY;
        msg.payData = parms;
        UnifyPayPlugin.getInstance(this).sendPayRequest(msg);
    }

    /**
     * 快捷支付
     *
     * @param parms
     */
    private void payUMSPay(String parms) {
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_UMSPAY;
        msg.payData = parms;
        UnifyPayPlugin.getInstance(this).sendPayRequest(msg);
    }

    /**
     * 云闪付
     *
     * @param appPayRequest
     */
    private void payCloudQuickPay(String appPayRequest) {
        String tn = "空";
        try {
            JSONObject e = new JSONObject(appPayRequest);
            tn = e.getString("tn");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        UPPayAssistEx.startPay(this, null, null, tn, "00");
        Log.d("test", "云闪付支付 tn = " + tn);
    }

    /**
     * 组装参数
     * <功能详细描述>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getWeiXinParams() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, String> params = new HashMap<String, String>();
        String orderId = getCommonOrder("3194");//getOrderId4Weixin();
        params.put("instMid", "APPDEFAULT");
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("mid", mMerchantId.getText().toString());
        params.put("msgId", "dsa2231s");
        params.put("msgSrc", mEditMsgSrc.getText().toString());//"WWW.SHHXQWLKJ.COM"//
        params.put("msgType", "wx.appPreOrder"); //"wx.unifiedOrder"// 机器ip地址
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("tid", mTerminerId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("tradeType", "APP");
        params.put("subAppId", "wxc71b9ae0235a4c30");
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
        params.put("divisionFlag", cbDivision.isChecked() + "");
        //if(cbDivision.isChecked()){
        params.put("platformAmount", cbDivision.isChecked() ? mPlatformAmount.getText().toString() : "");
        params.put("subOrders", cbDivision.isChecked() ? divisionInfo.getText().toString() : "[]");
        //}
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");//signWithMd5(buildSignString(params),"fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR","UTF-8");//"fZjyfDK7ix7CKhhBSC8mQWTAtmp44JsTrbkkyKXtxNAxxPFT"//
        params.put("sign", sign);

        WXRequest req = new WXRequest();
        req.tid = params.get("tid");//mTerminerId.getText().toString();
        req.msgSrc = params.get("msgSrc");//mEditMsgSrc.getText().toString();;
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.mid = params.get("mid");//mMerchantId.getText().toString();
        req.msgType = params.get("msgType");//"wx.unifiedOrder";
        req.msgId = "dsa2231s";
        req.totalAmount = mAmountText.getEditableText().toString();
        req.instMid = "APPDEFAULT";
        req.tradeType = "APP";
        req.subAppId = "wxc71b9ae0235a4c30";
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        req.srcReserve = params.get("srcReserve");
        req.divisionFlag = params.get("divisionFlag");
        //if(cbDivision.isChecked()){
        req.platformAmount = params.get("platformAmount");
        req.subOrders = params.get("subOrders");
        // }
        return req.toString();
    }
    private Map<String, String>   getWeiXinParams2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<String, String>();
        String orderId = getCommonOrder("3194");//getOrderId4Weixin();
        params.put("instMid", "APPDEFAULT");
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("mid", mMerchantId.getText().toString());
        params.put("msgId", "dsa2231s");
        params.put("msgSrc", mEditMsgSrc.getText().toString());//"WWW.SHHXQWLKJ.COM"//
        params.put("msgType", "wx.appPreOrder"); //"wx.unifiedOrder"// 机器ip地址
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("tid", mTerminerId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("tradeType", "APP");
        params.put("subAppId", "wxc71b9ae0235a4c30");
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
        params.put("divisionFlag", cbDivision.isChecked() + "");
        //if(cbDivision.isChecked()){
        params.put("platformAmount", cbDivision.isChecked() ? mPlatformAmount.getText().toString() : "");
        params.put("subOrders", cbDivision.isChecked() ? divisionInfo.getText().toString() : "[]");
        //}
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");//signWithMd5(buildSignString(params),"fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR","UTF-8");//"fZjyfDK7ix7CKhhBSC8mQWTAtmp44JsTrbkkyKXtxNAxxPFT"//
        params.put("sign", sign);

        return params;
    }

    private String getAliPayParm() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<String, String>();
        params.put("tid", mTerminerId.getText().toString());
        params.put("msgSrc", mEditMsgSrc.getText().toString());
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("mid", mMerchantId.getText().toString());
        params.put("msgType", "trade.precreate"); // 机器ip地址
        params.put("instMid", "APPDEFAULT");
        params.put("mobile", mMobileId.getText().toString());
        params.put("msgId", getPostOrderId());
        params.put("orderSource", "NETPAY");
        params.put("merchantUserId", mMerchantUserId.getText().toString());//"898340149000005"
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");//signWithMd5(buildSignString(params),"fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR","UTF-8");//
        params.put("sign", sign);
        PostonRequest req = new PostonRequest();
        req.tid = params.get("tid");
        req.msgSrc = params.get("msgSrc");
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.totalAmount = params.get("totalAmount");
        req.mid = params.get("mid");
        req.msgType = params.get("msgType");
        req.instMid = params.get("instMid");
        req.mobile = params.get("mobile");
        req.msgId = params.get("msgId");
        req.orderSource = params.get("orderSource");
        req.merchantUserId = params.get("merchantUserId");
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        req.srcReserve = params.get("srcReserve");
        return req.toString();
    }

    /**
     * UAT环境（暂只用于支付宝支付方式）2018-07-11
     *
     * @return
     */
    private String getAliPayUatParm() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<String, String>();
        params.put("tid", mTerminerId.getText().toString());
        params.put("msgSrc", mEditMsgSrc.getText().toString());
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("mid", mMerchantId.getText().toString());
        params.put("msgType", "trade.precreate"); // 机器ip地址
        params.put("instMid", "APPDEFAULT");
        params.put("mobile", mMobileId.getText().toString());
        params.put("msgId", getPostOrderId());
        params.put("orderSource", "NETPAY");
        params.put("merchantUserId", mMerchantUserId.getText().toString());//"898340149000005"
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");//signWithMd5(buildSignString(params),"fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR","UTF-8");//
        params.put("sign", sign);

        PostonRequest req = new PostonRequest();
        req.tid = params.get("tid");
        req.msgSrc = params.get("msgSrc");
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.totalAmount = params.get("totalAmount");
        req.mid = params.get("mid");
        req.msgType = params.get("msgType");
        req.instMid = params.get("instMid");
        req.mobile = params.get("mobile");
        req.msgId = params.get("msgId");
        req.orderSource = params.get("orderSource");
        req.merchantUserId = params.get("merchantUserId");
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        req.srcReserve = params.get("srcReserve");

        return req.toString();
    }

    private String getPostParam() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<String, String>();
        String orderId = getPostOrderId();
        params.put("tid", mTerminerId.getText().toString());
        params.put("msgSrc", mEditMsgSrc.getText().toString());
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("mid", mMerchantId.getText().toString());
        params.put("msgType", "qmf.order"); // 机器ip地址
        params.put("instMid", "APPDEFAULT");
        params.put("mobile", mMobileId.getText().toString());
        params.put("msgId", getPostOrderId());
        params.put("orderSource", "NETPAY");
        params.put("merchantUserId", mMerchantUserId.getText().toString());
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());//"商户想定制化展示的内容，长度不大于255"
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");
        params.put("sign", sign);
        PostonRequest req = new PostonRequest();
        req.tid = params.get("tid");
        req.msgSrc = params.get("msgSrc");
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.totalAmount = params.get("totalAmount");
        req.mid = params.get("mid");
        req.msgType = params.get("msgType");
        req.instMid = params.get("instMid");
        req.mobile = params.get("mobile");
        req.msgId = params.get("msgId");
        req.orderSource = params.get("orderSource");
        req.merchantUserId = params.get("merchantUserId");
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        req.srcReserve = params.get("srcReserve");
        return req.toString();
    }

    /**
     * 云闪付
     * @return
     */
    private String getCloudQuickPayParm1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<String, String>();
        String orderId = getPostOrderId();
        params.put("tid", mTerminerId.getText().toString());
        params.put("msgSrc", "NETPAY");//mEditMsgSrc.getText().toString()
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("mid", "898310148160568");//mMerchantId.getText().toString()
        params.put("msgType", "uac.appOrder"); // 机器ip地址
        params.put("instMid", "APPDEFAULT");
        params.put("mobile", mMobileId.getText().toString());
        params.put("msgId", getPostOrderId());
        params.put("orderSource", "NETPAY");
        params.put("merchantUserId", mMerchantUserId.getText().toString());
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        String sign = signWithMd5(buildSignString(params), "1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm", "UTF-8");//signWithMd5(buildSignString(params),mMd5SecretKey,"UTF-8");
        params.put("sign", sign);
        PostonRequest req = new PostonRequest();
        req.tid = params.get("tid");
        req.msgSrc = params.get("msgSrc");
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.totalAmount = params.get("totalAmount");
        req.mid = params.get("mid");
        req.msgType = params.get("msgType");
        req.instMid = params.get("instMid");
        req.mobile = params.get("mobile");
        req.msgId = params.get("msgId");
        req.orderSource = params.get("orderSource");
        req.merchantUserId = params.get("merchantUserId");
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        return req.toString();
    }

    /**
     * 云闪付
     * @return
     */
    private String getCloudQuickPayParm() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, String> params = new HashMap<String, String>();
        String orderId = getOrderId();
        params.put("instMid", "APPDEFAULT");
        params.put("merOrderId", mMerOrderId.getText().toString());
        params.put("mid", mMerchantId.getText().toString());////"898310148160568"
        params.put("msgId", "dsa2231s");
        params.put("msgSrc", mEditMsgSrc.getText().toString());//"NETPAY"
        params.put("msgType", "uac.appOrder");//"wx.unifiedOrder"// 机器ip地址
        params.put("requestTimestamp", sdf.format(new Date()));
        params.put("tid", mTerminerId.getText().toString());
        params.put("totalAmount", mAmountText.getEditableText().toString());
        params.put("tradeType", "APP");
        params.put("subAppId", "wxc71b9ae0235a4c30");
        params.put("secureTransaction", String.valueOf(rgSecureTransaction.getCheckedRadioButtonId() == R.id.secure_transaction_true));
        params.put("srcReserve", mEditSrcReserve.getText().toString());
        params.put("divisionFlag", cbDivision.isChecked() + "");
        //if(cbDivision.isChecked()){
        params.put("platformAmount", cbDivision.isChecked() ? mPlatformAmount.getText().toString() : "");
        params.put("subOrders", cbDivision.isChecked() ? divisionInfo.getText().toString() : "[]");
        //}
        String sign = signWithMd5(buildSignString(params), mMd5SecretKey, "UTF-8");//signWithMd5(buildSignString(params),"1234567890lkkjjhhguuijmjfidfi4urjrjmu4i84jvm","UTF-8");//
        params.put("sign", sign);

        WXRequest req = new WXRequest();
        req.tid = mTerminerId.getText().toString();
        req.msgSrc = mEditMsgSrc.getText().toString();// "NETPAY";
        req.requestTimestamp = params.get("requestTimestamp");
        req.merOrderId = params.get("merOrderId");
        req.mid = params.get("mid");//mMerchantId.getText().toString();
        req.msgType = params.get("msgType");//"wx.unifiedOrder";
        req.msgId = params.get("msgId");//"dsa2231s";
        req.totalAmount = mAmountText.getEditableText().toString();
        req.instMid = params.get("instMid");//"APPDEFAULT";
        req.tradeType = params.get("tradeType");//"APP";
        req.subAppId = params.get("subAppId");//"wxc71b9ae0235a4c30";
        req.sign = sign;
        req.secureTransaction = params.get("secureTransaction");
        req.srcReserve = params.get("srcReserve");
        req.divisionFlag = params.get("divisionFlag");
        //if(cbDivision.isChecked()){
        req.platformAmount = params.get("platformAmount");
        req.subOrders = params.get("subOrders");
        // }
        return req.toString();
    }

    /**
     *  MD5加密
     */
    static public String signWithMd5(String originStr, String md5Key, String charset) {
        String text = originStr + md5Key;
        Log.i(TAG,"--------------md5加签字符串=" + text);
        return UnifyMd5.md5Hex(getContentBytes(text, charset)).toUpperCase();
    }
    /**
     *  MD5的 Bytes
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                    + charset);
        }
    }

    /**
     *  支付参数 排序拼接为字符串
     * @param params
     * @return
     */
    static public String buildSignString(Map<String, String> params) {

        List<String> keys = new ArrayList<>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key) || "sign_type".equals(key))
                continue;
            if (params.get(key) == null || params.get(key).equals(""))
                continue;
            keys.add(key);
        }
        Log.i("testzz","-----------------原支付参数转list=" + keys.toString() );
        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }
        Log.i("testzz","----------------- 支付排序拼接=" +  buf.toString() );
        return buf.toString();

    }


    public Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())//指定发射事件时的线程
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());//指定订阅者接收事件时的线程。
            }
        };
    }

}
