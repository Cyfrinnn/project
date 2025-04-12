<?php
// Include the database connection
include 'config.php';

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Logs</title>
    <link rel="stylesheet" href="logs.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <img src="sp_logo.png" alt="WorkBridge Logo">
        </div>
        <ul>
            <li><a href="dashboard.php">Dashboard</a></li>
            <li><a href="employers.php">Employers</a></li>
            <li><a href="applicants.php">Applicants</a></li>
            <li class="active"><a href="logs.php">User Logs</a></li>
            <li><a href="users.php">User Accounts</a></li>
        </ul>
    </div>
    <div class="content">
        <div class="search-filter">
            <div class="search-bar">
                <span class="icon">&#128269;</span>
                <input type="text" id="searchBar" placeholder="Search...">
            </div>
        </div>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>User</th>
                        <th>Activity</th>
                        <th>Company Name</th>
                        <th>Date and Time</th>
                    </tr>
                </thead>
                <tbody id="logsTable">
                    <?php
                    // Fetch logs dynamically from the database
                    $query = "SELECT 'Admin' AS user, status AS activity, company_name, updated_at AS date_time
                              FROM employers
                              ORDER BY updated_at DESC"; // Order by latest date and time
                    $result = $conn->query($query);

                    if ($result->num_rows > 0) {
                        while ($row = $result->fetch_assoc()) {
                            echo "<tr>
                                    <td>{$row['user']}</td>
                                    <td>{$row['activity']}</td>
                                    <td>{$row['company_name']}</td>
                                    <td>{$row['date_time']}</td>
                                  </tr>";
                        }
                    } else {
                        echo "<tr><td colspan='4'>No logs found</td></tr>";
                    }
                    ?>
                </tbody>
            </table>
        </div>
    </div>
    <script>
        document.getElementById('searchBar').addEventListener('input', function() {
            const filter = this.value.toLowerCase();
            document.querySelectorAll('#logsTable tr').forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    </script>
</body>
</html>
