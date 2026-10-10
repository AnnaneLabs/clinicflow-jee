<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="ClinicFlow administration overview — clinic statistics and recent activity.">
  <title>Overview · ClinicFlow Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="app-page">
<div class="app-shell">

  <!-- ──────────────────── SIDEBAR ──────────────────── -->
  <aside class="sidebar" id="sidebar">
    <a class="brand" href="${pageContext.request.contextPath}/admin/dashboard">
      <span class="brand-mark">✚</span>
      <span>Clinic<span>Flow</span></span>
    </a>

    <div class="workspace-label">Administration</div>
    <nav class="side-nav" aria-label="Admin navigation">
      <a class="nav-item active" id="nav-overview" href="${pageContext.request.contextPath}/admin/dashboard">
        <span class="nav-icon">▦</span> Overview
      </a>
      <a class="nav-item" id="nav-doctors" href="${pageContext.request.contextPath}/admin/doctors">
        <span class="nav-icon">⚕</span> Doctors
      </a>
      <a class="nav-item" id="nav-departments" href="${pageContext.request.contextPath}/admin/departments">
        <span class="nav-icon">▤</span> Departments
      </a>
      <a class="nav-item" id="nav-users" href="${pageContext.request.contextPath}/admin/users">
        <span class="nav-icon">♙</span> User Accounts
      </a>
      <a class="nav-item" id="nav-appointments" href="${pageContext.request.contextPath}/admin/appointments">
        <span class="nav-icon">📅</span> Appointments
      </a>
    </nav>

    <div class="sidebar-bottom">
      <div class="help-card">
        <div class="help-icon">✦</div>
        <strong>Need a hand?</strong>
        <p>Your clinic workspace, all in one place.</p>
      </div>
      <div class="sidebar-user">
        <div class="avatar"><c:out value="${sessionScope.user.firstName.substring(0,1)}"/></div>
        <div class="user-meta">
          <strong><c:out value="${sessionScope.user.fullName}"/></strong>
          <span>Administrator</span>
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
        <span class="today-label">Clinic administration</span>
        <div class="top-avatar" title="${sessionScope.user.fullName}">
          <c:out value="${sessionScope.user.firstName.substring(0,1)}"/>
        </div>
      </div>
    </header>

    <div class="content">

      <!-- Page header -->
      <div class="welcome-row">
        <div>
          <div class="eyebrow">YOUR CLINIC AT A GLANCE</div>
          <h1>Good day, <c:out value="${sessionScope.user.firstName}"/> <span class="wave">✦</span></h1>
          <p class="muted">Here's what's happening across your clinic right now.</p>
        </div>
        <div class="date-chip">
          <span>◷</span> Live overview
        </div>
      </div>

      <!-- Stats row -->
      <section class="stats-grid" aria-label="Clinic statistics">

        <article class="stat-card">
          <div class="stat-top">
            <span class="stat-label">Total patients</span>
            <span class="stat-icon icon-blue">♙</span>
          </div>
          <div class="stat-number"><c:out value="${totalPatients}"/></div>
          <div class="stat-foot">
            <span class="stat-caption">Registered patient records</span>
          </div>
        </article>

        <article class="stat-card">
          <div class="stat-top">
            <span class="stat-label">Medical team</span>
            <span class="stat-icon icon-violet">⚕</span>
          </div>
          <div class="stat-number"><c:out value="${totalDoctors}"/></div>
          <div class="stat-foot">
            <span class="stat-caption">Active doctors on staff</span>
          </div>
        </article>

        <article class="stat-card">
          <div class="stat-top">
            <span class="stat-label">Appointments</span>
            <span class="stat-icon icon-green">▦</span>
          </div>
          <div class="stat-number"><c:out value="${totalAppointments}"/></div>
          <div class="stat-foot">
            <strong><c:out value="${plannedAppointments}"/></strong> planned upcoming
          </div>
        </article>

        <article class="stat-card">
          <div class="stat-top">
            <span class="stat-label">Active accounts</span>
            <span class="stat-icon icon-amber">◎</span>
          </div>
          <div class="stat-number"><c:out value="${activeUsers}"/></div>
          <div class="stat-foot">
            Of <strong><c:out value="${totalUsers}"/></strong> total accounts
          </div>
        </article>

      </section>

      <!-- Main panels grid -->
      <section class="dashboard-grid">

        <!-- Recent appointments -->
        <article class="panel appointments-panel">
          <div class="panel-heading">
            <div>
              <h2>Recent appointments</h2>
              <p>Latest scheduled visits in your clinic</p>
            </div>
            <span class="subtle-pill">Latest 6</span>
          </div>

          <c:choose>
            <c:when test="${empty recentAppointments}">
              <div class="empty-state">
                <div class="empty-icon">▦</div>
                <strong>No appointments yet</strong>
                <p>When appointments are created they'll appear here.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>Reference</th>
                      <th>Date &amp; Time</th>
                      <th>Type</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="appointment" items="${recentAppointments}">
                      <tr>
                        <td><span class="ref-id">#APT-<c:out value="${appointment.id}"/></span></td>
                        <td>
                          <span class="table-date"><c:out value="${appointment.startTime.toLocalDate()}"/></span>
                          <span class="table-time"><c:out value="${appointment.startTime.toLocalTime()}"/></span>
                        </td>
                        <td><c:out value="${appointment.type}"/></td>
                        <td>
                          <span class="status-pill"><c:out value="${appointment.status}"/></span>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
        </article>

        <!-- Quick summary panel -->
        <article class="panel quick-panel">
          <div class="panel-heading">
            <div>
              <h2>Workspace status</h2>
              <p>System health at a glance</p>
            </div>
            <span class="healthy-dot" title="All systems operational"></span>
          </div>

          <div class="system-status">
            <div class="system-symbol">✓</div>
            <div>
              <strong>Admin workspace ready</strong>
              <p>Signed in with administrator access.</p>
            </div>
          </div>

          <div class="quick-divider"></div>
          <h3>Quick summary</h3>

          <div class="summary-line">
            <span>Patient records</span>
            <strong><c:out value="${totalPatients}"/></strong>
          </div>
          <div class="summary-line">
            <span>Doctor accounts</span>
            <strong><c:out value="${totalDoctors}"/></strong>
          </div>
          <div class="summary-line">
            <span>Active user accounts</span>
            <strong><c:out value="${activeUsers}"/></strong>
          </div>
          <div class="summary-line">
            <span>Planned appointments</span>
            <strong><c:out value="${plannedAppointments}"/></strong>
          </div>

          <div class="quick-note">
            <span>✦</span>
            <p>Keep clinic information accurate to help your team work smoothly.</p>
          </div>
        </article>

      </section>

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
