package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.exception.AccountDisabledException;
import com.clinicmanager.exception.InvalidCredentialsException;
import com.clinicmanager.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override public void init() { authService = new AuthService(); }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) != null &&
                req.getSession(false).getAttribute("user") instanceof UserDTO user) {
            resp.sendRedirect(req.getContextPath() + dashboardFor(user));
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            UserDTO user = authService.login(email, password);
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + dashboardFor(user));
        } catch (InvalidCredentialsException e) {
            req.setAttribute("error", "The email or password you entered is incorrect.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        } catch (AccountDisabledException e) {
            req.setAttribute("error", "This account is disabled. Please contact the clinic administrator.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            getServletContext().log("Login failed due to an application error.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "We couldn't sign you in right now. Please try again later.");
        }
    }

    static String dashboardFor(UserDTO user) {
        return switch (user.getRole()) {
            case ADMIN -> "/admin/dashboard";
            case DOCTOR -> "/doctor/dashboard";
            case PATIENT -> "/patient/dashboard";
            case STAFF -> "/staff/dashboard";
        };
    }
}
