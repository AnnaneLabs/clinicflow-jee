<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Medical History &amp; Prescriptions · ClinicFlow Patient Portal</title>
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

      <div class="workspace-label">PATIENT PORTAL</div>

      <nav class="side-nav">
        <a class="nav-item" href="${pageContext.request.contextPath}/dashboard">
          <span class="nav-icon">📊</span> Overview
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/patient/appointments">
          <span class="nav-icon">📅</span> Book Consultations
        </a>
        <a class="nav-item active" href="${pageContext.request.contextPath}/patient/medical-history">
          <span class="nav-icon">📋</span> Medical History
        </a>

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
          <div class="avatar"><c:out value="${user.firstName.substring(0,1)}"/></div>
          <div class="user-meta">
            <strong><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/></strong>
            <span>Patient</span>
          </div>
          <a class="icon-btn" href="${pageContext.request.contextPath}/logout" title="Sign out">🚪</a>
        </div>
      </div>
    </aside>

    <!-- ── Main Content Area ── -->
    <div class="main-area">
      <header class="topbar">
        <div class="breadcrumb">
          <span>Patient Portal</span> <span>/</span> <strong>Medical History</strong>
        </div>
        <div class="topbar-right">
          <span class="today-label"><c:out value="${patient.user.firstName}"/> <c:out value="${patient.user.lastName}"/> (CIN: <c:out value="${patient.cin}"/>)</span>
          <a href="${pageContext.request.contextPath}/profile" class="top-avatar" title="View profile">
            <c:out value="${user.firstName.substring(0,1)}"/>
          </a>
        </div>
      </header>

      <main class="content">
        <div class="welcome-row">
          <div>
            <span class="eyebrow">HEALTH RECORDS</span>
            <h1>Medical History &amp; Prescriptions</h1>
            <p class="muted">Your timeline of clinical consultation diagnoses, doctor recommendations, and prescriptions.</p>
          </div>
        </div>

        <section class="panel">
          <div class="panel-heading">
            <div>
              <h2>Consultation Medical Records</h2>
              <p>Verified clinical notes written by your attending doctors.</p>
            </div>
            <span class="subtle-pill">${medicalHistory.size()} Records</span>
          </div>

          <c:choose>
            <c:when test="${empty medicalHistory}">
              <div class="empty-state">
                <div class="empty-icon">📋</div>
                <strong>No medical records found</strong>
                <p>Clinical notes and prescriptions recorded by your attending doctors after consultations will be displayed here.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div style="display:flex; flex-direction:column; gap:20px;">
                <c:forEach var="note" items="${medicalHistory}">
                  <article class="panel" style="border-left: 4px solid var(--primary); background:#FAFFFE;">
                    <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:12px;">
                      <div>
                        <span class="eyebrow" style="color:var(--primary);"><c:out value="${note.appointment.startTime.toLocalDate()}"/> &bull; <c:out value="${note.appointment.startTime.toLocalTime()}"/></span>
                        <h3 style="font-size:18px; font-weight:800; color:var(--dark); margin:4px 0 2px;">
                          Diagnosis: <c:out value="${note.diagnosis}"/>
                        </h3>
                        <p class="muted" style="font-size:12.5px;">
                          Attending Physician: <strong><c:out value="${note.doctor.title}"/> <c:out value="${note.doctor.user.firstName}"/> <c:out value="${note.doctor.user.lastName}"/></strong> (<c:out value="${note.doctor.specialty.name}"/>)
                        </p>
                      </div>
                      <span class="status-pill"><c:out value="${note.status}"/></span>
                    </div>

                    <div style="background:var(--white); border:1px solid var(--border); border-radius:var(--radius-md); padding:16px; font-size:13.5px; line-height:1.6; color:var(--dark); white-space:pre-wrap;"><c:out value="${note.content}"/></div>
                  </article>
                </c:forEach>
              </div>
            </c:otherwise>
          </c:choose>
        </section>

      </main>
    </div>
  </div>
</body>
</html>
