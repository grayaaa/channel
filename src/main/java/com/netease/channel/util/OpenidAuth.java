package com.netease.channel.util;

import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 主要有一下几个操作： 1. consumer 与 openid server 进行关联（association） 2. consumer 发起认证请求。 3. consumer 对 openid
 * 发送过来的消息进行签名验证，如果签名一致，则认证通过！ 4. 这一步是可选的(check authentication)，consumer 将 openid 发送过来的消息组织一下，再重新发送给 openid
 * server，判断验证是否通过。
 */
public class OpenidAuth {
    private static final Logger LOG = Logger.getLogger(OpenidAuth.class);
    private static String OPENID_SERVER = "https://login.netease.com/openid/";
    // private String assoc_handle = "";
    // private String mac_key = "";
    // private int expires_in = 0;

    // private Map<String, String> assoc_data;
    // private Map<String, String> redirect_data;
    private Map<String, String> auth_response;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        OpenidAuth consumer = new OpenidAuth();
        consumer.association();
        // consumer.authentication();
        // try {
        // consumer.check_signature();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // consumer.check_authentication();
    }

    public void OpenidAuth() {

    }

    public String MaptoString_url_utf8(Map<String, String> map) {
    /*
     * 将URL的参数以分段的方式进行URL utf8编码，并返回一个字符串
     */
        String arguments = "?";
        Iterator iter = map.entrySet().iterator();
        boolean first_arg = true;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            String key_str = (String) key;
            String val_str = (String) val;

            try {
                key_str = URLEncoder.encode(key_str, "UTF-8");
                val_str = URLEncoder.encode(val_str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (first_arg == false) {
                arguments = arguments + "&";
            }
            first_arg = false;

            arguments = arguments + key_str;
            arguments = arguments + "=";
            arguments = arguments + val_str;
        }
        return arguments;
    }

    public Map<String, String> association() {
        Map<String, String> assoc_data = new HashMap<String, String>();
        assoc_data.put("openid.mode", "associate");
        assoc_data.put("openid.assoc_type", "HMAC-SHA256");
        assoc_data.put("openid.session_type", "no-encryption");

        String arguments = MaptoString_url_utf8(assoc_data);
        Map<String, String> asso = new HashMap<String, String>();

        URL url = null;

        try {
            url = new URL(OPENID_SERVER + arguments);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
      /* 接收OpenID返回的值，将assoc_handle, expires_in, mac_key的值存起来 */
            BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String str = "";

            do {
                try {
                    str = r.readLine();
                    if (str == null) {
                        break;
                    }
                    String[] temp_arrays = str.split(":");
                    if (temp_arrays[0].equals("assoc_handle")) {
                        // assoc_handle = temp_arrays[1];
                        asso.put(LoginUtil.ASSOC_HANDLE, temp_arrays[1]);
                        // System.out.println("assoc_handle in oid: " + temp_arrays[1]);
                    } else if (temp_arrays[0].equals("expires_in")) {
                        // String expires_str = temp_arrays[1];
                        // expires_in = Integer.parseInt(expires_str);
                        asso.put(LoginUtil.EXPIRES_IN, temp_arrays[1]);
                        // System.out.println("expires_in in oid: " + temp_arrays[1]);
                    } else if (temp_arrays[0].equals("mac_key")) {
                        // mac_key = temp_arrays[1];
                        asso.put(LoginUtil.MAC_KEY, temp_arrays[1]);
                        // System.out.println("mac_key in oid: " + temp_arrays[1]);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (str != null);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return asso;

    }

    public String authentication(String returnto, String realm, String assoc_handle) {
        Map<String, String> redirect_data = new HashMap<String, String>();
        redirect_data.put("openid.ns", "http://specs.openid.net/auth/2.0");
        redirect_data.put("openid.mode", "checkid_setup");
        redirect_data.put("openid.assoc_handle", assoc_handle);
        redirect_data.put("openid.return_to", returnto);
        redirect_data.put("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select");
        redirect_data.put("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select");
        redirect_data.put("openid.realm", realm);
        redirect_data.put("openid.ns.sreg", "http://openid.net/extensions/sreg/1.1");
        redirect_data.put("openid.sreg.required", "nickname,email,fullname");
        // redirect_data.put("openid.sreg.required", "nickname,email");
        // redirect_data.put("openid.ns.ax", "http://openid.net/srv/ax/1.0");
        // redirect_data.put("openid.ax.mode", "fetch_request");
        // redirect_data.put("openid.ax.type.empno", "https://login.netease.com/openid/empno/");
        // redirect_data.put("openid.ax.type.dep", "https://login.netease.com/openid/dep/");
        // redirect_data.put("openid.ax.required", "empno,dep");

        String arguments = MaptoString_url_utf8(redirect_data);
    /* 生成完整的URL地址 */
        return OPENID_SERVER + arguments;

    }

    public Map<String, String> check_signature(String originurl, String assoc_handle, String mac_key) throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        auth_response = new HashMap<String, String>();
        // System.out.println("访问成功后，请将完整的返回URL提交此处：");
        // InputStreamReader isr = new InputStreamReader(new InputStream(ourl));
        // BufferedReader br= new BufferedReader(isr);
        // String origin_url = br.readLine();
        String url = URLDecoder.decode(originurl, "UTF-8");
        LOG.debug("url-->"+url);

//        String[] arrays = url.split("&openid");
//        for (String s : arrays) {
//            if (s.startsWith(".")) { // not the first one
//                String param = s.replaceFirst("=", "@,,@");
//                String parray[] = param.split("@,,@");
//                if (parray.length == 2) {
//                    auth_response.put("openid" + parray[0], parray[1]);
//                } else {
//                    LOG.error("url parse error: [" + s + "] in [" + url + "]");
//                }
//            }
//        }
//
//        int size = arrays.length;
//    /* 将OpenID server返回的URL地址解析，获取参数 */
//        for (int i = 0; i < size; i++) {
//            int index = arrays[i].indexOf("=");
//            if (index == -1)
//                continue;
//            auth_response.put(arrays[i].substring(0, index), arrays[i].substring(index + 1, arrays[i].length()));
//        }
        auth_response = CRequest.URLRequest(url);
        LOG.debug("auth_response==>"+auth_response);

        if (auth_response.get("openid.mode").equals("id_res") == false) {
            // System.out.println("openid.mode 返回值不是 id_res, 认证失败!");
            LOG.error("openid.mode 返回值不是 id_res, 认证失败!");
            return null;
        }

        if (auth_response.get("openid.identity").startsWith("https://login.netease.com/openid/") == false) {
            LOG.error("openid.identity does not start with https://login.netease.com/openid/， 认证失败！");
            return null;
        }
        LOG.debug("assoc_handle-->" + assoc_handle);
        if (auth_response.get("openid.assoc_handle").equals(assoc_handle) == true) {
            String[] signed_items = auth_response.get("openid.signed").split(",");
            String signed_content = "";
            for (int i = 0; i < signed_items.length; i++) {
                signed_content = signed_content + signed_items[i];
                signed_content = signed_content + ":";
                signed_content = signed_content + auth_response.get("openid." + signed_items[i]);
                signed_content = signed_content + "\n";
            }
            // System.out.println("需要签名的参数和值:\n" + signed_content);
      /* 注意，mac_key也许要先进行base64解码 */
            byte[] decoded64 = (new sun.misc.BASE64Decoder()).decodeBuffer(mac_key);
            SecretKey signingKey = new SecretKeySpec(decoded64, "HMACSHA256");
            Mac mac = null;
            try {
                mac = Mac.getInstance("HMACSHA256");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                mac.init(signingKey);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      /* 签名的字符串需要转换成 UTF-8 编码 */
            byte[] digest = mac.doFinal(signed_content.getBytes("UTF-8"));
      /* 计算得到消息摘要之后，需要进行base64编码 */
            String signature = (new sun.misc.BASE64Encoder()).encode(digest);
            // System.out.println("openid server返回的签名是:" + auth_response.get("openid.sig"));
            // System.out.println("consumer 计算出来的签名是:" + signature);
            if (signature.equals(auth_response.get("openid.sig")) == true) {
                returnMap.put("email", auth_response.get("openid.sreg.email"));
                returnMap.put("fullname", auth_response.get("openid.sreg.fullname"));
                returnMap.put("returnto", auth_response.get("openid.return_to"));
                // System.out.println("签名一致，验证成功！");
            } else {
                // System.out.println("签名不一致，验证失败！");
                LOG.error("openid.sig 签名不一致，验证失败！");
                return null;
            }
        } else {
            if (check_authentication() == true) {
                returnMap.put("email", auth_response.get("openid.sreg.email"));
                returnMap.put("fullname", auth_response.get("openid.sreg.fullname"));
                returnMap.put("returnto", auth_response.get("openid.return_to"));
            } else {
                LOG.error("openid.assoc_handle，验证失败！");
                return null;
            }
        }

        return returnMap;
    }

    public boolean check_authentication() {
    /* 将openid.mode参数的值设置为check_authentication，其他参数和值不变，发回给OpenID server */
        auth_response.put("openid.mode", "check_authentication");
        String arguments = MaptoString_url_utf8(auth_response);
        URL url = null;
        boolean valid = false;
        try {
            url = new URL(OPENID_SERVER + arguments);

            // System.out.println(url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String str = "";
            String auth_result = "";

            do {
                try {
                    str = r.readLine();
                    if (str == null)
                        break;
                    String[] temp_arrays = str.split(":");
                    if (temp_arrays[0].equals("is_valid")) {
                        auth_result = temp_arrays[1];
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(str);

            } while (str != null);
            if (auth_result.equals("true")) {
                valid = true;
                // System.out.println("check authentication 认证成功！");
            } else {
                valid = false;
                LOG.error("is_valid is false，验证失败！");
                // System.out.println("check authentication 认证失败！");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return valid;
    }
}
