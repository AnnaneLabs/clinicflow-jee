<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Absences & Leave · ClinicFlow Doctor Console</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="app-page">
  <div class="app-shell">

    <!-- ── Sidebar ── -->
    <aside class="sidebar">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">✚</span>
        <span>Clinic<span>Flow</span></span>
      </a>

      <div class="workspace-label">DOCTOR PORTAL</div>

      <nav class="side-nav">
        <a class="nav-item" href="${pageContext.request.contextPath}/dashboard">
          <span class="nav-icon">📊</span> Overview
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/availability">
          <span class="nav-icon">⏰</span> Working Hours
        </a>
        <a class="nav-item active" href="${pageContext.request.contextPath}/doctor/absence">
          <span class="nav-icon">🌴</span> Absences &amp; Leave
        </a>

        <div class="nav-divider"></div>

        <a class="nav-item" href="${pageContext.request.contextPath}/profile">
          <span class="nav-icon">👤</span> Profile Settings
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/change-password">
          <span class="nav-icon">🔒</span> Change Password
        </a>
      </nav>

      <div class="sidebar-bottom">
        <div class="help-card">
          <div class="help-icon">🌴</div>
          <strong>Absence Policy</strong>
          <p>Declaring absences automatically prevents patient appointments from being scheduled during your leave period.</p>
        </div>

        <div class="sidebar-user">
          <div class="avatar"><c:out value="${user.firstName.substring(0,1)}"/></div>
          <div class="user-meta">
            <strong>Dr. <c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/></strong>
            <span>Doctor</span>
          </div>
          <a class="icon-btn" href="${pageContext.request.contextPath}/logout" title="Sign out">🚪</a>
        </div>
      </div>
    </aside>

    <!-- ── Main Content Area ── -->
    <div class="main-area">
      <header class="topbar">
        <div class="breadcrumb">
          <span>Doctor Portal</span> <span>/</span> <strong>Absences &amp; Leave</strong>
        </div>
        <div class="topbar-right">
          <span class="today-label">Dr. <c:out value="${doctor.user.lastName}"/> (<c:out value="${doctor.specialty.name}"/>)</span>
          <a href="${pageContext.request.contextPath}/profile" class="top-avatar" title="View profile">
            <c:out value="${user.firstName.substring(0,1)}"/>
          </a>
        </div>
      </header>

      <main class="content">
        <div class="welcome-row">
          <div>
            <span class="eyebrow">LEAVE MANAGEMENT</span>
            <h1>Absences &amp; Vacations</h1>
            <p class="muted">Declare your planned leave or unexpected absences to manage clinic scheduling.</p>
          </div>
        </div>

        <c:if test="${not empty sessionScope.success}">
          <div class="notice success" style="margin-bottom:20px;">
            <c:out value="${sessionScope.success}"/>
            <% session.removeAttribute("success"); %>
          </div>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
          <div class="notice error" style="margin-bottom:20px;">
            <c:out value="${sessionScope.error}"/>
            <% session.removeAttribute("error"); %>
          </div>
        </c:if>

        <div class="dashboard-grid">

          <!-- Left Column: Absences List -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Declared Absence Periods</h2>
                <p>History and upcoming leave declarations.</p>
              </div>
              <span class="subtle-pill">${absences.size()} Records</span>
            </div>

            <c:choose>
              <c:when test="${empty absences}">
                <div class="empty-state">
                  <div class="empty-icon">🌴</div>
                  <strong>No absence declarations</strong>
                  <p>Declare leave or vacations using the form when you are unavailable for patient appointments.</p>
                </div>
              </c:when>
              <c:otherwise>
                <div class="table-wrap">
                  <table class="no-wrap">
                    <thead>
                      <tr>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Reason / Notes</th>
                        <th style="text-align:right;">Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="ab" items="${absences}">
                        <tr>
                          <td><strong><c:out value="${ab.startDate}"/></strong></td>
                          <td><strong><c:out value="${ab.endDate}"/></strong></td>
                          <td>
                            <c:choose>
                              <c:when test="${not empty ab.reason}">
                                <c:out value="${ab.reason}"/>
                              </c:when>
                              <c:otherwise>
                                <span class="muted">No reason specified</span>
                              </c:otherwise>
                            </c:choose>
                          </td>
                          <td style="text-align:right;">
                            <form method="post" action="${pageContext.request.contextPath}/doctor/absence" style="display:inline;">
                              <input type="hidden" name="action" value="delete">
                              <input type="hidden" name="id" value="${ab.id}">
                              <button type="submit" class="btn btn-outline" style="color:var(--error-text); border-color:var(--error-border); padding:4px 10px; font-size:12px;" onclick="return confirm('Are you sure you want to cancel this absence declaration?');">
                                Cancel
                              </button>
                            </form>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </c:otherwise>
            </c:choose>
          </section>

          <!-- Right Column: Declare Absence Form -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Declare Leave / Absence</h2>
                <p>Submit a new leave period.</p>
              </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/doctor/absence" class="form-stack">
              <div class="form-group">
                <label for="startDate">Start Date</label>
                <input type="date" id="startDate" name="startDate" required>
              </div>

              <div class="form-group">
                <label for="endDate">End Date</label>
                <input type="date" id="endDate" name="endDate" required>
              </div>

              <div class="form-group">
                <label for="reason">Reason / Leave Note (Optional)</label>
                <textarea id="reason" name="reason" rows="3" placeholder="e.g. Annual leave, conference, personal leave..."></textarea>
              </div>

              <button type="submit" class="btn btn-primary btn-full" style="margin-top:10px;">
                + Declare Absence
              </button>
            </form>
          </section>

        </div>
      </main>
    </div>
  </div>

  <script>
    document.addEventListener("DOMContentLoaded", function() {
      const today = new Date().toISOString().split('T')[0];
      const startInput = document.getElementById("startDate");
      const endInput = document.getElementById("endDate");
      if (startInput && !startInput.value) startInput.value = today;
      if (endInput && !endInput.value) endInput.value = today;
    });
  </script>
</body>
</html>
