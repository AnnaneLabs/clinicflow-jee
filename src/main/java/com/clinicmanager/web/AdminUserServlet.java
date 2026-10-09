package com.clinicmanager.web;

import com.clinicmanager.model.User;
import com.clinicmanager.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    public void init() {
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userRepository.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("toggleActive".equalsIgnoreCase(action)) {
            try {
                Long userId = Long.parseLong(req.getParameter("userId"));
                User user = userRepository.findById(userId).orElseThrow();
                user.setActive(!user.isActive());
                userRepository.update(user);
                req.getSession().setAttribute("flashSuccess", "User account status updated.");
            } catch (RuntimeException e) {
                req.getSession().setAttribute("flashError", "Failed to update account status: " + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
