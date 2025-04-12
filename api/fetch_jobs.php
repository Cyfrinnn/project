<?php
include 'db_connection.php';

// Fetch jobs from the database
$sql = "SELECT * FROM job_posts"; 
$result = mysqli_query($conn, $sql);

$jobs = array();

// Loop through the results and build the jobs array
while ($row = mysqli_fetch_assoc($result)) {
    $jobs[] = $row; 
}

// Set the response header to return JSON data
header('Content-Type: application/json');

// Encode the jobs array into JSON and output it
echo json_encode($jobs);

// Close the database connection
mysqli_close($conn);
?>
