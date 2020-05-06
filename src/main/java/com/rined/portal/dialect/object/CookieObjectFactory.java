package com.rined.portal.dialect.object;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

public class CookieObjectFactory implements IExpressionObjectFactory {
    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.singleton("cookie");
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        IExpressionObjects expressionObjects = context.getExpressionObjects();
        HttpServletResponse response =
                (HttpServletResponse) expressionObjects.getObject("response");
        HttpServletRequest request =
                (HttpServletRequest) expressionObjects.getObject("httpServletRequest");
        return new CookieControlObject(request, response);
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return false;
    }
}
