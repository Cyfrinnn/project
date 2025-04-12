<?php
session_start();

// Redirect to login page if the user is not logged in
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit;
}

include 'config.php'; // Include the database connection
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Accounts</title>
    <link rel="stylesheet" href="users.css">
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
            <li><a href="logs.php">User Logs</a></li>
            <li class="active"><a href="users.php">User Accounts</a></li>
        </ul>
    </div>
    <div class="main-content">
        <header>
    <div class="header-content">
        <div class="header-buttons">
            <!-- Profile Button -->
            <button class="profile-button" onclick="toggleDropdown()">&#128100;</button>
            <!-- Dropdown Menu -->
            <div id="profileDropdown" class="dropdown-menu">
                <button onclick="manageAccount()">Manage Account</button>
                <button onclick="logOut()">Log Out</button>
            </div>
        </div>
    </div>
        </header>
        <div class="user-table">
            <table>
                <thead>
                    <tr>
                        <th>Admin Name</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Data will be dynamically populated here by AJAX -->
                </tbody>
            </table>
        </div>
        <div class="action-buttons">
            <button class="add-button" onclick="addAccount()">Add Account</button>
            <button class="remove-button" onclick="removeAccount()">Remove Account</button>
        </div>
    </div>
    <script>
        function fetchStatus() {
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "fetch_status.php", true);
            xhr.onload = function () {
                if (xhr.status === 200) {
                    const data = JSON.parse(xhr.responseText);
                    const tableBody = document.querySelector("tbody");
                    tableBody.innerHTML = "";

                    data.forEach(admin => {
                        const statusClass = admin.status === "Active now" ? "online" : "offline";
                        const row = `
                            <tr>
                                <td>${admin.username}</td>
                                <td>
                                    <span class="status-circle ${statusClass}"></span>
                                    ${admin.status}
                                </td>
                            </tr>
                        `;
                        tableBody.innerHTML += row;
                    });
                }
            };
            xhr.send();
        }

        // Fetch statuses immediately and every 5 seconds
        fetchStatus();
        setInterval(fetchStatus, 5000);

        function updateLastActive() {
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "update_last_active.php", true);
            xhr.send();
        }

        // Update `last_active` immediately and every 5 seconds
        updateLastActive();
        setInterval(updateLastActive, 5000);

        // Add Account Functionality
        function addAccount() {
            const username = prompt("Enter the username for the new account:");
            if (username) {
                const xhr = new XMLHttpRequest();
                xhr.open("POST", "add_account.php", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.onload = function () {
                    if (xhr.status === 200) {
                        alert(xhr.responseText);
                        fetchStatus(); // Refresh the table
                    }
                };
                xhr.send(`username=${username}`);
            }
        }

        // Remove Account Functionality
        function removeAccount() {
            const username = prompt("Enter the username of the account to remove:");
            if (username) {
                const xhr = new XMLHttpRequest();
                xhr.open("POST", "remove_account.php", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.onload = function () {
                    if (xhr.status === 200) {
                        alert(xhr.responseText);
                        fetchStatus(); // Refresh the table
                    }
                };
                xhr.send(`username=${username}`);
            }
        }
    </script>
    <script>
    function toggleDropdown() {
        const dropdown = document.getElementById('profileDropdown');
        // Toggle visibility
        dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
    }

    // Functionality for Manage Account button
    function manageAccount() {
        alert("Redirecting to Manage Account page...");
        // Replace with the actual redirection
        window.location.href = "manage_account.php";
    }

    function logOut() {
    const confirmation = confirm("Are you sure you want to log out?");
    if (confirmation) {
        // Redirect to the logout page if the user confirms
        window.location.href = "login.php";
    }
}


    // Close dropdown if clicked outside
    window.onclick = function(event) {
        const dropdown = document.getElementById('profileDropdown');
        if (!event.target.matches('.profile-button')) {
            dropdown.style.display = 'none';
        }
    };
</script>
</body>
<?php
$conn->close(); // Close the database connection
?>
</html>
