<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="${roleTitle} workspace — ClinicFlow.">
  <title>${roleTitle} workspace · ClinicFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="app-page">
<div class="app-shell">

  <!-- ──────────────────── SIDEBAR ──────────────────── -->
  <aside class="sidebar" id="sidebar">
    <a class="brand" href="#">
      <span class="brand-mark">✚</span>
      <span>Clinic<span>Flow</span></span>
    </a>

    <div class="workspace-label">Workspace</div>
    <nav class="side-nav" aria-label="Workspace navigation">
      <a class="nav-item active" id="nav-overview" href="#">
        <span class="nav-icon">▦</span> Overview
      </a>
      <a class="nav-item" id="nav-profile" href="${pageContext.request.contextPath}/profile">
        <span class="nav-icon">♙</span> My Profile
      </a>
    </nav>

    <div class="sidebar-bottom">
      <div class="sidebar-user">
        <div class="avatar">${sessionScope.user.firstName.substring(0,1)}</div>
        <div class="user-meta">
          <strong>${sessionScope.user.fullName}</strong>
          <span>${roleTitle}</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/logout">
          <button class="icon-btn" title="Sign out" aria-label="Sign out">↗</button>
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
          <div class="eyebrow">YOUR CLINIC WORKSPACE</div>
          <h1>Welcome back, ${sessionScope.user.firstName} <span class="wave">✦</span></h1>
          <p class="muted">You're signed in to your ${roleTitle} workspace. More features coming soon.</p>
        </div>
        <div class="date-chip">
          <span>◷</span> ${roleTitle} dashboard
        </div>
      </div>

      <!-- Status panel -->
      <div class="dashboard-grid" style="grid-template-columns: 1fr 340px;">
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>${roleTitle} Dashboard</h2>
              <p>Your role-protected workspace is ready</p>
            </div>
            <span class="healthy-dot" title="Workspace active"></span>
          </div>

          <div class="empty-state">
            <div class="empty-icon" style="width:60px;height:60px;font-size:28px;">✦</div>
            <strong>Your workspace is ready to grow</strong>
            <p>Role-specific appointment and schedule features will be connected in the next phase of development.</p>
          </div>
        </article>

        <!-- Quick links -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Quick actions</h2>
              <p>Common tasks at your fingertips</p>
            </div>
          </div>

          <div style="display:flex; flex-direction:column; gap:10px;">
            <a href="${pageContext.request.contextPath}/profile"
               class="btn btn-ghost" style="justify-content:flex-start; gap:12px;">
              <span style="font-size:16px;">♙</span> My Profile
            </a>
            <a href="${pageContext.request.contextPath}/change-password"
               class="btn btn-ghost" style="justify-content:flex-start; gap:12px;">
              <span style="font-size:16px;">🔒</span> Change Password
            </a>
            <div style="height:1px;background:var(--border);margin:4px 0;"></div>
            <form method="post" action="${pageContext.request.contextPath}/logout">
              <button type="submit" class="btn btn-danger" style="width:100%; justify-content:flex-start; gap:12px;">
                <span style="font-size:16px;">↗</span> Sign Out
              </button>
            </form>
          </div>
        </article>
      </div>

      <footer class="page-footer">
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
