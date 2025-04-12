<?php
include 'config.php';

$sql = "SELECT username, 
               CASE 
                   WHEN TIMESTAMPDIFF(MINUTE, last_active, NOW()) <= 5 THEN 'Active now'
                   ELSE 'Offline'
               END AS status
        FROM admins";

$result = $conn->query($sql);

$admins = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $admins[] = $row;
    }
}

$conn->close();

echo json_encode($admins);
?>
