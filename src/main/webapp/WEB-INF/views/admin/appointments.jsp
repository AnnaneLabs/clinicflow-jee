<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Master Appointments &amp; Reports · ClinicFlow Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body class="app-page">
  <div class="app-shell">

    <!-- ── Sidebar ── -->
    <aside class="sidebar" id="sidebar">
      <a class="brand" href="${pageContext.request.contextPath}/admin/dashboard">
        <span class="brand-mark">✚</span>
        <span>Clinic<span>Flow</span></span>
      </a>

      <div class="workspace-label">Administration</div>
      <nav class="side-nav" aria-label="Admin navigation">
        <a class="nav-item" href="${pageContext.request.contextPath}/admin/dashboard">
          <span class="nav-icon">▦</span> Overview
        </a>
        <a class="nav-item active" href="${pageContext.request.contextPath}/admin/appointments">
          <span class="nav-icon">📅</span> Appointments
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/admin/doctors">
          <span class="nav-icon">⚕</span> Doctors
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/admin/departments">
          <span class="nav-icon">▤</span> Departments
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/admin/users">
          <span class="nav-icon">♙</span> User Accounts
        </a>
      </nav>

      <div class="sidebar-bottom">
        <div class="sidebar-user">
          <div class="avatar"><c:out value="${sessionScope.user.firstName.substring(0,1)}"/></div>
          <div class="user-meta">
            <strong><c:out value="${sessionScope.user.fullName}"/></strong>
            <span>Administrator</span>
          </div>
          <a class="icon-btn" href="${pageContext.request.contextPath}/logout" title="Sign out">🚪</a>
        </div>
      </div>
    </aside>

    <!-- ── Main Content Area ── -->
    <div class="main-area">
      <header class="topbar">
        <div class="breadcrumb">
          <span>Administration</span> <span>/</span> <strong>Clinic Appointments &amp; Reports</strong>
        </div>
        <div class="topbar-right">
          <span class="today-label">Clinic administration</span>
          <a href="${pageContext.request.contextPath}/profile" class="top-avatar" title="View profile">
            <c:out value="${sessionScope.user.firstName.substring(0,1)}"/>
          </a>
        </div>
      </header>

      <main class="content">
        <div class="welcome-row">
          <div>
            <span class="eyebrow">CLINIC MANAGEMENT</span>
            <h1>All Clinic Appointments &amp; Capacity</h1>
            <p class="muted">Master overview of all scheduled, completed, and canceled patient consultations across departments.</p>
          </div>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
          <div class="notice success" style="margin-bottom:20px;">
            <c:out value="${sessionScope.flashSuccess}"/>
            <% session.removeAttribute("flashSuccess"); %>
          </div>
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
          <div class="notice error" style="margin-bottom:20px;">
            <c:out value="${sessionScope.flashError}"/>
            <% session.removeAttribute("flashError"); %>
          </div>
        </c:if>

        <!-- Stats Grid -->
        <section class="stats-grid" style="grid-template-columns: repeat(4, 1fr); margin-bottom:24px;">
          <article class="stat-card">
            <div class="stat-top">
              <span class="stat-label">Total Clinic Bookings</span>
              <span class="stat-icon icon-blue">📅</span>
            </div>
            <div class="stat-number"><c:out value="${totalBookings}"/></div>
            <div class="stat-foot">Master appointment records</div>
          </article>

          <article class="stat-card">
            <div class="stat-top">
              <span class="stat-label">Planned Upcoming</span>
              <span class="stat-icon icon-violet">◷</span>
            </div>
            <div class="stat-number"><c:out value="${plannedCount}"/></div>
            <div class="stat-foot">Scheduled consultations</div>
          </article>

          <article class="stat-card">
            <div class="stat-top">
              <span class="stat-label">Completed (Done)</span>
              <span class="stat-icon icon-green">✔</span>
            </div>
            <div class="stat-number"><c:out value="${doneCount}"/></div>
            <div class="stat-foot">Attended consultations</div>
          </article>

          <article class="stat-card">
            <div class="stat-top">
              <span class="stat-label">Canceled</span>
              <span class="stat-icon icon-rose">✖</span>
            </div>
            <div class="stat-number"><c:out value="${canceledCount}"/></div>
            <div class="stat-foot">Canceled bookings</div>
          </article>
        </section>

        <section class="panel">
          
          <!-- Filter Bar -->
          <div class="panel-heading" style="flex-wrap:wrap; gap:16px;">
            <div>
              <h2>Appointments Master Register</h2>
              <p>Filter clinic appointments by doctor, status, or date.</p>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/admin/appointments" style="display:flex; gap:12px; align-items:center; flex-wrap:wrap;">
              <select name="doctorId" style="padding:6px 12px; font-size:13px; width:auto;">
                <option value="">-- All Doctors --</option>
                <c:forEach var="d" items="${doctors}">
                  <option value="${d.id}" <c:if test="${selectedDoctorId == d.id}">selected</c:if>>
                    <c:out value="${d.title}"/> <c:out value="${d.user.firstName}"/> <c:out value="${d.user.lastName}"/>
                  </option>
                </c:forEach>
              </select>

              <select name="status" style="padding:6px 12px; font-size:13px; width:auto;">
                <option value="">-- All Statuses --</option>
                <c:forEach var="st" items="${statuses}">
                  <option value="${st}" <c:if test="${selectedStatus == st}">selected</c:if>>${st}</option>
                </c:forEach>
              </select>

              <input type="date" name="date" value="${selectedDate}" style="padding:5px 10px; font-size:13px; width:auto;">

              <button type="submit" class="btn btn-primary" style="padding:7px 14px; font-size:13px;">Filter</button>
              <a href="${pageContext.request.contextPath}/admin/appointments" class="btn btn-ghost" style="padding:7px 12px; font-size:13px;">Reset</a>
            </form>
          </div>

          <c:choose>
            <c:when test="${empty appointments}">
              <div class="empty-state">
                <div class="empty-icon">📅</div>
                <strong>No appointments found</strong>
                <p>No clinic appointments matched your filter criteria.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table class="no-wrap">
                  <thead>
                    <tr>
                      <th>Ref ID</th>
                      <th>Date &amp; Time</th>
                      <th>Patient</th>
                      <th>Attending Doctor</th>
                      <th>Department &amp; Specialty</th>
                      <th>Type</th>
                      <th>Status</th>
                      <th style="text-align:right;">Update Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="app" items="${appointments}">
                      <tr>
                        <td><span class="ref-id">#APT-<c:out value="${app.id}"/></span></td>
                        <td>
                          <span class="table-date"><c:out value="${app.startTime.toLocalDate()}"/></span>
                          <span class="table-time"><c:out value="${app.startTime.toLocalTime()}"/> - <c:out value="${app.endTime.toLocalTime()}"/></span>
                        </td>
                        <td>
                          <div style="font-weight:700; color:var(--dark);">
                            <c:out value="${app.patient.user.firstName}"/> <c:out value="${app.patient.user.lastName}"/>
                          </div>
                          <div style="font-size:11px; color:var(--muted-text);">
                            Phone: <c:out value="${app.patient.user.phone}"/>
                          </div>
                        </td>
                        <td>
                          <div style="font-weight:700; color:var(--dark);">
                            <c:out value="${app.doctor.title}"/> <c:out value="${app.doctor.user.firstName}"/> <c:out value="${app.doctor.user.lastName}"/>
                          </div>
                        </td>
                        <td>
                          <span class="status-pill doctor"><c:out value="${app.doctor.specialty.department.name}"/> &bull; <c:out value="${app.doctor.specialty.name}"/></span>
                        </td>
                        <td><span class="subtle-pill"><c:out value="${app.type}"/></span></td>
                        <td>
                          <span class="status-pill <c:if test="${app.status.name() == 'CANCELED'}">inactive</c:if> <c:if test="${app.status.name() == 'DONE'}">pending</c:if>">
                            <c:out value="${app.status}"/>
                          </span>
                        </td>
                        <td style="text-align:right;">
                          <form method="post" action="${pageContext.request.contextPath}/admin/appointments" style="display:inline-flex; gap:6px; align-items:center;">
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
