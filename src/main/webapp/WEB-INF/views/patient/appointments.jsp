<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Book Consultations · ClinicFlow Patient Portal</title>
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
        <a class="nav-item active" href="${pageContext.request.contextPath}/patient/appointments">
          <span class="nav-icon">📅</span> Book Consultations
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
        <div class="help-card">
          <div class="help-icon">💚</div>
          <strong>Care, Connected</strong>
          <p>Book consultations with specialist doctors in just a few clicks.</p>
        </div>

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
          <span>Patient Portal</span> <span>/</span> <strong>Book Consultations</strong>
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
            <span class="eyebrow">APPOINTMENT SCHEDULING</span>
            <h1>Book &amp; Manage Appointments</h1>
            <p class="muted">Schedule a consultation with our clinic specialists or view your booking history.</p>
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

          <!-- Left Column: Patient's Appointments History -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>My Appointments</h2>
                <p>Your upcoming and past consultation bookings.</p>
              </div>
              <span class="subtle-pill">${appointments.size()} Total</span>
            </div>

            <c:choose>
              <c:when test="${empty appointments}">
                <div class="empty-state">
                  <div class="empty-icon">📅</div>
                  <strong>No appointments booked yet</strong>
                  <p>Use the form on the right to select a specialist doctor and book your consultation slot.</p>
                </div>
              </c:when>
              <c:otherwise>
                <div class="table-wrap">
                  <table class="no-wrap">
                    <thead>
                      <tr>
                        <th>Date &amp; Time</th>
                        <th>Doctor &amp; Specialty</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Reason</th>
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
                              <c:out value="${app.doctor.title}"/> <c:out value="${app.doctor.user.firstName}"/> <c:out value="${app.doctor.user.lastName}"/>
                            </div>
                            <div style="font-size:11px; color:var(--primary); font-weight:600;">
                              <c:out value="${app.doctor.specialty.name}"/>
                            </div>
                          </td>
                          <td><span class="subtle-pill"><c:out value="${app.type}"/></span></td>
                          <td>
                            <span class="status-pill <c:if test="${app.status.name() == 'CANCELED'}">inactive</c:if> <c:if test="${app.status.name() == 'DONE'}">pending</c:if>">
                              <c:out value="${app.status}"/>
                            </span>
                          </td>
                          <td>
                            <c:choose>
                              <c:when test="${not empty app.reason}"><c:out value="${app.reason}"/></c:when>
                              <c:otherwise><span class="muted">General consultation</span></c:otherwise>
                            </c:choose>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </c:otherwise>
            </c:choose>
          </section>

          <!-- Right Column: Interactive Booking Form -->
          <section class="panel">
            <div class="panel-heading">
              <div>
                <h2>Book a Consultation</h2>
                <p>Select a doctor, date, and available slot.</p>
              </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/patient/appointments" class="form-stack" id="bookingForm">
              
              <!-- Specialty Selector -->
              <div class="form-group">
                <label for="specialtySelect">1. Select Specialty</label>
                <select id="specialtySelect" onchange="filterDoctorsBySpecialty()">
                  <option value="">-- All Specialties --</option>
                  <c:forEach var="spec" items="${specialties}">
                    <option value="${spec.id}"><c:out value="${spec.name}"/></option>
                  </c:forEach>
                </select>
              </div>

              <!-- Doctor Selector -->
              <div class="form-group">
                <label for="doctorId">2. Select Doctor</label>
                <select id="doctorId" name="doctorId" required onchange="loadAvailableSlots()">
                  <option value="">-- Choose Doctor --</option>
                  <c:forEach var="doc" items="${doctors}">
                    <option value="${doc.id}" data-specialty="${doc.specialty.id}">
                      <c:out value="${doc.title}"/> <c:out value="${doc.user.firstName}"/> <c:out value="${doc.user.lastName}"/> (<c:out value="${doc.specialty.name}"/>)
                    </option>
                  </c:forEach>
                </select>
              </div>

              <!-- Date Picker -->
              <div class="form-group">
                <label for="date">3. Select Date</label>
                <input type="date" id="date" name="date" required onchange="loadAvailableSlots()">
              </div>

              <!-- Available Slot Selector -->
              <div class="form-group">
                <label for="time">4. Select Time Slot</label>
                <select id="time" name="time" required>
                  <option value="">-- Choose Doctor &amp; Date First --</option>
                </select>
                <span class="form-hint" id="slotHint">Open slots are calculated based on doctor working hours and leave calendar.</span>
              </div>

              <!-- Appointment Type -->
              <div class="form-group">
                <label for="type">5. Consultation Type</label>
                <select id="type" name="type" required>
                  <c:forEach var="t" items="${appointmentTypes}">
                    <option value="${t}">${t}</option>
                  </c:forEach>
                </select>
              </div>

              <!-- Reason -->
              <div class="form-group">
                <label for="reason">6. Reason for Visit / Symptoms</label>
                <textarea id="reason" name="reason" rows="2" placeholder="e.g. Regular checkup, headache, follow-up on test results..."></textarea>
              </div>

              <button type="submit" class="btn btn-primary btn-full" style="margin-top:10px;">
                + Confirm Appointment Booking
              </button>
            </form>
          </section>

        </div>
      </main>
    </div>
  </div>

  <script>
    document.addEventListener("DOMContentLoaded", function() {
      // Default date to today
      const today = new Date().toISOString().split('T')[0];
      const dateInput = document.getElementById("date");
      if (dateInput && !dateInput.value) {
        dateInput.value = today;
        dateInput.min = today;
      }
    });

    function filterDoctorsBySpecialty() {
      const specId = document.getElementById("specialtySelect").value;
      const docSelect = document.getElementById("doctorId");
      const options = docSelect.querySelectorAll("option");

      options.forEach(opt => {
        if (!opt.value) return; // Keep placeholder
        if (!specId || opt.getAttribute("data-specialty") === specId) {
          opt.style.display = "";
        } else {
          opt.style.display = "none";
        }
      });
      docSelect.value = "";
      loadAvailableSlots();
    }

    function loadAvailableSlots() {
      const doctorId = document.getElementById("doctorId").value;
      const date = document.getElementById("date").value;
      const timeSelect = document.getElementById("time");
      const slotHint = document.getElementById("slotHint");

      timeSelect.innerHTML = "<option value=''>-- Loading slots... --</option>";

      if (!doctorId || !date) {
        timeSelect.innerHTML = "<option value=''>-- Choose Doctor &amp; Date First --</option>";
        slotHint.innerText = "Select doctor and date to view available time slots.";
        return;
      }

      fetch('${pageContext.request.contextPath}/patient/appointments?action=getSlots&doctorId=' + doctorId + '&date=' + date)
        .then(response => response.json())
        .then(slots => {
          timeSelect.innerHTML = "";
          if (!slots || slots.length === 0) {
            timeSelect.innerHTML = "<option value=''>No open slots on this date</option>";
            slotHint.innerText = "The doctor is either absent or has no working hours configured for this day.";
          } else {
            timeSelect.innerHTML = "<option value=''>-- Select Open Slot --</option>";
            slots.forEach(slot => {
              const opt = document.createElement("option");
              opt.value = slot;
              opt.textContent = slot;
              timeSelect.appendChild(opt);
            });
            slotHint.innerText = slots.length + " open consultation slot(s) available on this date.";
          }
        })
        .catch(err => {
          timeSelect.innerHTML = "<option value=''>Error loading slots</option>";
          slotHint.innerText = "Could not fetch slots. Please try again.";
        });
    }
  </script>
</body>
</html>
