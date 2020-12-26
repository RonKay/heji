package com.rh.heji.utlis.http.basic;

import android.content.Intent;
import android.text.TextUtils;


import com.rh.heji.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Date: 2019-10-15
 * Author: 锅得铁
 * - OkHttp请求配置
 */
public class OkHttpConfig {
    public static String SERVLET_DOWNLOAD_USER_HEADIMG = "";
    public final static int BUFFER_SIZE = 64 * 1024;
    public static Long connectTimeout = 10000L;
    public static Long readTimeout = 10000L;
    public static Long writeTimeout = 10000L;

    /**
     * OkHttpClient 配置
     *
     * @return
     */
    public static OkHttpClient.Builder getClientBuilder() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        if (!BuildConfig.DEBUG) {
            logging.redactHeader("Authorization");
            logging.redactHeader("Cookie");
        }
        //HttpHeaderInterceptor headerInterceptor = new HttpHeaderInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //.addInterceptor(headerInterceptor)
                .addInterceptor(logging)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        return builder;
    }

    /**
     * Header 拦截器
     */
    static class HttpHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request().newBuilder()
                    //.header("Content-Type", "application/json")
                    //.addHeader("DATE_TIME",time)
                    //.addHeader("Authorization", token.getAuth_cookie())//该字段已经不能存在
//                    .addHeader("usertype", token.getUserType() + "")
//                    .addHeader("username", token.getUserName()+"")
//                    .addHeader("ticket", token.getTicket()+"")
//                    .addHeader("platform", BuildConfig.ClientType+"")
                    .build();
            return chain.proceed(request);
        }
    }

}
