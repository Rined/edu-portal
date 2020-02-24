package com.rined.portal.controllers;

import com.rined.portal.exceptions.AlreadyExistException;
import com.rined.portal.exceptions.NotFoundException;
import com.rined.portal.properties.ErrorViewTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.StringJoiner;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandlerAdvice {
    private final ErrorViewTemplateProperties errorProperties;

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler({AlreadyExistException.class})
    public ModelAndView alreadyExists(AlreadyExistException e) {
        return createErrorModelAndView("409 Conflict!", e);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ModelAndView notFound(NotFoundException e) {
        return createErrorModelAndView("404 Not found!", e);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public ModelAndView notFound(BindException e) {
        StringJoiner joiner = new StringJoiner("! ", "", "!");
        e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .forEach(joiner::add);
        return createErrorModelAndView("400 Bad request!", joiner.toString());
    }

    private ModelAndView createErrorModelAndView(String code, Exception e) {
        return createErrorModelAndView(code, e.getMessage());
    }

    private ModelAndView createErrorModelAndView(String code, String message) {
        ModelAndView modelAndView = new ModelAndView(errorProperties.getName());
        modelAndView.addObject(errorProperties.getCodeAlias(), code);
        modelAndView.addObject(errorProperties.getDescriptionAlias(), message);
        return modelAndView;
    }

}
