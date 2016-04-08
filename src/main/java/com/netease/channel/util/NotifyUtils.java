package com.netease.channel.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * 通知工具类（POPO）
 */
public class NotifyUtils {

    private static final Log logger = LogFactory.getLog(NotifyUtils.class);

    private static final String POPO_URL = "http://220.181.29.178:5820/popo";

    private static final String LINE_SEPARATOR = "\r\n";

    private static final String IP_TEMPLATE = "IP=[%s]";
    private static final String[] POPO_NOTIFY_TO = new String[]{"kaiwang@corp.netease.com"};
    private static String LOCAL_IP = "";

    public static void notify(Object msg) {
        for (String to : POPO_NOTIFY_TO) {
            sendPopo(to, msg.toString());
        }
    }

    public static void notify(Object msg, String[] users) {
        for (String to : users) {
            sendPopo(to, msg.toString());
        }
    }

    public static void sendPopo(String to, String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        try {
            StringBuilder buf = new StringBuilder();
            buf.append(POPO_URL);
            buf.append("?account=").append(to);
            String data = URLEncoder.encode(msg, "UTF-8");
            buf.append("&msg=").append(data.substring(0, Math.min(data.length(), 4000)));
            HttpClientUtil.getInstance().execute(buf.toString());
        } catch (Throwable e) {
            logger.error(e, e);
        }
    }

    public static String getLocalIP() {
        if (StringUtils.isNotBlank(LOCAL_IP)) {
            return LOCAL_IP;
        }
        String ip = "";
        try {
            ip = getRealIp();
        } catch (Exception e) {
        }
        LOCAL_IP = String.format(IP_TEMPLATE, new Object[]{ip});
        return LOCAL_IP;
    }

    public static String getRealIp() throws Exception {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}
