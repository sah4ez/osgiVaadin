package org.vaadin.osgi.example;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = VaadinOSGiUI.class)
public class VaadinOSGiServlet extends VaadinServlet {

    /**
     *
     */
    private static final long serialVersionUID = -7204996277655209911L;
}