<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Create your ClinicFlow patient account and start managing your health journey.">
  <title>Create account · ClinicFlow</title>
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
        <span class="eyebrow">WELCOME TO CLINICFLOW</span>
        <h1>Your care journey, all in one place.</h1>
        <p>Join thousands of patients managing their health with confidence, clarity and calm.</p>

        <div class="story-features">
          <div class="story-feature">
            <div class="story-feature-dot">✦</div>
            Book appointments in seconds
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">⚕</div>
            Access your medical history
          </div>
          <div class="story-feature">
            <div class="story-feature-dot">▦</div>
            Secure, private and encrypted
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
        <span class="eyebrow">GET STARTED</span>
        <h2>Create your account</h2>
        <p class="muted">A few details and you're all set — takes under a minute.</p>

        <% if (request.getAttribute("error") != null) { %>
          <div class="notice error" role="alert">
            <%= org.apache.commons.text.StringEscapeUtils.escapeHtml4(String.valueOf(request.getAttribute("error"))) %>
          </div>
        <% } %>

        <form id="register-form" method="post" action="${pageContext.request.contextPath}/register" class="form-stack">

          <div class="form-row">
            <div class="form-group">
              <label for="firstName">First name</label>
              <input id="firstName" name="firstName"
                     autocomplete="given-name"
                     placeholder="Sarah"
                     required maxlength="80">
            </div>
            <div class="form-group">
              <label for="lastName">Last name</label>
              <input id="lastName" name="lastName"
                     autocomplete="family-name"
                     placeholder="Mitchell"
                     required maxlength="80">
            </div>
          </div>

          <div class="form-group">
            <label for="email">Email address</label>
            <input id="email" name="email" type="email"
                   autocomplete="email"
                   placeholder="you@example.com"
                   required maxlength="150">
          </div>

          <div class="form-group">
            <label for="phone">Phone <span style="font-weight:400; color:var(--gray)">(optional)</span></label>
            <input id="phone" name="phone" type="tel"
                   autocomplete="tel"
                   placeholder="+1 555 000 0000"
                   maxlength="30">
          </div>

          <div class="form-divider"></div>

          <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password"
                   autocomplete="new-password"
                   placeholder="At least 8 characters"
                   required minlength="8">
            <span class="form-hint">Use a mix of letters, numbers and symbols.</span>
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirm password</label>
            <input id="confirmPassword" name="confirmPassword" type="password"
                   autocomplete="new-password"
                   placeholder="Repeat your password"
                   required>
          </div>

          <button id="register-btn" class="btn btn-primary btn-full" type="submit">
            Create account &nbsp;→
          </button>
        </form>

        <p class="form-note">
          Already have an account?
          <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </p>
      </div>

      <footer class="login-legal">© ClinicFlow · A calmer way to care.</footer>
    </section>

  </main>
</body>
</html>
