package com.tutorias.uaa.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class FiltroCors
 */
public class FiltroCors implements Filter {

	public void destroy() {}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse respuesta = (HttpServletResponse) response;
		respuesta.addHeader("Access-Control-Allow-Origin", "*");
		respuesta.addHeader("Access-Control-Allow-Headers", "*");
		respuesta.addHeader("Access-Control-Allow-Methods", "*");
		chain.doFilter(request, response);
	}
	
	public void init(FilterConfig fConfig) throws ServletException {}
}
