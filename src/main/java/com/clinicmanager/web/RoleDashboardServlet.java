package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/doctor/dashboard", "/patient/dashboard", "/staff/dashboard"})
public class RoleDashboardServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String view = path.startsWith("/doctor/") ? "doctor"
                : path.startsWith("/patient/") ? "patient" : "staff";
        req.setAttribute("roleTitle", view.substring(0, 1).toUpperCase() + view.substring(1));
        req.getRequestDispatcher("/WEB-INF/views/role-dashboard.jsp").forward(req, resp);
    }
}
