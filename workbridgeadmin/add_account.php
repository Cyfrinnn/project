<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username']; // Get the username from the POST request
    $defaultPassword = "admin"; // Set the default password (plain text)

    if (!empty($username)) {
        // Insert the new account into the database
        $sql = "INSERT INTO admins (username, password, last_active) VALUES (?, ?, NOW())";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $username, $defaultPassword);

        if ($stmt->execute()) {
            echo "Account added successfully with the default password!";
        } else {
            echo "Error adding account: " . $stmt->error;
        }
    } else {
        echo "Username cannot be empty.";
    }
}
?>
