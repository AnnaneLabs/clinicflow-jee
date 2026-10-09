<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Patient Appointments · ClinicFlow Doctor Console</title>
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
        <a class="nav-item active" href="${pageContext.request.contextPath}/doctor/appointments">
          <span class="nav-icon">📅</span> Patient Appointments
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/availability">
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
          <span>Doctor Portal</span> <span>/</span> <strong>Patient Appointments</strong>
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
            <span class="eyebrow">AGENDA &amp; APPOINTMENTS</span>
            <h1>Patient Consultation Schedule</h1>
            <p class="muted">Review and manage your scheduled patient consultations.</p>
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

        <section class="panel">
          <div class="panel-heading">
            <div>
              <h2>Scheduled Consultations</h2>
              <p>Appointments booked by patients for your specialty.</p>
            </div>
            <span class="subtle-pill">${appointments.size()} Total</span>
          </div>

          <c:choose>
            <c:when test="${empty appointments}">
              <div class="empty-state">
                <div class="empty-icon">📅</div>
                <strong>No patient appointments scheduled</strong>
                <p>Appointments booked by patients will appear here automatically.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table class="no-wrap">
                  <thead>
                    <tr>
                      <th>Date &amp; Time</th>
                      <th>Patient Name</th>
                      <th>Type</th>
                      <th>Reason / Symptoms</th>
                      <th>Status</th>
                      <th style="text-align:right;">Update Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="app" items="${appointments}">
                      <tr>
                        <td>
                          <span class="table-date"><c:out value="${app.startTime.toLocalDate()}"/></span>
                          <span class="table-time">
                            <c:out value="${app.startTime.toLocalTime()}"/> - <c:out value="${app.endTime.toLocalTime()}"/>
                          </span>
                        </td>
                        <td>
                          <div style="font-weight:700; color:var(--dark);">
                            <c:out value="${app.patient.user.firstName}"/> <c:out value="${app.patient.user.lastName}"/>
                          </div>
                          <div style="font-size:11px; color:var(--muted-text);">
                            CIN: <c:out value="${app.patient.cin}"/> &bull; Phone: <c:out value="${app.patient.user.phone}"/>
                          </div>
                        </td>
                        <td>
                          <span class="subtle-pill"><c:out value="${app.type}"/></span>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${not empty app.reason}"><c:out value="${app.reason}"/></c:when>
                            <c:otherwise><span class="muted">General consultation</span></c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <span class="status-pill <c:if test="${app.status.name() == 'CANCELED'}">inactive</c:if> <c:if test="${app.status.name() == 'DONE'}">pending</c:if>">
                            <c:out value="${app.status}"/>
                          </span>
                        </td>
                        <td style="text-align:right;">
                          <form method="post" action="${pageContext.request.contextPath}/doctor/appointments" style="display:inline-flex; gap:6px; align-items:center;">
                            <input type="hidden" name="appointmentId" value="${app.id}">
                            <select name="status" style="padding:4px 8px; font-size:12px; width:auto;" onchange="this.form.submit()">
                              <c:forEach var="st" items="${statuses}">
                                <option value="${st}" <c:if test="${app.status == st}">selected</c:if>>${st}</option>
                              </c:forEach>
                            </select>
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

      </main>
    </div>
  </div>
</body>
</html>
