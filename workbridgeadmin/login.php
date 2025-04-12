<?php
session_start(); // Start the session
include('config.php'); // Include your database connection

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST['username'];
    $password = $_POST['password']; // Password entered by the user

    // Query the database to fetch the user
    $sql = "SELECT * FROM admins WHERE username=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc(); // Fetch the user data

        // Check if the password matches
        if ($password === $user['password'] || password_verify($password, $user['password'])) {
            // Login successful
            $_SESSION['username'] = $username;
            header("Location: dashboard.php");
            exit;
        } else {
            echo "Invalid username or password";
        }
    } else {
        echo "Invalid username or password";
    }
}
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WorkBridge Login</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
    <div class="logo">
        <img src="login_logo.png" alt="WorkBridge Logo">
    </div>
    <div class="login-container">
        <form action="login.php" method="post">
    <h2>Admin</h2>
    <input type="text" name="username" placeholder="Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <button type="submit">Log in</button>
    <p><a href="forgot_password.php">Forgot Password?</a></p>
</form>

    </div>
</body>
</html>
