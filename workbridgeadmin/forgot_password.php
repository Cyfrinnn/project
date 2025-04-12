<?php
include('config.php');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];

    // Validate the username
    $sql = "SELECT * FROM admins WHERE username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows === 1) {
        // Generate a temporary password
        $tempPassword = substr(md5(uniqid(rand(), true)), 0, 8); // Random 8-character password
        $encryptedPassword = password_hash($tempPassword, PASSWORD_DEFAULT);

        // Update the temporary password in the database
        $sql = "UPDATE admins SET password = ? WHERE username = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $encryptedPassword, $username);

        if ($stmt->execute()) {
            echo "Your temporary password is: $tempPassword";
            echo "<p><a href='login.php'>Back to Login</a></p>";
        } else {
            echo "Error updating password.";
        }
    } else {
        echo "No account found with that username.";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="forgot_password.css">
</head>
<body>
    <div class="container">
        <h2>Forgot Password</h2>
        <form action="forgot_password.php" method="POST">
            <label for="username">Enter your Username</label>
            <input type="text" id="username" name="username" placeholder="Username" required>
            <button type="submit">Reset Password</button>
            <p><a href="manage_account.php">Back</a></p>
        </form>
    </div>
</body>
</html>
