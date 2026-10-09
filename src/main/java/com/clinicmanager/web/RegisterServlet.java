package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthService authService;
    @Override public void init() { authService = new AuthService(); }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        if (password == null || !password.equals(confirm)) {
            req.setAttribute("error", "Your passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }
        try {
            UserDTO user = authService.register(firstName, lastName, email, phone, password);
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + "/patient/dashboard");
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage() == null ? "We couldn't create your account." : e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}
