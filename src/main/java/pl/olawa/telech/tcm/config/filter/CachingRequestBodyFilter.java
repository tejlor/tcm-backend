package pl.olawa.telech.tcm.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/*
 * Filtr dodający Wrappera na request, umożliwiający ponowny odczyt zawartości (niezbędny do logowania requestu).
 */
@Component
public class CachingRequestBodyFilter extends GenericFilterBean {
    
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {		
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        RequestWrapper wrappedRequest = new RequestWrapper(currentRequest);
        chain.doFilter(wrappedRequest, servletResponse);
    }
	
}
