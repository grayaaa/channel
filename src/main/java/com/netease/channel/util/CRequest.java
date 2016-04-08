package com.netease.channel.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qmgeng on 16/4/1.
 */
public class CRequest {
    /**
     * 解析出url请求的路径，包括页面
     *
     * @param strURL url地址
     * @return url路径
     */
    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }

        return strPage;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], strSplit.substring(arrSplitEqual[0].length() + 1, strSplit.length()));

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static void main(String[] args) {
        String url = "http://buzz096x.hz.youdao.com:8080/?openid.assoc_handle={HMAC-SHA256}{56fe1144}{d5KA5A==}&openid.ax.mode=fetch_response&openid.claimed_id=https://login.netease.com/openid/qmgeng/&openid.identity=https://login.netease.com/openid/qmgeng/&openid.mode=id_res&openid.ns=http://specs.openid.net/auth/2.0&openid.ns.ax=http://openid.net/srv/ax/1.0&openid.ns.sreg=http://openid.net/extensions/sreg/1.1&openid.op_endpoint=https://login.netease.com/openid/&openid.response_nonce=2016-04-01T06:12:22ZZp3tof&openid.return_to=http://buzz096x.hz.youdao.com:8080/&openid.sig=PJr2W5GwLyON+skvn0OGK+98J4iz8v9ni2r5riuWkEU=&openid.signed=assoc_handle,ax.mode,claimed_id,identity,mode,ns,ns.ax,ns.sreg,op_endpoint,response_nonce,return_to,signed,sreg.email,sreg.fullname,sreg.nickname&openid.sreg.email=qmgeng@corp.netease.com&openid.sreg.fullname=耿庆民&openid.sreg.nickname=qmgeng";
        System.out.println(URLRequest(url));
    }
}
