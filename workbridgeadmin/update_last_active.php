<?php
session_start();
include 'config.php';

if (isset($_SESSION['username'])) {
    $username = $_SESSION['username'];
    $sql = "UPDATE admins SET last_active = NOW() WHERE username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
}
?>
