package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private AuthService authService;
    @Override public void init() { authService = new AuthService(); }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserDTO current = (UserDTO) req.getSession(false).getAttribute("user");
        try {
            UserDTO updated = authService.updateProfile(current.getId(), req.getParameter("firstName"),
                    req.getParameter("lastName"), req.getParameter("phone"));
            req.getSession(false).setAttribute("user", updated);
            req.setAttribute("success", "Your profile has been updated.");
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage() == null ? "Profile update failed." : e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }
}
