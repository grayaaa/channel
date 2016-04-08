package com.netease.channel.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * XSS过滤工具类
 */
public final class RemoveXSSUtils {

    /**
     * 回车 换行符等 替换为空白
     */
    private static final int LEVEL_FILTER_HTML_1 = 1;
    /**
     * html敏感的字符<>'"/,
     */
    private static final int LEVEL_FILTER_HTML_2 = 2;
    /**
     * 空白及敏感字符
     */
    public static final int LEVEL_BASIC = LEVEL_FILTER_HTML_1 | LEVEL_FILTER_HTML_2;
    /**
     * 所有的html规定的必须转义的字符 完整字符集请参考<br/>
     * http://www.w3.org/TR/html4/sgml/entities.html
     */
    private static final int LEVEL_FILTER_HTML_3 = 4;
    /**
     * html规定的全部转义
     */
    public static final int LEVEL_HTML_ENTITY = LEVEL_FILTER_HTML_1 | LEVEL_FILTER_HTML_2 | LEVEL_FILTER_HTML_3;
    /**
     * 全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母：FF00-FFEF
     */
    private static final int LEVEL_FILTER_HTML_4 = 8;
    /**
     * CJK部首补充  CJK标点符号
     */
    private static final int LEVEL_FILTER_HTML_5 = 16;
    /**
     * 转义html4 里面的需要转义的特殊字符集
     */
    private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();
    // Private variables
    private static String EMPTYSTRING_JAVASCRIPT = "''";
    private static String EMPTYSTRING_VBS = "\"\"";
    private static String EMPTYSTRING = "";
    private static StringBuffer strb;
    private static StringCharacterIterator sci;
    private static Map<Character, String> LEVEL_1_CHAR_MAP = new HashMap<Character, String>();

    private static Map<Character, String> LEVEL_2_CHAR_MAP = new HashMap<Character, String>();

    static {
        LEVEL_1_CHAR_MAP.put('\r', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\t', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\f', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\n', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2000', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2001', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2002', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2003', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2004', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2005', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2006', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2007', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2008', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2009', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u200a', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u200b', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2028', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2029', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u3000', EMPTYSTRING);
        LEVEL_1_CHAR_MAP.put('\u2008', EMPTYSTRING);

        LEVEL_2_CHAR_MAP.put('<', "&lt;");
        LEVEL_2_CHAR_MAP.put('>', "&gt;");
        LEVEL_2_CHAR_MAP.put('\"', "&quot;");
        LEVEL_2_CHAR_MAP.put('\'', "&apos;");
        LEVEL_2_CHAR_MAP.put('\r', EMPTYSTRING);
        LEVEL_2_CHAR_MAP.put('\t', EMPTYSTRING);
        LEVEL_2_CHAR_MAP.put('\f', EMPTYSTRING);
        LEVEL_2_CHAR_MAP.put('\n', EMPTYSTRING);

        //LEVEL_2_CHAR_MAP.put('/', "&#47;");
        //LEVEL_2_CHAR_MAP.put(',', "&#44;");
    }

    ;

    /**
     *
     */
    private RemoveXSSUtils() {

    }

    /**
     * 基本的html转义
     *
     * @param strInput
     * @return
     */
    private static String encodeHtml(String strInput) {
        if (strInput == null || strInput.length() == 0) {
            return EMPTYSTRING;
        }
        //strInput = strInput.replaceAll("\\p{C}", "");
        return encodeHtml(strInput, LEVEL_BASIC);
    }

