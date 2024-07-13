package com.framework.controllers;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ControllerInvoker {

    public Object invokeControllerMethod(Object controller, Method method, HttpServletRequest request) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Param paramAnnotation = parameters[i].getAnnotation(Param.class);
            if (paramAnnotation != null) {
                String paramName = paramAnnotation.name();
                String paramValue = request.getParameter(paramName);
                args[i] = convertToParameterType(paramValue, parameters[i].getType());
            }
        }

        return method.invoke(controller, args);
    }

    private Object convertToParameterType(String value, Class<?> type) {
        if (type.equals(String.class)) {
            return value;
        }
        // Ajoutez des conversions pour d'autres types si nécessaire
        throw new IllegalArgumentException("Type de paramètre non pris en charge : " + type);
    }
}
