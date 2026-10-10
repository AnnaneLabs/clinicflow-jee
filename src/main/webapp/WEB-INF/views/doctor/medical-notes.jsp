<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Clinical Notes &amp; Prescriptions · ClinicFlow Doctor Console</title>
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
        <a class="nav-item" href="${pageContext.request.contextPath}/doctor/appointments">
          <span class="nav-icon">📅</span> Patient Appointments
        </a>
        <a class="nav-item active" href="${pageContext.request.contextPath}/doctor/medical-notes">
          <span class="nav-icon">📝</span> Clinical Notes &amp; Prescriptions
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
          <span>Doctor Portal</span> <span>/</span> <strong>Clinical Notes &amp; Prescriptions</strong>
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
            <span class="eyebrow">CLINICAL RECORDS</span>
            <h1>Consultation Notes &amp; Prescriptions</h1>
            <p class="muted">Record medical diagnosis, clinical notes, and prescriptions for patient consultations.</p>
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

          <!-- Left Column: Medical Notes History List -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Clinical Notes Log</h2>
                <p>History of recorded diagnoses and prescriptions.</p>
              </div>
              <span class="subtle-pill">${notes.size()} Records</span>
            </div>

            <c:choose>
              <c:when test="${empty notes}">
                <div class="empty-state">
                  <div class="empty-icon">📝</div>
                  <strong>No clinical notes recorded yet</strong>
                  <p>Select a patient consultation on the right to write diagnosis notes and prescriptions.</p>
                </div>
              </c:when>
              <c:otherwise>
                <div class="table-wrap">
                  <table class="no-wrap">
                    <thead>
                      <tr>
                        <th>Date &amp; Time</th>
                        <th>Patient Name</th>
                        <th>Diagnosis</th>
                        <th>Status</th>
                        <th>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="n" items="${notes}">
                        <tr>
                          <td>
                            <span class="table-date"><c:out value="${n.appointment.startTime.toLocalDate()}"/></span>
                            <span class="table-time"><c:out value="${n.appointment.startTime.toLocalTime()}"/></span>
                          </td>
                          <td>
                            <div style="font-weight:700; color:var(--dark);">
                              <c:out value="${n.appointment.patient.user.firstName}"/> <c:out value="${n.appointment.patient.user.lastName}"/>
                            </div>
                            <div style="font-size:11px; color:var(--muted-text);">
                              CIN: <c:out value="${n.appointment.patient.cin}"/>
                            </div>
                          </td>
                          <td><strong><c:out value="${n.diagnosis}"/></strong></td>
                          <td>
                            <span class="status-pill <c:if test="${n.status.name() == 'DRAFT'}">pending</c:if>">
                              <c:out value="${n.status}"/>
                            </span>
                          </td>
                          <td>
                            <a href="${pageContext.request.contextPath}/doctor/medical-notes?appointmentId=${n.appointment.id}" class="btn btn-outline" style="padding:4px 10px; font-size:12px;">
                              Edit Note
                            </a>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </c:otherwise>
            </c:choose>
          </section>

          <!-- Right Column: Medical Note Editor Form -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Record Consultation Note</h2>
                <p>Enter patient diagnosis and medical prescription.</p>
              </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/doctor/medical-notes" class="form-stack">

              <!-- Select Appointment -->
              <div class="form-group">
                <label for="appointmentId">Select Patient Consultation</label>
                <select id="appointmentId" name="appointmentId" required>
                  <option value="">-- Choose Consultation --</option>
                  <c:forEach var="app" items="${appointments}">
                    <option value="${app.id}" <c:if test="${selectedAppointmentId == app.id}">selected</c:if>>
                      <c:out value="${app.startTime.toLocalDate()}"/> <c:out value="${app.startTime.toLocalTime()}"/> &ndash; Patient: <c:out value="${app.patient.user.firstName}"/> <c:out value="${app.patient.user.lastName}"/> (<c:out value="${app.status}"/>)
                    </option>
                  </c:forEach>
                </select>
              </div>

              <!-- Diagnosis -->
              <div class="form-group">
                <label for="diagnosis">Medical Diagnosis</label>
                <input type="text" id="diagnosis" name="diagnosis"
                       value="<c:out value='${selectedNote.diagnosis}'/>"
                       placeholder="e.g. Acute Hypertension, Allergic Rhinitis..." required>
              </div>

              <!-- Content / Prescription / Clinical Notes -->
              <div class="form-group">
                <label for="content">Clinical Observations &amp; Prescriptions</label>
                <textarea id="content" name="content" rows="6"
                          placeholder="e.g. Rx: Amoxicillin 500mg - 1 tab every 8h for 7 days. Patient advised to rest and monitor blood pressure."><c:out value="${selectedNote.content}"/></textarea>
              </div>

              <!-- Note Status -->
              <div class="form-group">
                <label for="status">Document Status</label>
                <select id="status" name="status" required>
                  <c:forEach var="ns" items="${noteStatuses}">
                    <option value="${ns}" <c:if test="${selectedNote.status == ns}">selected</c:if>>${ns}</option>
                  </c:forEach>
                </select>
              </div>

              <button type="submit" class="btn btn-primary btn-full" style="margin-top:10px;">
                💾 Save Medical Record &amp; Complete Consultation
              </button>
            </form>
          </section>

        </div>
      </main>
    </div>
  </div>
</body>
</html>
