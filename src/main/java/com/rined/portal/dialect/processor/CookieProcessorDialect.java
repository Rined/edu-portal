package com.rined.portal.dialect.processor;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

@Component
@SuppressWarnings("ObviousNullCheck")
public class CookieProcessorDialect extends AbstractProcessorDialect {

    protected CookieProcessorDialect() {
        super("Cookie dialect", "cookie", 1000);
    }

//    <html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:cookie="http://cookie">
//    <div cookie:get="cookieName"></div> -> <div> cookieValue </div>
    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new GetCookieAttributeTagProcessor(dialectPrefix));
        return processors;
    }
}
