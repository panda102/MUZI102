package com.nxm.muzi102.https.httpUtils;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHelper {

    /**
     * 执行不带参数的get请求
     */
    public static void get(String url, RequestListener callBack) {
        get(url, null, callBack);
    }

    /**
     * 执行不带参数的post请求
     */
    public static void post(String url, RequestListener callBack) {
        post(url, null, callBack);
    }


    /**
     * 执行带参数的get请求
     */
    public static void get(final String url, Map<Object, Object> map,
                           final RequestListener callBack) {
        getBuilder(url, map).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
//				LogUtils.d(url+response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
//				LogUtils.d(url+e.getMessage());
                    }

                });
    }

    /**
     * 执行带参数的get请求
     */
    public static void get(final String url, Map<Object, Object> map, Map<Object, Object> header,
                           final RequestListener callBack) {
        getBuilder(url, map, header).build()
//                .readTimeOut(READ_TIMEOUT)//设置读取超时时间
//                .writeTimeOut(WRITE_TIMEOUT)
//                .connTimeOut(CONNECT_TIMEOUT)//设置连接超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                    }

                });
    }

    /**
     * 执行需要缓存数据的post请求
     */
    public static void postCache(final String url, Map<String, String> map,
                                 final RequestEnd callBack) {
        postBuilder(url, map).build()
//                .readTimeOut(READ_TIMEOUT)//设置读取超时时间
//                .writeTimeOut(WRITE_TIMEOUT)
//                .connTimeOut(CONNECT_TIMEOUT)//设置连接超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });
        callBack.onRequest(url);
    }

    /**
     * 执行带参数的post请求
     */
    public static void post(final String url, Map<String, String> map,
                            final RequestListener callBack) {
        postBuilder(url, map)
                .build()
//                .readTimeOut(READ_TIMEOUT)//设置读取超时时间
//                .writeTimeOut(WRITE_TIMEOUT)
//                .connTimeOut(CONNECT_TIMEOUT)//设置连接超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });
    }

    /**
     * 执行带参数的post请求
     */
    public static void post(final String url, Map<Object, Object> map, Map<Object, Object> header,
                            final RequestListener callBack) {
        postBuilder(url, map, header).build()
//                .readTimeOut(READ_TIMEOUT)//设置读取超时时间
//                .writeTimeOut(WRITE_TIMEOUT)
//                .connTimeOut(CONNECT_TIMEOUT)//设置连接超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });
    }

    /**
     * 执行带参数的post请求 ,转成json字符串
     */
    public static void postString(String url, Map<Object, Object> map,
                                  final RequestListener callBack) {
        postStringBuilder(url, map).build()
//                .readTimeOut(READ_TIMEOUT)//设置读取超时时间
//                .writeTimeOut(WRITE_TIMEOUT)
//                .connTimeOut(CONNECT_TIMEOUT)//设置连接超时时间
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);

                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });
    }

    /**
     * 执行带参数的post请求 ,转成json字符串
     */
    public static void postJsonResult(String url, Map<Object, Object> map,
                                      final RequestListener callBack) {
        postStringBuilder(url, map).build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // 请求成功
                String error_no = JsonUtils.getFieldValue(response, "error_no");

                if ("0".equals(error_no)) {
                    String data = JsonUtils.getFieldValue(response, "data");
                    callBack.onResponse(data);
                } else {
                    String error_info = JsonUtils.getFieldValue(response,
                            "error_info");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                callBack.onError(request, e);
            }
        });
    }

    /**
     * 执行带参数的post请求
     */
    public static void postResult(String url, Map<String, String> map,
                                  final RequestListener callBack) {
        postBuilder(url, map).build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                String result = JsonUtils.getFieldValue(response, "result");
                callBack.onResponse(result);
            }

            @Override
            public void onError(Request request, Exception e) {
                callBack.onError(request, e);
            }
        });
    }

    /**
     * 执行带参数的post请求
     */
    public static void postStr(String url, String postBody,
                               final RequestListener callBack) {
        PostStringBuilder builder = postBuilderString(url).content(postBody);
        builder.build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                callBack.onResponse(response);
            }

            @Override
            public void onError(Request request, Exception e) {
                callBack.onError(request, e);
            }
        });

    }

    /**
     * 带参数的postStringBuilder
     */
    private static PostStringBuilder postStringBuilder(String url,
                                                       Map<Object, Object> map) {
        PostStringBuilder builder = postBuilderString(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                builder = builder.content(JsonUtils.parseMapToJson(map));
            }
        }
        return builder;
    }

    /**
     * 带参数的postBuilder
     */
    private static PostFormBuilder postBuilder(String url,
                                               Map<String, String> map) {
        PostFormBuilder builder = postBuilder(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder = builder.addParams(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return builder;
    }

    /**
     * 带参数的postBuilder
     */
    private static PostFormBuilder postBuilder(String url,
                                               Map<Object, Object> map, Map<Object, Object> header) {
        PostFormBuilder builder = postBuilder(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                builder = builder.addParams(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        if (null != header && header.size() > 0) {
            for (Map.Entry<Object, Object> entry : header.entrySet()) {
                builder = builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return builder;
    }

    /**
     * 带参数的getBuilder
     */
    private static GetBuilder getBuilder(String url, Map<Object, Object> map) {
        GetBuilder builder = getBuilder(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                builder = builder.addParams(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return builder;
    }


    /**
     * 带参数的getBuilder
     */
    private static GetBuilder getBuilder(String url, Map<Object, Object> map, Map<Object, Object> header) {
        GetBuilder builder = getBuilder(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                builder = builder.addParams(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        if (null != header && header.size() > 0) {
            for (Map.Entry<Object, Object> entry : header.entrySet()) {
                builder = builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return builder;
    }

    /**
     * 不带参数的postBuilder
     */
    private static PostFormBuilder postBuilder(String url) {
        return OkHttpUtils.post().url(url);
    }

    /**
     * 不带参数的getBuilder
     */
    private static GetBuilder getBuilder(String url) {
        return OkHttpUtils.get().url(url);
    }

    /**
     * 不带参数的postStringBuilder
     */
    private static PostStringBuilder postBuilderString(String url) {
        return OkHttpUtils.postString().url(url);
    }

    /**
     * 上传多文件及map
     */
    public static void upLoadFiles(final String url, Map<Object, Object> map, List<File> files, final RequestListener callBack) {
        PostFormBuilder builder = postBuilder(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                builder = builder.addParams(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        if (files != null && files.size() > 0) {
            for (File file : files) {
                builder.addFile("file", file.getName(), file);
            }
        }

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });
    }

    /**
     * 晒单上传文件
     *
     * @param url
     * @param token
     * @param spellbuyProductId
     * @param shareContent
     * @param files
     * @param callBack
     */
    public static void uploadFile(final String url, String token,
                                  String spellbuyProductId, String shareContent, List<File> files,
                                  final RequestListener callBack) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", token);
        headers.put("spellbuyProductId", spellbuyProductId);
//		headers.put(Ckey.phoneType, SPutils.get(Ckey.phoneType));
        headers.put("shareContent", shareContent);
        PostFormBuilder builder = postBuilder(url);
        builder.headers(headers);
        for (File file : files) {
            builder.addFile("file", file.getName(), file);
        }
        builder.build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                callBack.onResponse(response);
            }

            @Override
            public void onError(Request request, Exception e) {
                callBack.onError(request, e);
            }
        });

    }

    /***
     * 用户头像上传
     *
     * @param url
     * @param token
     * @param file
     * @param callBack
     */
    public static void uploadHeadFile(final String url, String token, String userId, File file,
                                      final RequestListener callBack) {

        PostFormBuilder builder = postBuilder(url);

        builder.addHeader("Authorization", "Bearer " + token).addParams("userId", userId)
                .url(url).addFile("file", file.getName(), file).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        callBack.onError(request, e);
                    }
                });

    }
}