    /**
     * 按指定的级别转义html
     *
     * @param strInput
     * @return
     */
    private static String encodeHtml(String strInput, int level) {
        if (strInput == null || strInput.length() == 0) {
            return EMPTYSTRING;
        }
        StringBuffer builder = new StringBuffer(strInput.length() * 2);
        CharacterIterator it = new StringCharacterIterator(strInput);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            int ct = Character.getType(ch);
            if (ct == Character.CONTROL
                    || ct == Character.FORMAT
                    || ct == Character.PRIVATE_USE
                    || ct == Character.SURROGATE
                    || ct == Character.UNASSIGNED) {
                continue;
            }
            String reference = null;

            // 基本的回车 换行符等 替换为空白
            if ((LEVEL_FILTER_HTML_1 & level) > 0) {
                reference = LEVEL_1_CHAR_MAP.get(ch);

                if (reference != null) {
                    builder.append(reference);
                    continue;
                }
            }

            // html敏感的字符<>'"/,
            if ((LEVEL_FILTER_HTML_2 & level) > 0) {
                reference = LEVEL_2_CHAR_MAP.get(ch);

                if (reference != null) {
                    builder.append(reference);
                    continue;
                }
            }

            // html明确指定的需要转义的字符
            if ((LEVEL_FILTER_HTML_3 & level) > 0) {
                reference = characterEntityReferences.convertToReference(ch);
                if (reference != null) {
                    builder.append(reference);
                    continue;
                }
            }

            // 全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母
            if ((LEVEL_FILTER_HTML_4 & level) > 0) {
                if ((ch >= '\uFF00') && (ch <= '\uFFEF')) {
                    builder.append("&#" + (int) ch + ";");
                    continue;
                }
            }

            // CJK部首补充 CJK标点符号
            if ((LEVEL_FILTER_HTML_5 & level) > 0) {
                if ((ch >= '\u2E80') && (ch <= '\u2EFF') || (ch >= '\u3000') && (ch <= '\u303F')) {
                    builder.append("&#" + (int) ch + ";");
                    continue;
                }
            }

            builder.append(ch);
        }
        return builder.toString();
    }

    private static String encodeHtmlAttribute(String strInput) {
        if (strInput.length() == 0) {
            return EMPTYSTRING;
        }
        StringBuffer builder = new StringBuffer(strInput.length() * 2);
        CharacterIterator it = new StringCharacterIterator(strInput);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
                    || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
                builder.append(ch);
            } else {
                builder.append("&#" + (int) ch + ";");
            }
        }
        return builder.toString();
    }

    private static String encodeJs(String strInput) {
        if (strInput.length() == 0) {
            return EMPTYSTRING_JAVASCRIPT;
        }
        StringBuffer builder = new StringBuffer("'");
        CharacterIterator it = new StringCharacterIterator(strInput);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
                    || (((ch == ' ') || ((ch > '/') && (ch < ':')))
                    || (((ch == '.') || (ch == ','))
                    || ((ch == '-') || (ch == '_'))))) {
                builder.append(ch);
            } else if (ch > '\u007f') {
                builder.append("\\u" + twoByteHex(ch));
            } else {
                builder.append("\\x" + singleByteHex(ch));
            }
        }
        builder.append("'");
        return builder.toString();
    }

    private static String encodeUrl(String strInput) {
        if (strInput.length() == 0) {
            return EMPTYSTRING;
        }
        StringBuffer builder = new StringBuffer(strInput.length() * 2);
        CharacterIterator it = new StringCharacterIterator(strInput);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
                    || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == '-')) || (ch == '_')))) {
                builder.append(ch);
            } else if (ch > '\u007f') {
                builder.append("%u" + twoByteHex(ch));
            } else {
                builder.append("%" + singleByteHex(ch));
            }
        }
        return builder.toString();
    }

    private static String encodeVbs(String strInput) {
        if (strInput.length() == 0) {
            return EMPTYSTRING_VBS;
        }
        StringBuffer builder = new StringBuffer(strInput.length() * 2);
        boolean flag = false;
        CharacterIterator it = new StringCharacterIterator(strInput);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
                    || (((ch == ' ') || ((ch > '/') && (ch < ':')))
                    || (((ch == '.') || (ch == ',')) || ((ch == '-')
                    || (ch == '_'))))) {
                if (!flag) {
                    builder.append("&\"");
                    flag = true;
                }
                builder.append(ch);
            } else {
                if (flag) {
                    builder.append("\"");
                    flag = false;
                }
                builder.append("&chrw(" + (long) ch + ")");
            }
        }
        if ((builder.length() > 0) && (builder.charAt(0) == '&')) {
            builder.delete(0, 1);
        }
        if (builder.length() == 0) {
            builder.insert(0, "\"\"");
        }
        if (flag) {
            builder.append("\"");
        }
        return builder.toString();
    }

    private static String encodeXml(String strInput) {
        return encodeHtml(strInput);
    }

    private static String encodeXmlAttribute(String strInput) {
        return encodeHtmlAttribute(strInput);
    }

    /**
     * Returns a string object encoded to be used in an HTML attribute.
     * <p/>
     * This method will return characters a-z, A-Z, 0-9, full stop, comma, dash, and underscore unencoded, and encode
     * all other character in decimal HTML entity format (i.e. < is encoded as &#60;).
     *
     * @param s a string to be encoded for use in an HTML attribute context
     * @return the encoded string
     */
    public static String htmlAttributeEncode(String s) {
        return encodeHtmlAttribute(s);
    }

    /**
     * 转义基本的html字符
     *
     * @param s a string to be encoded for use in an HTML context
     * @return the encoded string
     */
    public static String htmlEncode(String s) {
        return encodeHtml(s);
    }

    public static boolean isImgAddress(String url) {
        if (url.startsWith("http://")) {
            return true;
        }
        return false;
    }


    /**
     * 按转义级别转义字符
     *
     * @param strInput 输入
     * @param level    转义级别
     * @return 转义结果
     */
    public static String htmlEncode(String strInput, int level) {
        return encodeHtml(strInput, level);
    }

    /**
     * Returns a string object encoded to use in JavaScript as a string.
     * <p/>
     * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore unencoded, and
     * encode all other character in a 2 digit hexadecimal escaped format for non-unicode characters (e.g. \x17), and in
     * a 4 digit unicode format for unicode character (e.g. \u0177).
     * <p/>
     * The encoded string will be returned enclosed in single quote characters (i.e. ').
     *
     * @param s a string to be encoded for use in a JavaScript context
     * @return the encoded string
     */
    public static String javascriptEncode(String s) {
        return encodeJs(s);
    }

    private static String singleByteHex(char c) {
        long num = c;
        return leftPad(Long.toString(num, 16), "0", 2);
    }

    private static String twoByteHex(char c) {
        long num = c;
        return leftPad(Long.toString(num, 16), "0", 4);
    }

    /**
     * Returns a string object encoded to use in a URL context.
     * <p/>
     * This method will return characters a-z, A-Z, 0-9, full stop, dash, and underscore unencoded, and encode all other
     * characters in short hexadecimal URL notation. for non-unicode character (i.e. < is encoded as %3c), and as
     * unicode hexadecimal notation for unicode characters (i.e. %u0177).
     *
     * @param s a string to be encoded for use in a URL context
     * @return the encoded string
     */
    public static String urlEncode(String s) {
        return encodeUrl(s);
    }

    /**
     * Returns a string object encoded to use in VBScript as a string.
     * <p/>
     * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore unencoded (each
     * substring enclosed in double quotes), and encode all other characters in concatenated calls to chrw(). e.g. foo'
     * will be encoded as "foo"&chrw(39).
     *
     * @param s a string to be encoded for use in a JavaScript context
     * @return the encoded string
     */
    public static String visualBasicScriptEncodeString(String s) {
        return encodeVbs(s);
    }

    /**
     * Returns a string object encoded to be used in an XML attribute.
     * <p/>
     * This method will return characters a-z, A-Z, 0-9, full stop, comma, dash, and underscore unencoded, and encode
     * all other character in decimal entity format (i.e. < is encoded as &#60;).
     *
     * @param s a string to be encoded for use in an XML attribute context
     * @return the encoded string
     */
    public static String xmlAttributeEncode(String s) {
        return encodeXmlAttribute(s);
    }

    /**
     * Returns a string object encoded to use in XML.
     * <p/>
     * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore unencoded, and
     * encode all other character in decimal entity format (i.e. < is encoded as &#60;).
     *
     * @param s a string to be encoded for use in an XML context
     * @return the encoded string
     */
    public static String xmlEncode(String s) {
        return encodeXml(s);
    }

    private static String leftPad(String stringToPad, String padder, int size) {
        if (padder.length() == 0) {
            return stringToPad;
        }
        strb = new StringBuffer(size);
        sci = new StringCharacterIterator(padder);

        while (strb.length() < (size - stringToPad.length())) {
            for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
                if (strb.length() < size - stringToPad.length()) {
                    strb.insert(strb.length(), String.valueOf(ch));
                }
            }
        }
        return strb.append(stringToPad).toString();
    }

    public static void main(String[] args) {
        System.out.println(htmlEncode("ccc🏀aaa"));
        String str = "‮123";
        System.out.println(htmlEncode(str));
    }
}
