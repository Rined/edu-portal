package com.rined.portal.dialect.object;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

@Component
@SuppressWarnings("ObviousNullCheck")
public class CookieObjectDialect extends AbstractDialect implements IExpressionObjectDialect {

    protected CookieObjectDialect() {
        super("Cookie Object Dialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new CookieObjectFactory();
    }
}
