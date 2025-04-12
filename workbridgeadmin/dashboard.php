<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

include('config.php'); // Include your database connection

$sql = "
    SELECT
        (SELECT COUNT(*) FROM applicants) AS total_applicants,
        (SELECT COUNT(*) FROM employers) AS total_employers,
        (SELECT COUNT(*) FROM employers WHERE status = 'active') AS active_job_listings,
        (SELECT COUNT(*) FROM applicants) AS applications_submitted,
        (SELECT COUNT(*) FROM applicants) + (SELECT COUNT(*) FROM employers) AS total_users
";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $total_applicants = $row['total_applicants'];
    $total_employers = $row['total_employers'];
    $active_job_listings = $row['active_job_listings'];
    $applications_submitted = $row['applications_submitted'];
    $total_users = $row['total_users'];
} else {
    // Default values if no data is found
    $total_applicants = 0;
    $total_employers = 0;
    $active_job_listings = 0;
    $applications_submitted = 0;
    $total_users = 0;
}
// Fetch data for user composition
$query = "
    SELECT 
        (SELECT COUNT(*) FROM admins) AS total_admins,
        (SELECT COUNT(*) FROM applicants) AS total_applicants,
        (SELECT COUNT(*) FROM employers) AS total_employers
    ";
$result = $conn->query($query);
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $total_admins = $row['total_admins'];
    $total_applicants = $row['total_applicants'];
    $total_employers = $row['total_employers'];
}


?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link rel="stylesheet" href="dashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <img src="sp_logo.png" alt="WorkBridge Logo">
        </div>
        <ul>
            <li class="active"><a href="dashboard.php">Dashboard</a></li>
            <li><a href="employers.php">Employers</a></li>
            <li><a href="applicants.php">Applicants</a></li>
            <li><a href="logs.php">User Logs</a></li>
            <li><a href="users.php">User Accounts</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header>
            <div class="header-content">
            </div>
        </header>
        <div class="dashboard">
            <h1>Overview Metrics</h1>
            <table>
                <thead>
                    <tr>
                        <th>Metric</th>
                        <th>Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Total Users</td>
                        <td><?php echo $total_users; ?></td>
                    </tr>
                    <tr>
                        <td>Active Job Listings</td>
                        <td><?php echo $total_employers; ?></td>
                    </tr>
                    <tr>
                        <td>Applications Submitted</td>
                        <td><?php echo $applications_submitted; ?></td>
                    </tr>
                </tbody>
            </table>

        </div>
            <div class="pie-container">
        <canvas id="userCompositionChart" width="450" height="400"></canvas>
            </div>
                <script>
                    const ctx = document.getElementById('userCompositionChart').getContext('2d');
                    const userCompositionChart = new Chart(ctx, {
                        type: 'pie',
                        data: {
                            labels: ['Applicants', 'Employers'],
                            datasets: [{
                                data: [<?php echo $total_applicants; ?>, <?php echo $total_employers; ?>],
                                backgroundColor: ['#2ecc71', '#e74c3c']
                            }]
                        },
                        options: {
                            responsive:false,
                            maintainAspectRatio: false
                        }
                    });
                </script>
    </div>
</body>
</html>
