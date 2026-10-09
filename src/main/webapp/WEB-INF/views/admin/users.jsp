<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Manage system user accounts and access privileges — ClinicFlow Admin.">
  <title>User Accounts · ClinicFlow Admin</title>
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
      <a class="nav-item" id="nav-departments" href="${pageContext.request.contextPath}/admin/departments">
        <span class="nav-icon">▤</span> Departments
      </a>
      <a class="nav-item active" id="nav-users" href="${pageContext.request.contextPath}/admin/users">
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
          Admin <span>/</span> <strong>User Accounts</strong>
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
          <div class="eyebrow">USER ACCESS CONTROL</div>
          <h1>System User Accounts</h1>
          <p class="muted">Manage registered accounts and enable or disable access privileges.</p>
        </div>
        <div class="date-chip">
          <span>♙</span> <c:out value="${users.size()}"/> total users
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

      <!-- Full-width users table -->
      <article class="panel">
        <div class="panel-heading">
          <div>
            <h2>All Accounts</h2>
            <p>System users across all roles</p>
          </div>
          <span class="subtle-pill"><c:out value="${users.size()}"/> Users</span>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>User</th>
                <th>Email address</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="u" items="${users}">
                <tr>
                  <td>
                    <div style="display:flex; align-items:center; gap:10px;">
                      <div class="avatar" style="width:30px;height:30px;font-size:11px;flex-shrink:0;">
                        <c:out value="${u.firstName.substring(0,1)}"/>
                      </div>
                      <div>
                        <div style="font-weight:600;font-size:13px;">
                          <c:out value="${u.firstName}"/> <c:out value="${u.lastName}"/>
                        </div>
                        <div style="font-size:11px;color:var(--muted-text);">#<c:out value="${u.id}"/></div>
                      </div>
                    </div>
                  </td>
                  <td style="color:var(--muted-text);font-size:12.5px;">
                    <c:out value="${u.email}"/>
                  </td>
                  <td style="color:var(--muted-text);font-size:12.5px;">
                    <c:out value="${u.phone != null ? u.phone : '—'}"/>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${u.role == 'ADMIN'}">
                        <span class="role-badge admin"><c:out value="${u.role}"/></span>
                      </c:when>
                      <c:when test="${u.role == 'DOCTOR'}">
                        <span class="role-badge doctor"><c:out value="${u.role}"/></span>
                      </c:when>
                      <c:otherwise>
                        <span class="role-badge patient"><c:out value="${u.role}"/></span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${u.active}">
                        <span class="status-pill">Active</span>
                      </c:when>
                      <c:otherwise>
                        <span class="status-pill inactive">Disabled</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <c:if test="${u.id != sessionScope.user.id}">
                      <form method="post"
                            action="${pageContext.request.contextPath}/admin/users"
                            style="display:inline;">
                        <input type="hidden" name="action" value="toggleActive">
                        <input type="hidden" name="userId" value="${u.id}">
                        <c:choose>
                          <c:when test="${u.active}">
                            <button type="submit" class="btn btn-xs btn-danger">Disable</button>
                          </c:when>
                          <c:otherwise>
                            <button type="submit" class="btn btn-xs btn-ghost"
                                    style="color:var(--primary);border-color:var(--primary-light);background:var(--primary-light);">
                              Enable
                            </button>
                          </c:otherwise>
                        </c:choose>
                      </form>
                    </c:if>
                    <c:if test="${u.id == sessionScope.user.id}">
                      <span style="font-size:11px;color:var(--gray);">— You</span>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </article>

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
