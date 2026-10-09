package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/doctor/*", "/patient/*", "/staff/*", "/profile", "/change-password"})
public class AuthFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String expected = path.startsWith("/admin/") ? "ADMIN"
                : path.startsWith("/doctor/") ? "DOCTOR"
                : path.startsWith("/patient/") ? "PATIENT"
                : path.startsWith("/staff/") ? "STAFF" : null;
        if (expected != null && !expected.equals(user.getRole().name())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this page.");
            return;
        }
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        chain.doFilter(request, response);
    }
}
