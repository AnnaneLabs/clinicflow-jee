package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"", "/index"})
public class IndexServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Object current = session == null ? null : session.getAttribute("user");
        if (current instanceof UserDTO user) {
            resp.sendRedirect(req.getContextPath() + LoginServlet.dashboardFor(user));
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
