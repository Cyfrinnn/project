<?php
// Enable error reporting for debugging
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database connection details
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "employer_db";

// Create a connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    echo json_encode(['error' => 'Database connection failed: ' . $conn->connect_error]);
    exit();
}

// Decode JSON data from POST request
$data = json_decode(file_get_contents('php://input'), true);

// Validate input fields
if (empty($data['full_name']) || empty($data['email']) || empty($data['password'])) {
    echo json_encode(['error' => 'All fields are required']);
    exit();
}

// Sanitize and assign variables
$full_name = $data['full_name'];
$email = $data['email'];
$password = $data['password'];

// Hash the password for security
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

// Check if email already exists
$emailCheck = "SELECT email FROM applicants WHERE email = ?";
$stmt = $conn->prepare($emailCheck);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // Duplicate email found
    echo json_encode(['error' => 'An account with this email already exists']);
    $stmt->close();
    $conn->close();
    exit();
}

// Insert data into applicants table
$insertQuery = "INSERT INTO applicants (full_name, email, password) VALUES (?, ?, ?)";
$stmt = $conn->prepare($insertQuery);
$stmt->bind_param("sss", $full_name, $email, $hashed_password);

if ($stmt->execute()) {
    echo json_encode(['status' => 'success', 'message' => 'Applicant registered successfully']);
} else {
    echo json_encode(['error' => 'Error registering applicant: ' . $stmt->error]);
}

// Close statement and connection
$stmt->close();
$conn->close();
?>
