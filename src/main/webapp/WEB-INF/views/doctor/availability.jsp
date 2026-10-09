<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Weekly Availabilities · ClinicFlow Doctor Console</title>
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
        <a class="nav-item active" href="${pageContext.request.contextPath}/doctor/availability">
          <span class="nav-icon">⏰</span> Working Hours
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/absence">
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
          <div class="help-icon">💡</div>
          <strong>Availability Tip</strong>
          <p>Patients can only book appointment slots during your configured working hours.</p>
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
          <span>Doctor Portal</span> <span>/</span> <strong>Weekly Availabilities</strong>
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
            <span class="eyebrow">SCHEDULE MANAGEMENT</span>
            <h1>Working Hours &amp; Availabilities</h1>
            <p class="muted">Set your weekly consultation hours and active validity periods.</p>
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

          <!-- Left Column: Existing Availabilities List -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Active Schedule Blocks</h2>
                <p>Your current configured working time blocks.</p>
              </div>
              <span class="subtle-pill">${availabilities.size()} Blocks</span>
            </div>

            <c:choose>
              <c:when test="${empty availabilities}">
                <div class="empty-state">
                  <div class="empty-icon">⏰</div>
                  <strong>No availability slots configured</strong>
                  <p>Use the form to add your weekly working hours so patients can book appointments.</p>
                </div>
              </c:when>
              <c:otherwise>
                <div class="table-wrap">
                  <table class="no-wrap">
                    <thead>
                      <tr>
                        <th>Day of Week</th>
                        <th>Time Interval</th>
                        <th>Status</th>
                        <th>Valid From</th>
                        <th>Valid To</th>
                        <th style="text-align:right;">Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="a" items="${availabilities}">
                        <tr>
                          <td><strong><c:out value="${a.dayOfWeek}"/></strong></td>
                          <td>
                            <span class="ref-id">
                              <c:out value="${a.startTime}"/> - <c:out value="${a.endTime}"/>
                            </span>
                          </td>
                          <td>
                            <span class="status-pill <c:if test="${a.status != 'ACTIVE'}">inactive</c:if>">
                              <c:out value="${a.status}"/>
                            </span>
                          </td>
                          <td><c:out value="${a.validFrom}"/></td>
                          <td>
                            <c:choose>
                              <c:when test="${not empty a.validTo}"><c:out value="${a.validTo}"/></c:when>
                              <c:otherwise><span class="muted">No expiry</span></c:otherwise>
                            </c:choose>
                          </td>
                          <td style="text-align:right;">
                            <form method="post" action="${pageContext.request.contextPath}/doctor/availability" style="display:inline;">
                              <input type="hidden" name="action" value="delete">
                              <input type="hidden" name="id" value="${a.id}">
                              <button type="submit" class="btn btn-outline" style="color:var(--error-text); border-color:var(--error-border); padding:4px 10px; font-size:12px;" onclick="return confirm('Are you sure you want to remove this availability block?');">
                                Delete
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

          <!-- Right Column: Add Schedule Form -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Add Working Hours</h2>
                <p>Define a new weekly availability block.</p>
              </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/doctor/availability" class="form-stack">
              <div class="form-group">
                <label for="dayOfWeek">Day of Week</label>
                <select id="dayOfWeek" name="dayOfWeek" required>
                  <c:forEach var="d" items="${daysOfWeek}">
                    <option value="${d}">${d}</option>
                  </c:forEach>
                </select>
              </div>

              <div style="display:grid; grid-template-columns:1fr 1fr; gap:12px;">
                <div class="form-group">
                  <label for="startTime">Start Time</label>
                  <input type="time" id="startTime" name="startTime" value="09:00" required>
                </div>
                <div class="form-group">
                  <label for="endTime">End Time</label>
                  <input type="time" id="endTime" name="endTime" value="17:00" required>
                </div>
              </div>

              <div style="display:grid; grid-template-columns:1fr 1fr; gap:12px;">
                <div class="form-group">
                  <label for="validFrom">Valid From</label>
                  <input type="date" id="validFrom" name="validFrom" required>
                </div>
                <div class="form-group">
                  <label for="validTo">Valid To (Optional)</label>
                  <input type="date" id="validTo" name="validTo" placeholder="Optional">
                </div>
              </div>

              <button type="submit" class="btn btn-primary btn-full" style="margin-top:10px;">
                + Save Schedule Block
              </button>
            </form>
          </section>

        </div>
      </main>
    </div>
  </div>

  <script>
    // Default validFrom to today's date
    document.addEventListener("DOMContentLoaded", function() {
      const today = new Date().toISOString().split('T')[0];
      const validFromInput = document.getElementById("validFrom");
      if (validFromInput && !validFromInput.value) {
        validFromInput.value = today;
      }
    });
  </script>
</body>
</html>
