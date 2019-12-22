package com.lzj.testdemo.httpserver;




import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lzj on 2018/4/28.
 *  网络引擎Retrofit
 */

public class RetrofitClientManager {
    public static  String BASE_URL = "https://qr-test2.chinaums.com/netpay-route-server/api/";//生成环境

    public static final int DEFAULT_TIMEOUT = 5;
    public   ApiService apiService;
    public OkHttpClient.Builder  okHttpClient;
    private static RetrofitClientManager mRetrofit;

    public static RetrofitClientManager getInstance() {
        if(mRetrofit==null) {
            synchronized (RetrofitClientManager.class) {
                if (mRetrofit == null)
                    mRetrofit = new RetrofitClientManager();
            }
        }
        return mRetrofit;
    }

    public static RetrofitClientManager getInstance(String baseUrl) {
//        if(mRetrofit==null) {
//            synchronized (RetrofitClientManager.class) {
//                if (mRetrofit == null)
//                    mRetrofit = new RetrofitClientManager();
//            }
//        }

        mRetrofit = new RetrofitClientManager();

        BASE_URL = baseUrl;
        return mRetrofit;
    }


    private RetrofitClientManager() {
        okHttpClient=null;
        okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new LoggingInterceptor());//自定义拦截器

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        apiService = retrofit.create(ApiService.class);
    }
}
