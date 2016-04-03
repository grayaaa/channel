package com.netease.channel.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JSONResponseBuilder {

	private static Logger logger = LoggerFactory
			.getLogger(JSONResponseBuilder.class);

	public static String buildResp(HttpServletResponse response,
			Map<String, Object> result) {
		response.setContentType("application/json;charset=UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			JSONUtil.writeJson(result, writer);
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static void buildRespVar(HttpServletResponse response,
			Map<String, Object> result, String var){
		if (StringUtils.isNotBlank(var)) {
			response.setContentType("text/javascript;charset=UTF-8");
			try {
				PrintWriter writer = response.getWriter();
				writer.append("var ").append(var).append("=")
						.append(JSONUtil.toJson(result)).append(";");
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}else{
			response.setContentType("application/json;charset=UTF-8");
			try {
				PrintWriter writer = response.getWriter();
				JSONUtil.writeJson(result, writer);
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static String buildResp(HttpServletResponse response,
			Map<String, Object> result, String callback) {
		if (StringUtils.isNotBlank(callback)) {
			response.setContentType("text/javascript;charset=UTF-8");
			try {
				PrintWriter writer = response.getWriter();
				writer.append(callback).append('(')
						.append(JSONUtil.toJson(result)).append(");");
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			response.setContentType("application/json;charset=UTF-8");
			try {
				PrintWriter writer = response.getWriter();
				JSONUtil.writeJson(result, writer);
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	public static String buildSuccResp(HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", Const.ErrorCode.SUCCESS);
		response.setContentType("application/json;charset=UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			JSONUtil.writeJson(result, writer);
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String buildErrorResp(HttpServletResponse response,
			Integer errorCode, String errorMessage) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", errorCode);
		result.put("error", errorMessage);
		response.setContentType("application/json;charset=UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			JSONUtil.writeJson(result, writer);
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static void outToJson(Object object) {
        try {
            System.out.println(ChannelSerializer.json.writeValueAsString(object));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
