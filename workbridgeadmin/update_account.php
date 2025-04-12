<?php
session_start();
include 'config.php';

// Redirect if the user is not logged in
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit;
}

$responseMessage = ""; // Initialize message for feedback
$redirectButton = ""; // Initialize button for redirection

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_SESSION['username'];
    $currentPassword = $_POST['current_password'];
    $newPassword = $_POST['new_password'];
    $confirmPassword = $_POST['confirm_password'];

    // Ensure New Password matches Confirm Password
    if ($newPassword !== $confirmPassword) {
        $responseMessage = "New Password and Confirm Password do not match.";
        $redirectButton = '<button onclick="redirectToManage()">Back to Manage Account</button>';
    } else {
        // Fetch the current hashed password from the database
        $sql = "SELECT password FROM admins WHERE username = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();
        $user = $result->fetch_assoc();

        // Verify the current password using password_verify
        if (password_verify($currentPassword, $user['password'])) {
            // Encrypt the new password
            $encryptedPassword = password_hash($newPassword, PASSWORD_DEFAULT);

            // Update the password in the database
            $sql = "UPDATE admins SET password = ? WHERE username = ?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("ss", $encryptedPassword, $username);

            if ($stmt->execute()) {
                $responseMessage = "Password updated successfully!";
                $redirectButton = '<button onclick="redirectToDashboard()">Go to Dashboard</button>';
            } else {
                $responseMessage = "Error updating password.";
                $redirectButton = '<button onclick="redirectToManage()">Back to Manage Account</button>';
            }
        } else {
            $responseMessage = "Incorrect current password.";
            $redirectButton = '<button onclick="redirectToManage()">Back to Manage Account</button>';
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Password</title>
    <link rel="stylesheet" href="manage_account.css">
    <style>
        .response-container {
            text-align: center;
            margin-top: 50px;
            font-family: Arial, sans-serif;
        }

        .response-message {
            font-size: 18px;
            margin-bottom: 20px;
        }

        .response-button button {
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            background-color: #1abc9c;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .response-button button:hover {
            background-color: #16a085;
        }
    </style>
</head>
<body>
    <div class="response-container">
        <div class="response-message">
            <?php echo $responseMessage; ?>
        </div>
        <div class="response-button">
            <?php echo $redirectButton; ?>
        </div>
    </div>

    <script>
        function redirectToDashboard() {
            window.location.href = "dashboard.php";
        }

        function redirectToManage() {
            window.location.href = "manage_account.php";
        }
    </script>
</body>
</html>
