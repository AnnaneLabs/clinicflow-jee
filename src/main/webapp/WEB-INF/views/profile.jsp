<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Update your profile details — ClinicFlow account settings.">
  <title>My Profile · ClinicFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="login-page">
  <main class="login-shell" style="min-height:580px; max-width:900px;">

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
        <span class="eyebrow">YOUR ACCOUNT</span>
        <h1>Your details, kept up to date.</h1>
        <p>Manage the personal information associated with your ClinicFlow account.</p>
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
        <h2>My profile</h2>
        <p class="muted">Update your personal details below.</p>

        <!-- Profile avatar display -->
        <div class="profile-avatar-row" style="margin-top:16px;">
          <div class="profile-avatar-lg">
            <c:out value="${sessionScope.user.firstName.substring(0,1)}"/>
          </div>
          <div>
            <div style="font-weight:700;font-size:14px;color:var(--dark);">
              <c:out value="${sessionScope.user.fullName}"/>
            </div>
            <div style="font-size:12px;color:var(--muted-text);">
              <c:out value="${sessionScope.user.email}"/>
            </div>
            <div style="font-size:11px;color:var(--primary);font-weight:600;margin-top:3px;">
              <c:out value="${sessionScope.user.role}"/>
            </div>
          </div>
        </div>

        <c:if test="${not empty success}">
          <div class="notice success"><c:out value="${success}"/></div>
        </c:if>
        <c:if test="${not empty error}">
          <div class="notice error"><c:out value="${error}"/></div>
        </c:if>

        <form id="profile-form" method="post"
              action="${pageContext.request.contextPath}/profile"
              class="form-stack">

          <div class="form-row">
            <div class="form-group">
              <label for="firstName">First name</label>
              <input id="firstName" name="firstName"
                     value="<c:out value='${sessionScope.user.firstName}'/>"
                     required maxlength="80">
            </div>
            <div class="form-group">
              <label for="lastName">Last name</label>
              <input id="lastName" name="lastName"
                     value="<c:out value='${sessionScope.user.lastName}'/>"
                     required maxlength="80">
            </div>
          </div>

          <div class="form-group">
            <label for="email">Email address</label>
            <input id="email" value="<c:out value='${sessionScope.user.email}'/>" disabled>
            <span class="form-hint">Email cannot be changed. Contact your administrator.</span>
          </div>

          <div class="form-group">
            <label for="phone">Phone number</label>
            <input id="phone" name="phone"
                   value="<c:out value='${sessionScope.user.phone}'/>"
                   placeholder="e.g. +1 555 000 0000"
                   maxlength="30">
          </div>

          <button id="save-profile-btn" class="btn btn-primary btn-full" type="submit">
            Save changes
          </button>
        </form>

        <p class="form-note">
          <a href="${pageContext.request.contextPath}/change-password">Change password</a>
          &nbsp;·&nbsp;
          <a href="${pageContext.request.contextPath}/dashboard">Back to workspace</a>
        </p>
      </div>

      <footer class="login-legal">© ClinicFlow · A calmer way to care.</footer>
    </section>

  </main>
</body>
</html>
