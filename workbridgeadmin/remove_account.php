<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];

    if (!empty($username)) {
        $sql = "DELETE FROM admins WHERE username = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $username);

        if ($stmt->execute()) {
            echo "Account removed successfully!";
        } else {
            echo "Error removing account.";
        }
    } else {
        echo "Username cannot be empty.";
    }
}
?>
