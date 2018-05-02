package org.woehlke.twitterwall.frontend.errorhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.woehlke.twitterwall.frontend.content.Symbols;
import org.woehlke.twitterwall.frontend.content.ContentFactory;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by tw on 17.06.17.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(HttpServletRequest request, Exception ex) {
        log.warn("EntityNotFoundException occured :: URL=" + request.getRequestURL());
        log.warn(ex.getMessage());
        return getTemplate(request, ex);
    }

    /*
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final HttpServletRequest request, final Throwable throwable, final Model model) {
        log.error("Exception during execution of SpringSecurity application", throwable);
        ModelAndView mav = new ModelAndView();
        String symbol = Symbols.EXCEPTION.toString();
        String title = "Exception";
        String subtitle = throwable.getMessage();
        mav = contentFactory.setupPage(mav, title, subtitle, symbol);
        mav.addObject("exception", throwable);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("exceptionhandler/persistentObjectNotFound");
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        mav.addObject("errorMessage", errorMessage);
        return mav;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleIllegalArgumentException(HttpServletRequest request, Exception ex) {
        log.warn("IllegalArgumentException occured :: URL=" + request.getRequestURL());
        log.warn(ex.getMessage());
        return getTemplate(request, ex);
    }
    */

    private ModelAndView getTemplate(HttpServletRequest request, Exception ex) {
        ModelAndView mav = new ModelAndView();
        String symbol = Symbols.EXCEPTION.toString();
        String title = "Exception";
        String subtitle = ex.getMessage();
        mav = contentFactory.setupPage(mav, title, subtitle, symbol);
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("exceptionhandler/persistentObjectNotFound");
        return mav;
    }

    @Autowired
    public GlobalExceptionHandler(ContentFactory contentFactory) {
        this.contentFactory = contentFactory;
    }

    private final ContentFactory contentFactory;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

}
