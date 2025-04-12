<?php
include 'config.php';

// Fetch all applicants initially
$sql = "SELECT id, full_name, email, address FROM applicants";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Applicants</title>
    <link rel="stylesheet" href="applicants.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <img src="sp_logo.png" alt="WorkBridge Logo">
        </div>
        <ul>
            <li><a href="dashboard.php">Dashboard</a></li>
            <li><a href="employers.php">Employers</a></li>
            <li class="active"><a href="applicants.php">Applicants</a></li>
            <li><a href="logs.php">User Logs</a></li>
            <li><a href="users.php">User Accounts</a></li>
        </ul>
    </div>

    <div class="content">
        <main>
            <!-- Search Bar -->
            <div class="search-bar">
                <span class="icon">&#128269;</span>
                <input type="text" id="searchBar" placeholder="Search...">
            </div>

            <!-- Applicants Table -->
            <div class="table-container">
                <table class="applicants-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Applicant Name</th>
                            <th>Email Address</th>
                            <th>Address</th>
                            <th>History</th>
                        </tr>
                    </thead>
                    <tbody id="applicantsTable">
                        <?php
                        if ($result->num_rows > 0) {
                            while ($row = $result->fetch_assoc()) {
                                echo "<tr>
                                    <td>{$row['id']}</td>
                                    <td>{$row['full_name']}</td>
                                    <td>{$row['email']}</td>
                                    <td>{$row['address']}</td>
                                    <td><a href='applicant_history.php?id={$row['id']}' class='view-button'>View</a></td>
                                </tr>";
                            }
                        } else {
                            echo "<tr><td colspan='5'>No results found</td></tr>";
                        }
                        ?>
                    </tbody>
                </table>
            </div>
        </main>
    </div>

    <script>
        // Real-time search functionality
        document.getElementById('searchBar').addEventListener('input', function() {
            const filter = this.value.toLowerCase();
            const rows = document.querySelectorAll('#applicantsTable tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    </script>
</body>
</html>
