/*
 * Copyright 2009-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netease.channel.filter;

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the {@link RequestAttribute} to inject an
 * {@link javax.servlet.http.HttpServletRequest} as a method argument.
 */
public class RequestAttributeArgumentResolver extends BaseAttributeArgumentResolver {
    protected String getAttribute(Object paramAnn) {
        String attributeName = null;
        if (RequestAttribute.class.isInstance(paramAnn)) {
            RequestAttribute attribute = (RequestAttribute) paramAnn;
            attributeName = attribute.value();
        }
        return attributeName;
    }

    protected Object getValue(NativeWebRequest webRequest, String attributeName) {
        Object attribute = webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
        return attribute;
    }

    public Object defaultValue(Object oriValue, Class<?> parameterType) {
        if (oriValue == null) {
            if (long.class == parameterType) {
                return 0L;
            } else if (int.class == parameterType) {
                return 0;
            } else if (byte.class == parameterType) {
                return (byte) 0;
            } else if (char.class == parameterType) {
                return '\0';
            } else if (float.class == parameterType) {
                return 0F;
            } else if (double.class == parameterType) {
                return 0D;
            } else if (short.class == parameterType) {
                return (short) 0;
            } else if (boolean.class == parameterType) {
                return false;
            }
        }

        return oriValue;
    }

}
