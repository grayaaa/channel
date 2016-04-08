package com.netease.channel.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import java.lang.annotation.Annotation;

/**
 *
 */
public abstract class BaseAttributeArgumentResolver implements WebArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(BaseAttributeArgumentResolver.class);

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        String attributeName = null;
        String parameterName = methodParameter.getParameterName();
        Class<?> parameterType = methodParameter.getParameterType();

        Object paramAnn = null;

        Annotation[] paramAnns = null;

        paramAnns = methodParameter.getParameterAnnotations();
        logger.debug("methodParameter.info:{},{},{}", new Object[]{parameterName,
                methodParameter.getParameterIndex(), parameterType});
        for (int j = 0; j < paramAnns.length; j++) {
            paramAnn = paramAnns[j];
            attributeName = getAttribute(paramAnn);
            if (attributeName != null) {
                break;
            }
        }

        if (attributeName == null) {
            return UNRESOLVED;
        }

        Object value = getValue(webRequest, attributeName);
        Object defaultValue = defaultValue(value, parameterType);
        if (paramAnn != null) {
            checkValue(defaultValue, paramAnn);
        }
        logger.debug("{},{},{}", new Object[]{parameterName, value, defaultValue});
        return defaultValue;
    }

    /**
     * Can be used to validate value based on annotation contents. For example, to check if value is required
     *
     * @param value    the value returned from the request
     * @param paramAnn matched annotation
     */
    protected void checkValue(Object value, Object paramAnn) {
    }

    protected abstract Object getValue(NativeWebRequest webRequest, String attributeName);

    /**
     * if the parameter.oriValue is null and the the parameter.type is primative, just return the primative value
     *
     * @param oriValue
     * @param parameterType
     * @return
     */
    protected Object defaultValue(Object oriValue, Class<?> parameterType) {
        return oriValue;
    }

    protected abstract String getAttribute(Object paramAnn);

}
