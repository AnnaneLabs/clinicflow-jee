<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Manage clinic departments and medical specialties — ClinicFlow Admin.">
  <title>Departments &amp; Specialties · ClinicFlow Admin</title>
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
      <a class="nav-item" id="nav-doctors" href="${pageContext.request.contextPath}/admin/doctors">
        <span class="nav-icon">⚕</span> Doctors
      </a>
      <a class="nav-item active" id="nav-departments" href="${pageContext.request.contextPath}/admin/departments">
        <span class="nav-icon">▤</span> Departments
      </a>
      <a class="nav-item" id="nav-users" href="${pageContext.request.contextPath}/admin/users">
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
          Admin <span>/</span> <strong>Departments &amp; Specialties</strong>
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
          <div class="eyebrow">CLINIC STRUCTURE</div>
          <h1>Departments &amp; Medical Specialties</h1>
          <p class="muted">Manage the medical structure and specialties across your clinic.</p>
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

      <!-- Two-column grid -->
      <div class="dashboard-grid">

        <!-- Departments column -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Departments</h2>
              <p>Medical divisions in the clinic</p>
            </div>
            <span class="subtle-pill"><c:out value="${departments.size()}"/> Depts</span>
          </div>

          <!-- Add department form -->
          <form id="add-dept-form" method="post"
                action="${pageContext.request.contextPath}/admin/departments"
                class="form-stack" style="margin-bottom:20px;">
            <input type="hidden" name="action" value="createDepartment">
            <div class="form-group">
              <label for="deptName">Add new department</label>
              <div style="display:flex; gap:10px;">
                <input id="deptName" name="name"
                       placeholder="e.g. Ophthalmology"
                       required style="flex:1;">
                <button id="add-dept-btn" type="submit" class="btn btn-primary">Add +</button>
              </div>
            </div>
          </form>

          <!-- Departments table -->
          <c:choose>
            <c:when test="${empty departments}">
              <div class="empty-state">
                <div class="empty-icon">▤</div>
                <strong>No departments yet</strong>
                <p>Add your first department using the form above.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Department name</th>
                      <th>Specialties</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="dept" items="${departments}">
                      <tr>
                        <td><span class="ref-id">#<c:out value="${dept.id}"/></span></td>
                        <td><strong><c:out value="${dept.name}"/></strong></td>
                        <td style="color:var(--muted-text);">
                          <c:out value="${dept.specialties != null ? dept.specialties.size() : 0}"/>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
        </article>

        <!-- Specialties column -->
        <article class="panel">
          <div class="panel-heading">
            <div>
              <h2>Specialties</h2>
              <p>Specific medical disciplines</p>
            </div>
            <span class="subtle-pill"><c:out value="${specialties.size()}"/> Specs</span>
          </div>

          <!-- Add specialty form -->
          <form id="add-spec-form" method="post"
                action="${pageContext.request.contextPath}/admin/departments"
                class="form-stack" style="margin-bottom:20px;">
            <input type="hidden" name="action" value="createSpecialty">

            <div class="form-group">
              <label for="specName">Specialty name</label>
              <input id="specName" name="name"
                     placeholder="e.g. Pediatric Cardiology"
                     required maxlength="120">
            </div>

            <div class="form-group">
              <label for="departmentId">Assigned department</label>
              <select id="departmentId" name="departmentId" required>
                <option value="">— Select department —</option>
                <c:forEach var="dept" items="${departments}">
                  <option value="${dept.id}"><c:out value="${dept.name}"/></option>
                </c:forEach>
              </select>
            </div>

            <button id="add-spec-btn" type="submit" class="btn btn-primary btn-full">
              Add Specialty +
            </button>
          </form>

          <!-- Specialties table -->
          <c:choose>
            <c:when test="${empty specialties}">
              <div class="empty-state">
                <div class="empty-icon">⚕</div>
                <strong>No specialties yet</strong>
                <p>Create departments first, then add specialties.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div class="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>Specialty</th>
                      <th>Department</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="spec" items="${specialties}">
                      <tr>
                        <td><strong><c:out value="${spec.name}"/></strong></td>
                        <td>
                          <span class="subtle-pill" style="font-size:11px;">
                            <c:out value="${spec.department.name}"/>
                          </span>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
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
