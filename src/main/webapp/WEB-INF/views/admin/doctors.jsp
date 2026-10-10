<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Manage doctors, assign specialties and provision accounts — ClinicFlow Admin.">
  <title>Doctor Management · ClinicFlow Admin</title>
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
      <a class="nav-item" id="nav-overview" href="${pageContext.request.contextPath}/admin/dashboard">
        <span class="nav-icon">▦</span> Overview
      </a>
      <a class="nav-item active" id="nav-doctors" href="${pageContext.request.contextPath}/admin/doctors">
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
          Admin <span>/</span> <strong>Doctor Management</strong>
        </nav>
      </div>
      <div class="topbar-right">
        <div class="top-avatar" title="${sessionScope.user.fullName}">
          <c:out value="${sessionScope.user.firstName.substring(0,1)}"/>
        </div>
      </div>
    </header>

    <div class="content">

      <!-- Page header -->
      <div class="welcome-row">
        <div>
          <div class="eyebrow">DOCTOR DIRECTORY</div>
          <h1>Manage Medical Staff</h1>
          <p class="muted">Provision doctor accounts, assign specialties, and track matricules.</p>
        </div>
      </div>

      <!-- Flash messages -->
      <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="notice success"><c:out value="${sessionScope.flashSuccess}"/>
          <% session.removeAttribute("flashSuccess"); %>
        </div>
      </c:if>
      <c:if test="${not empty sessionScope.flashError}">
        <div class="notice error"><c:out value="${sessionScope.flashError}"/>
          <% session.removeAttribute("flashError"); %>
        </div>
      </c:if>

      <!-- Main two-column grid -->
      <div class="dashboard-grid">

        <!-- Doctors list -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Medical Doctors</h2>
              <p>All registered clinic doctors</p>
            </div>
            <span class="subtle-pill"><c:out value="${doctors.size()}"/> Doctors</span>
          </div>

          <c:choose>
            <c:when test="${empty doctors}">
              <div class="empty-state">
                <div class="empty-icon">⚕</div>
                <strong>No doctors registered yet</strong>
                <p>Use the form on the right to add your clinic's first doctor.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>Matricule</th>
                      <th>Doctor</th>
                      <th>Specialty</th>
                      <th>Department</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="doctor" items="${doctors}">
                      <tr>
                        <td><span class="ref-id"><c:out value="${doctor.matricule}"/></span></td>
                        <td>
                          <div style="display:flex; align-items:center; gap:10px;">
                            <div class="avatar" style="width:30px;height:30px;font-size:11px;flex-shrink:0;">
                              <c:out value="${doctor.firstName.substring(0,1)}"/>
                            </div>
                            <div>
                              <div style="font-weight:600;font-size:13px;">
                                <c:out value="${doctor.title}"/> <c:out value="${doctor.fullName}"/>
                              </div>
                              <div style="font-size:11px;color:var(--muted-text);">
                                <c:out value="${doctor.email}"/>
                              </div>
                            </div>
                          </div>
                        </td>
                        <td><c:out value="${doctor.specialtyName}"/></td>
                        <td><c:out value="${doctor.departmentName}"/></td>
                        <td>
                          <c:choose>
                            <c:when test="${doctor.active}">
                              <span class="status-pill">Active</span>
                            </c:when>
                            <c:otherwise>
                              <span class="status-pill inactive">Disabled</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
        </article>

        <!-- Provision form -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Provision Doctor Account</h2>
              <p>Create an account &amp; assign specialty</p>
            </div>
          </div>

          <form id="provision-doctor-form" method="post"
                action="${pageContext.request.contextPath}/admin/doctors"
                class="form-stack">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
              <label for="matricule">Matricule (Unique ID)</label>
              <input id="matricule" name="matricule"
                     placeholder="DOC-101"
                     required maxlength="30">
            </div>

            <div class="form-row">
              <div class="form-group">
                <label for="title">Title</label>
                <input id="title" name="title" placeholder="Dr." value="Dr." required maxlength="20">
              </div>
              <div class="form-group">
                <label for="firstName">First name</label>
                <input id="firstName" name="firstName" placeholder="Sarah" required maxlength="80">
              </div>
            </div>

            <div class="form-group">
              <label for="lastName">Last name</label>
              <input id="lastName" name="lastName" placeholder="Mitchell" required maxlength="80">
            </div>

            <div class="form-group">
              <label for="email">Email address</label>
              <input id="email" name="email" type="email"
                     placeholder="dr.smith@clinicflow.com" required maxlength="150">
            </div>

            <div class="form-group">
              <label for="phone">Phone number <span style="font-weight:400;color:var(--gray)">(optional)</span></label>
              <input id="phone" name="phone" placeholder="0612345678" maxlength="30">
            </div>

            <div class="form-group">
              <label for="specialtyId">Specialty &amp; Department</label>
              <select id="specialtyId" name="specialtyId" required>
                <option value="">— Select specialty —</option>
                <c:forEach var="spec" items="${specialties}">
                  <option value="${spec.id}">
                    <c:out value="${spec.name}"/> (<c:out value="${spec.department.name}"/>)
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="form-divider"></div>

            <div class="form-group">
              <label for="password">Initial password</label>
              <input id="password" name="password" type="password"
                     placeholder="Minimum 8 characters"
                     required minlength="8">
              <span class="form-hint">Doctor will be prompted to change this on first login.</span>
            </div>

            <button id="provision-doctor-btn" type="submit" class="btn btn-primary btn-full">
              Create Doctor Account &nbsp;+
            </button>
          </form>
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
