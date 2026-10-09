<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Update your password — ClinicFlow account security.">
  <title>Change password · ClinicFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="login-page">
  <main class="login-shell" style="min-height:540px; max-width:900px;">

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
        <span class="eyebrow">ACCOUNT SECURITY</span>
        <h1>A little care goes a long way.</h1>
        <p>Keep your account secure with a strong, unique password. We recommend updating it regularly.</p>

        <div class="story-features" style="margin-top:24px;">
          <div class="story-feature">
            <div class="story-feature-dot">✦</div>
            At least 8 characters
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">✦</div>
            Mix of letters, numbers &amp; symbols
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">✦</div>
            Avoid using personal information
          </div>
        </div>
      </div>

      <div class="story-footer">
        <span class="status-dot"></span>
        Care, connected.
      </div>
    </section>

    <!-- ── Right: Form Panel ── -->
    <section class="login-panel">
      <div class="mobile-brand">
        <span class="brand-mark">✚</span>
        ClinicFlow
      </div>

      <div class="login-form-wrap">
        <span class="eyebrow">ACCOUNT SETTINGS</span>
        <h2>Change password</h2>
        <p class="muted">Confirm your current password to continue.</p>

        <c:if test="${not empty success}">
          <div class="notice success"><c:out value="${success}"/></div>
        </c:if>
        <c:if test="${not empty error}">
          <div class="notice error"><c:out value="${error}"/></div>
        </c:if>

        <form id="change-password-form" method="post"
              action="${pageContext.request.contextPath}/change-password"
              class="form-stack">

          <div class="form-group">
            <label for="currentPassword">Current password</label>
            <input id="currentPassword" name="currentPassword"
                   type="password"
                   autocomplete="current-password"
                   placeholder="Enter your current password"
                   required>
          </div>

          <div class="form-divider"></div>

          <div class="form-group">
            <label for="newPassword">New password</label>
            <input id="newPassword" name="newPassword"
                   type="password"
                   autocomplete="new-password"
                   placeholder="At least 8 characters"
                   minlength="8" required>
            <span class="form-hint">Use a mix of letters, numbers and symbols.</span>
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirm new password</label>
            <input id="confirmPassword" name="confirmPassword"
                   type="password"
                   autocomplete="new-password"
                   placeholder="Repeat your new password"
                   minlength="8" required>
          </div>

          <button id="update-password-btn" class="btn btn-primary btn-full" type="submit">
            Update password
          </button>
        </form>

        <p class="form-note">
          <a href="${pageContext.request.contextPath}/profile">Back to profile</a>
          &nbsp;·&nbsp;
          <a href="${pageContext.request.contextPath}/dashboard">Go to workspace</a>
        </p>
      </div>

      <footer class="login-legal">© ClinicFlow · A calmer way to care.</footer>
    </section>

  </main>
</body>
</html>
