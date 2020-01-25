package com.csdn.reader.util;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:
 * @description:
 * @date: 2019-05-30 23:48
 */
public class Tool {

    // IP地址代理库Map
    private static Map<String, Integer> IPProxyRepository = new HashMap<>();
    private static String[] keysArray = null;   // keysArray是为了方便生成随机的代理对象


    public static String doGet(String url) throws Exception{
        String body = "";


        CloseableHttpClient httpClient = null;
        HttpHost proxy = null;
        if (IPProxyRepository.size() > 0) {  // 如果ip代理地址库不为空，则设置代理
            proxy = getRandomProxy();
            httpClient = HttpClients.custom().setProxy(proxy).build();  // 创建httpclient对象
        } else {
            httpClient = HttpClients.custom().build();  // 创建httpclient对象
        }
        //HttpClient httpClient = HttpClientBuilder.create().build();


        HttpGet httpGet = new HttpGet(url);
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpGet.setConfig(defaultConfig);
        httpGet.setHeader("Accept","*/*");
        httpGet.setHeader("Accept-Encoding","gzip, deflate, br");
        httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httpGet.setHeader("Cookie","l=AurqcPuigwQdnQv7WvAfCoR1OlrRQW7h; isg=BHp6mNB79CHqYXpVEiRteXyyyKNcg8YEwjgLqoRvCI3ddxqxbLtOFUBGwwOrZ3ad; thw=cn; cna=VsJQERAypn0CATrXFEIahcz8; t=0eed37629fe7ef5ec0b8ecb6cd3a3577; tracknick=tb830309_22; _cc_=UtASsssmfA%3D%3D; tg=0; ubn=p; ucn=unzbyun; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; miid=981798063989731689; hng=CN%7Czh-CN%7CCNY%7C156; um=0712F33290AB8A6D01951C8161A2DF2CDC7C5278664EE3E02F8F6195B27229B88A7470FD7B89F7FACD43AD3E795C914CC2A8BEB1FA88729A3A74257D8EE4FBBC; enc=1UeyOeN0l7Fkx0yPu7l6BuiPkT%2BdSxE0EqUM26jcSMdi1LtYaZbjQCMj5dKU3P0qfGwJn8QqYXc6oJugH%2FhFRA%3D%3D; ali_ab=58.215.20.66.1516409089271.6; mt=ci%3D-1_1; cookie2=104f8fc9c13eb24c296768a50cabdd6e; _tb_token_=ee7e1e1e7dbe7; v=0");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64;` rv:47.0) Gecko/20100101 Firefox/47.0");
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            body =EntityUtils.toString(httpEntity, Consts.UTF_8);
            //System.out.println(body);
            //释放连接
            httpGet.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * @Title: doPost
     * @Description: post请求
     * @param url
     * @param params
     * @return
     * @author Mundo
     */
    public static String doPost(String url, Map<String, String> params) {
        String result = "";
        // 创建httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        try { // 参数键值对
            if (null != params && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                NameValuePair pair = null;
                for (String key : params.keySet()) {
                    pair = new BasicNameValuePair(key, params.get(key));
                    pairs.add(pair);
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpPost) {
                // 释放连接
                httpPost.releaseConnection();
            }
        }
        return result;
    }

    public static Set<String> getAllBlogUrl(String userId) throws Exception{
        Set<String> urls = new HashSet<String>();

        // ----------------------------------------------遍历每一页 获取文章链接----------------------------------------------
        final String homeUrl = "https://blog.csdn.net/" + userId + "/article/list/";// 后面加pageNum即可
        int totalPage = 0;
        String pageStr;
        StringBuilder curUrl = null;
        for (int i = 1; i < 15; i++) {
            Thread.sleep(1000);
            System.out.println("finding page " + i);
            curUrl = new StringBuilder(homeUrl);
            curUrl.append(i);
            System.out.println(curUrl);
            pageStr = doGet(curUrl.toString());

            List<String> list = getMatherSubstrs(pageStr, "(?<=href=\")https://blog.csdn.net/" + userId + "/article/details/[0-9]{8,9}(?=\")");
            urls.addAll(list);

            if (pageStr.lastIndexOf("空空如也") != -1) {
                System.out.println("No This Page!");
                break;
            } else {
                System.out.println("Success~");
            }
            totalPage = i;
        }
        System.out.println("总页数为: " + totalPage);

       // ---------------------------------------------------打印每个链接---------------------------------------------------
        System.out.println("打印每个链接");
        for (String s:urls) {
            System.out.println(s);
        }
        System.out.println("打印每个链接完毕");
        return urls;

    }

    /**
     * 随机返回一个代理对象
     *
     * @return
     */
    public static HttpHost getRandomProxy() {
        // 随机获取host:port，并构建代理对象
        Random random = new Random();
        String host = keysArray[random.nextInt(keysArray.length)];
        int port = IPProxyRepository.get(host);
        HttpHost proxy = new HttpHost(host, port);  // 设置http代理
        return proxy;
    }


    // 正则匹配
    public static List<String> getMatherSubstrs(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}
