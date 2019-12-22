package com.lzj.testdemo.httpserver;

import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 网络请求
 */
public interface ApiService {
    // @Headers("Content-type:text/xml")
    @Headers("Content-type:application/json")
    @POST("/")
    Observable<ResponseBody> requestHttp(@Body Map<String, String> map);

}
