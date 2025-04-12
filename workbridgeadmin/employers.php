<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

include('config.php'); // Include your database connection

// Handle approval and denial actions via AJAX
if (isset($_POST['ajax']) && $_POST['ajax'] === 'true') {
    $employer_id = $_POST['employer_id'];
    $action = $_POST['action'];
    $new_status = $action === 'approve' ? 'Approved' : 'Denied';
    
    // Save status and date/time in the database
    $conn->query("UPDATE employers SET status = '$new_status', updated_at = NOW() WHERE id = $employer_id");

    // Return updated counts for status boxes
    $pending_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status IS NULL OR status = 'Pending'")->fetch_assoc()['count'];
    $approved_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status = 'Approved'")->fetch_assoc()['count'];
    $denied_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status = 'Denied'")->fetch_assoc()['count'];

    echo json_encode([
        'success' => true,
        'pending_count' => $pending_count,
        'approved_count' => $approved_count,
        'denied_count' => $denied_count,
    ]);
    exit();
}

// Fetch data for status boxes
$pending_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status IS NULL OR status = 'Pending'")->fetch_assoc()['count'];
$approved_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status = 'Approved'")->fetch_assoc()['count'];
$denied_count = $conn->query("SELECT COUNT(*) AS count FROM employers WHERE status = 'Denied'")->fetch_assoc()['count'];

// Fetch employer data
$employers = $conn->query("SELECT * FROM employers");
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employers</title>
    <link rel="stylesheet" href="employers.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <img src="sp_logo.png" alt="WorkBridge Logo">
        </div>
        <ul>
            <li><a href="dashboard.php">Dashboard</a></li>
            <li class="active"><a href="employers.php">Employers</a></li>
            <li><a href="applicants.php">Applicants</a></li>
            <li><a href="logs.php">User Logs</a></li>
            <li><a href="users.php">User Accounts</a></li>
        </ul>
    </div>
    <div class="main-content">
        <div class="status-boxes">
            <div class="box pending">
                <h2>Pending</h2>
                <p><?php echo $pending_count; ?></p>
            </div>
            <div class="box approved">
                <h2>Approved</h2>
                <p><?php echo $approved_count; ?></p>
            </div>
            <div class="box denied">
                <h2>Denied</h2>
                <p><?php echo $denied_count; ?></p>
            </div>
        </div>
        <div class="search-filter">
            <div class="search-bar">
                <span class="icon">&#128269;</span>
                <input type="text" id="searchBar" placeholder="Search...">
            </div>
            <select id="statusFilter">
                <option value="all">All</option>
                <option value="pending">Pending</option>
                <option value="approved">Approved</option>
                <option value="denied">Denied</option>
            </select>
        </div>
        <div class="employers-table">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Company Name</th>
                        <th>Email Address</th>
                        <th>Proofs</th>
                        <th>Status</th>
                        <th>Approval</th>
                    </tr>
                </thead>
                <tbody id="employersTable">
                    <?php while($row = $employers->fetch_assoc()): ?>
                    <tr data-status="<?php echo strtolower($row['status'] ?? 'pending'); ?>">
                        <td><?php echo $row['id']; ?></td>
                        <td><?php echo $row['company_name']; ?></td>
                        <td><?php echo $row['email']; ?></td>
                        <td>
                            <?php if(isset($row['proofs'])): ?>
                                <img src="data:image/jpeg;base64,<?php echo base64_encode($row['proofs']); ?>" alt="Proof Image" width="100">
                            <?php else: ?>
                                N/A
                            <?php endif; ?>
                        </td>
                        <td><?php echo $row['status'] ?? 'Pending'; ?></td>
                        <td>
                            <button class="approve" data-id="<?php echo $row['id']; ?>" data-action="approve">✔️</button>
                            <button class="deny" data-id="<?php echo $row['id']; ?>" data-action="deny">❌</button>
                        </td>
                    </tr>
                    <?php endwhile; ?>
                </tbody>
            </table>
        </div>
    </div>
    <script>
        document.querySelectorAll('.approve, .deny').forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault(); // Prevent page reload
                
                const employerId = this.dataset.id;
                const action = this.dataset.action;

                // Send an AJAX request to update status
                fetch('employers.php', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        ajax: 'true',
                        employer_id: employerId,
                        action: action
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // Update the row's status dynamically
                        const row = this.closest('tr');
                        row.querySelector('td:nth-child(5)').textContent = action === 'approve' ? 'Approved' : 'Denied';

                        // Update the status box counts dynamically
                        document.querySelector('.box.pending p').textContent = data.pending_count;
                        document.querySelector('.box.approved p').textContent = data.approved_count;
                        document.querySelector('.box.denied p').textContent = data.denied_count;
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        });

        // Search bar functionality
        document.getElementById('searchBar').addEventListener('input', function() {
            const filter = this.value.toLowerCase();
            document.querySelectorAll('#employersTable tr').forEach(row => {
                const companyName = row.children[1].textContent.toLowerCase();
                row.style.display = companyName.includes(filter) ? '' : 'none';
            });
        });

        // Status filter functionality
        document.getElementById('statusFilter').addEventListener('change', function() {
            const filter = this.value;
            document.querySelectorAll('#employersTable tr').forEach(row => {
                row.style.display = (filter === 'all' || row.dataset.status === filter) ? '' : 'none';
            });
        });
    </script>
</body>
</html>
