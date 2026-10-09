<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Sign in to your ClinicFlow workspace — the calmer way to manage care.">
  <title>Sign in · ClinicFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="login-page">
  <main class="login-shell">

    <!-- ── Left: Story Panel ── -->
    <section class="login-story" aria-hidden="true">
      <div class="decor-one"></div>
      <div class="decor-two"></div>
      <div class="decor-three"></div>

      <a class="brand brand-light" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">✚</span>
        <span>Clinic<span>Flow</span></span>
      </a>

      <div class="story-copy">
        <span class="eyebrow">CARE, CONNECTED</span>
        <h1>Better care starts with a clearer picture.</h1>
        <p>One thoughtful workspace for everyone who keeps your clinic moving — doctors, staff and patients alike.</p>

        <div class="story-features">
          <div class="story-feature">
            <div class="story-feature-dot">✦</div>
            Unified patient record management
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">⚕</div>
            Smart appointment scheduling
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">▦</div>
            Role-based secure access
          </div>
        </div>
      </div>

      <div class="story-footer">
        <span class="status-dot"></span>
        Your clinic, always in sync.
      </div>
    </section>

    <!-- ── Right: Form Panel ── -->
    <section class="login-panel">
      <div class="mobile-brand">
        <span class="brand-mark">✚</span>
        ClinicFlow
      </div>

      <div class="login-form-wrap">
        <span class="eyebrow">WELCOME BACK</span>
        <h2>Sign in to your workspace</h2>
        <p class="muted">Enter your credentials to continue.</p>

        <% if ("1".equals(request.getParameter("loggedOut"))) { %>
          <div class="notice success" role="status">You have been signed out successfully.</div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
          <div class="notice error" role="alert"><%= request.getAttribute("error") %></div>
        <% } %>

        <form id="login-form" method="post" action="${pageContext.request.contextPath}/login" class="form-stack">

          <div class="form-group">
            <label for="email">Email address</label>
            <input id="email" name="email" type="email"
                   autocomplete="username"
                   placeholder="you@clinic.com"
                   required maxlength="150">
          </div>

          <div class="form-group">
            <div class="password-label">
              <label for="password">Password</label>
            </div>
            <input id="password" name="password" type="password"
                   autocomplete="current-password"
                   placeholder="Enter your password"
                   required>
          </div>

          <button id="login-btn" class="btn btn-primary btn-full" type="submit">
            Sign in &nbsp;→
          </button>
        </form>

        <p class="form-note">
          New to ClinicFlow?
          <a href="${pageContext.request.contextPath}/register">Create a patient account</a>
        </p>
      </div>

      <footer class="login-legal">© ClinicFlow · A calmer way to care.</footer>
    </section>

  </main>
</body>
</html>
