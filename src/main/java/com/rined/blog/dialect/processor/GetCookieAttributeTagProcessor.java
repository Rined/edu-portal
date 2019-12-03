package com.rined.blog.dialect.processor;

import com.rined.blog.utils.CookieUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

public class GetCookieAttributeTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "get";
    private static final int PRECEDENCE = 10000;

    GetCookieAttributeTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                PRECEDENCE,
                true);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag iProcessableElementTag,
                             AttributeName attributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {
        IExpressionObjects expressionObjects = context.getExpressionObjects();
        HttpServletRequest httpServletRequest =
                (HttpServletRequest) expressionObjects.getObject("httpServletRequest");
        Optional<String> cookieValue = CookieUtil.getCookieValue(httpServletRequest, attributeValue);
        cookieValue.ifPresent(s -> structureHandler.setBody(s, false));
    }
}
