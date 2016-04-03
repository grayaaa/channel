package com.netease.channel.util;

import org.apache.commons.beanutils.ConvertUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;


/**
 * Generate ids
 * 
 * @author
 */
public class IdUtils {

    public static final String seed = "http://vshow.com/";

    /**
     * Generator a user id by user`s email.
     * 
     * @param email
     * @return
     */
    public static long generateUserId(String email) {
        return genID(email);
    }
    
    public static long generateRechargeId(long userId) {
        return genID(seed + userId + "/recharge/" + UUID.randomUUID().toString());
    }
    
    
    public static long generatePayCashConfigId() {
        return genID(seed + "/payCashConfig/" + UUID.randomUUID().toString());
    }
    
    /**
     * Generate a id from a string value using MD5 digest algorithm
     * 
     * @param str
     * @return
     */
    public static long genID(String str) {
        byte[] data = encode(str);
        return halfDigest(data, 0, data.length);
    }

    /**
     * return a digest value which is half the length of a MD5 digest value.
     * 
     * @param data
     * @param start
     * @param len
     * @return
     */
    public static long halfDigest(byte[] data, int start, int len) {
        byte[] digest = digest(data, start, len);
        return (((long) digest[0] << 56) | ((long) (digest[1] & 0xFF) << 48) | ((long) (digest[2] & 0xFF) << 40)
                | ((long) (digest[3] & 0xFF) << 32) | ((long) (digest[4] & 0xFF) << 24)
                | ((long) (digest[5] & 0xFF) << 16) | ((long) (digest[6] & 0xFF) << 8) | ((long) digest[7] & 0xFF));
    }

    private static final ThreadLocal<MessageDigest> DIGESTER_CONTEXT = new ThreadLocal<MessageDigest>() {
        protected synchronized MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * Construct a hash value for a byte array.
     */
    public static byte[] digest(byte[] data, int start, int len) {
        MessageDigest digester = DIGESTER_CONTEXT.get();
        digester.update(data, start, len);
        return digester.digest();
    }

    

    /**
     * Convert <code>s</code> to a UTF8 Sequence.
     * 
     * @param s
     * @return
     */
    public static byte[] encode(String s) {
        int strlen = s.length();
        int utflen = 0;
        char c;
        for (int i = 0; i < strlen; i++) {
            c = s.charAt(i);
            if ((c >= 0x0000) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }
        byte[] buf = new byte[utflen];
        encode(s, buf, 0);
        return buf;
    }

    /**
     * 将字符串转化成为UTF-8的表示，保存在bytes从offset开始的空间内. 需要注意的是，这个方法没有做range check，所以希望给出的bytes留下 足够的空间.
     * 
     * @param s String to be encoded
     * @param bytes Buffer to store encoded result
     * @param offset Start of the buffer to store the result
     * @return 实际编码后的字节数，
     */
    private static int encode(String s, byte[] bytes, int offset) {
        int strlen = s.length();

        int i = 0;
        char c;
        int pos = offset;
        for (i = 0; i < strlen; i++) {
            c = s.charAt(i);
            if (!((c >= 0x0000) && (c <= 0x007F))) {
                break;
            }
            bytes[pos++] = (byte) c;
        }

        for (; i < strlen; i++) {
            c = s.charAt(i);
            if ((c >= 0x0000) && (c <= 0x007F)) {
                bytes[pos++] = (byte) c;
            } else if (c > 0x07FF) {
                bytes[pos++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytes[pos++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytes[pos++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytes[pos++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytes[pos++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        return pos - offset;
    }

    /**
     * 随机生成20位订单号，订单格式为年月日时分秒毫秒+三位随机字符串，随机字符串由0-9
     * @param length
     * @return
     */
    public static long generateOrderId() {
        int i;
        int count = 0; 
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer buf = new StringBuffer("");
        long currentTime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        buf.append(df.format(cal.getTime()));
        Random  rand = new Random(currentTime);
        while (count < 2) {
            i = Math.abs(rand.nextInt(str.length));
            if (i >= 0 && i < str.length) {
                buf.append(str[i]);
                count++;
            }
        }
        return  (Long)ConvertUtils.convert(buf.toString(), Long.class);
    }
    
    
     
     
     public static void main(String[] args) {
         System.out.println(generateUserId("sytang@corp.netease.com"));
     }
     
}
