<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="${roleTitle} workspace — ClinicFlow.">
  <title>${roleTitle} Dashboard · ClinicFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="app-page">
<div class="app-shell">

  <!-- ──────────────────── SIDEBAR ──────────────────── -->
  <aside class="sidebar" id="sidebar">
    <a class="brand" href="${pageContext.request.contextPath}/">
      <span class="brand-mark">✚</span>
      <span>Clinic<span>Flow</span></span>
    </a>

    <div class="workspace-label">${roleTitle} Portal</div>
    <nav class="side-nav" aria-label="Workspace navigation">
      <a class="nav-item active" href="${pageContext.request.contextPath}/dashboard">
        <span class="nav-icon">📊</span> Overview
      </a>

      <c:if test="${sessionScope.user.role == 'DOCTOR'}">
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/appointments">
          <span class="nav-icon">📅</span> Patient Appointments
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/availability">
          <span class="nav-icon">⏰</span> Working Hours
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/absence">
          <span class="nav-icon">🌴</span> Absences &amp; Leave
        </a>
      </c:if>

      <c:if test="${sessionScope.user.role == 'PATIENT'}">
        <a class="nav-item" href="${pageContext.request.contextPath}/patient/appointments">
          <span class="nav-icon">📅</span> Book Consultations
        </a>
      </c:if>

      <div class="nav-divider"></div>

      <a class="nav-item" href="${pageContext.request.contextPath}/profile">
        <span class="nav-icon">👤</span> My Profile
      </a>
      <a class="nav-item" href="${pageContext.request.contextPath}/change-password">
        <span class="nav-icon">🔒</span> Change Password
      </a>
    </nav>

    <div class="sidebar-bottom">
      <div class="sidebar-user">
        <div class="avatar">${sessionScope.user.firstName.substring(0,1)}</div>
        <div class="user-meta">
          <strong>${sessionScope.user.fullName}</strong>
          <span>${roleTitle}</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/logout" style="display:inline;">
          <button class="icon-btn" title="Sign out" aria-label="Sign out">🚪</button>
        </form>
      </div>
    </div>
  </aside>

  <!-- ──────────────────── MAIN ──────────────────── -->
  <main class="main-area">
    <header class="topbar">
      <div style="display:flex; align-items:center; gap:12px;">
        <button class="menu-toggle" id="menu-toggle-btn" aria-label="Toggle navigation">☰</button>
        <nav class="breadcrumb" aria-label="Breadcrumb">
          Workspace <span>/</span> <strong>Overview</strong>
        </nav>
      </div>
      <div class="topbar-right">
        <a href="${pageContext.request.contextPath}/profile" class="top-avatar" title="My profile">
          ${sessionScope.user.firstName.substring(0,1)}
        </a>
      </div>
    </header>

    <div class="content">

      <!-- Page header -->
      <div class="welcome-row">
        <div>
          <div class="eyebrow">${roleTitle.toUpperCase()} WORKSPACE</div>
          <h1>Welcome back, ${sessionScope.user.firstName} <span class="wave">👋</span></h1>
          <p class="muted">You are signed in to your ClinicFlow ${roleTitle} Portal.</p>
        </div>
        <div class="date-chip">
          <span>◷</span> ${roleTitle} Portal
        </div>
      </div>

      <!-- Doctor Role Features Grid -->
      <c:if test="${sessionScope.user.role == 'DOCTOR'}">
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap:20px; margin-bottom:28px;">
          
          <a href="${pageContext.request.contextPath}/doctor/appointments" class="stat-card" style="display:block; text-decoration:none;">
            <div class="stat-top">
              <span class="stat-label">PATIENT CONSULTATIONS</span>
              <div class="stat-icon icon-blue">📅</div>
            </div>
            <div style="font-family:'Manrope',sans-serif; font-size:18px; font-weight:800; color:var(--dark); margin:12px 0 6px;">
              Patient Appointments
            </div>
            <p class="muted" style="font-size:12.5px; line-height:1.5; margin-bottom:14px;">
              View upcoming patient appointments, accept, cancel, or update consultation status.
            </p>
            <div style="font-size:12px; font-weight:700; color:#2563EB; display:flex; align-items:center; gap:6px;">
              View Appointments &rarr;
            </div>
          </a>

          <a href="${pageContext.request.contextPath}/doctor/availability" class="stat-card" style="display:block; text-decoration:none;">
            <div class="stat-top">
              <span class="stat-label">SCHEDULE MANAGEMENT</span>
              <div class="stat-icon icon-green">⏰</div>
            </div>
            <div style="font-family:'Manrope',sans-serif; font-size:18px; font-weight:800; color:var(--dark); margin:12px 0 6px;">
              Working Hours
            </div>
            <p class="muted" style="font-size:12.5px; line-height:1.5; margin-bottom:14px;">
              Configure your weekly working days, start &amp; end times, and consultation schedules.
            </p>
            <div style="font-size:12px; font-weight:700; color:var(--primary); display:flex; align-items:center; gap:6px;">
              Manage Schedules &rarr;
            </div>
          </a>

          <a href="${pageContext.request.contextPath}/doctor/absence" class="stat-card" style="display:block; text-decoration:none;">
            <div class="stat-top">
              <span class="stat-label">LEAVE &amp; VACATIONS</span>
              <div class="stat-icon icon-amber">🌴</div>
            </div>
            <div style="font-family:'Manrope',sans-serif; font-size:18px; font-weight:800; color:var(--dark); margin:12px 0 6px;">
              Absences &amp; Leave
            </div>
            <p class="muted" style="font-size:12.5px; line-height:1.5; margin-bottom:14px;">
              Declare upcoming vacations or leave periods to pause patient bookings automatically.
            </p>
            <div style="font-size:12px; font-weight:700; color:#D97706; display:flex; align-items:center; gap:6px;">
              Declare Absences &rarr;
            </div>
          </a>

        </div>
      </c:if>

      <!-- Patient Role Features Grid -->
      <c:if test="${sessionScope.user.role == 'PATIENT'}">
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap:20px; margin-bottom:28px;">
          
          <a href="${pageContext.request.contextPath}/patient/appointments" class="stat-card" style="display:block; text-decoration:none;">
            <div class="stat-top">
              <span class="stat-label">CONSULTATION BOOKING</span>
              <div class="stat-icon icon-green">📅</div>
            </div>
            <div style="font-family:'Manrope',sans-serif; font-size:18px; font-weight:800; color:var(--dark); margin:12px 0 6px;">
              Book a Consultation
            </div>
            <p class="muted" style="font-size:12.5px; line-height:1.5; margin-bottom:14px;">
              Select a specialty doctor, view open time slots, and schedule your clinic appointment.
            </p>
            <div style="font-size:12px; font-weight:700; color:var(--primary); display:flex; align-items:center; gap:6px;">
              Book Appointment Now &rarr;
            </div>
          </a>

        </div>
      </c:if>

      <div class="dashboard-grid" style="grid-template-columns: 1fr 340px;">
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>${roleTitle} Quick Start</h2>
              <p>Key actions and workspace status</p>
            </div>
            <span class="healthy-dot" title="Workspace active"></span>
          </div>

          <div class="system-status">
            <span class="system-symbol">✔</span>
            <div>
              <strong>Your account is active &amp; verified</strong>
              <p>Select an action from the navigation sidebar or quick cards to manage your portal.</p>
            </div>
          </div>
        </article>

        <!-- Quick links -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Quick Actions</h2>
              <p>Direct shortcuts</p>
            </div>
          </div>

          <div style="display:flex; flex-direction:column; gap:10px;">
            <c:if test="${sessionScope.user.role == 'DOCTOR'}">
              <a href="${pageContext.request.contextPath}/doctor/appointments"
                 class="btn btn-outline" style="justify-content:flex-start; gap:12px; border-color:#2563EB; color:#2563EB;">
                <span style="font-size:16px;">📅</span> Patient Appointments
              </a>
              <a href="${pageContext.request.contextPath}/doctor/availability"
                 class="btn btn-outline" style="justify-content:flex-start; gap:12px; border-color:var(--primary); color:var(--primary);">
                <span style="font-size:16px;">⏰</span> Manage Working Hours
              </a>
              <a href="${pageContext.request.contextPath}/doctor/absence"
                 class="btn btn-outline" style="justify-content:flex-start; gap:12px; border-color:#D97706; color:#D97706;">
                <span style="font-size:16px;">🌴</span> Declare Absences
              </a>
              <div style="height:1px;background:var(--border);margin:4px 0;"></div>
            </c:if>

            <c:if test="${sessionScope.user.role == 'PATIENT'}">
              <a href="${pageContext.request.contextPath}/patient/appointments"
                 class="btn btn-outline" style="justify-content:flex-start; gap:12px; border-color:var(--primary); color:var(--primary);">
                <span style="font-size:16px;">📅</span> Book a Consultation
              </a>
              <div style="height:1px;background:var(--border);margin:4px 0;"></div>
            </c:if>

            <a href="${pageContext.request.contextPath}/profile"
               class="btn btn-ghost" style="justify-content:flex-start; gap:12px;">
              <span style="font-size:16px;">👤</span> My Profile
            </a>
            <a href="${pageContext.request.contextPath}/change-password"
               class="btn btn-ghost" style="justify-content:flex-start; gap:12px;">
              <span style="font-size:16px;">🔒</span> Change Password
            </a>
            <div style="height:1px;background:var(--border);margin:4px 0;"></div>
            <form method="post" action="${pageContext.request.contextPath}/logout">
              <button type="submit" class="btn btn-outline" style="width:100%; justify-content:flex-start; gap:12px; color:var(--error-text); border-color:var(--error-border);">
                <span style="font-size:16px;">🚪</span> Sign Out
              </button>
            </form>
          </div>
        </article>
      </div>

      <footer class="page-footer" style="margin-top:40px;">
        <span>© ClinicFlow</span>
        <span>Care, connected.</span>
      </footer>

    </div>
  </main>

</div>

<script>
  document.getElementById('menu-toggle-btn').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('sidebar-open');
  });
</script>
</body>
</html>
