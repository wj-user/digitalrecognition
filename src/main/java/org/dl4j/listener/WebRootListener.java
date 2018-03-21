package org.dl4j.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dl4j.constant.WebConstant;


public class WebRootListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		WebConstant.WEB_ROOT = servletContextEvent.getServletContext().getRealPath("/");
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}