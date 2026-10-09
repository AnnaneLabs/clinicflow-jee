package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private AuthService authService;
    @Override public void init() { authService = new AuthService(); }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserDTO current = (UserDTO) req.getSession(false).getAttribute("user");
        String next = req.getParameter("newPassword");
        if (next == null || !next.equals(req.getParameter("confirmPassword"))) {
            req.setAttribute("error", "Your new passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(req, resp);
            return;
        }
        try {
            authService.changePassword(current.getId(), req.getParameter("currentPassword"), next);
            req.setAttribute("success", "Your password has been changed.");
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage() == null ? "Password change failed." : e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(req, resp);
    }
}
