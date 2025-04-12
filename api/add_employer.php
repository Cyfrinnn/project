<?php
header('Content-Type: application/json');

// Enable error reporting for debugging
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database connection credentials
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "employer_db"; 

// Create a connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check for connection errors
if ($conn->connect_error) {
    echo json_encode(['error' => 'Database connection failed: ' . $conn->connect_error]);
    exit();
}

// Get raw input data
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Validate input data (ensures no empty fields)
if (empty($data['company_name']) || empty($data['email']) || empty($data['password'])) {
    echo json_encode(['error' => 'All fields are required']);
    exit();
}

// Extract and sanitize input values
$company_name = $data['company_name'];
$email = $data['email'];
$password = password_hash($data['password'], PASSWORD_DEFAULT); // Hash the password securely

// Check for duplicate email in the database
$emailCheck = "SELECT email FROM employers WHERE email = ?";
$stmt = $conn->prepare($emailCheck);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // Email is already registered
    echo json_encode(['error' => 'An account with this email already exists']);
    $stmt->close();
    $conn->close();
    exit();
}

// Insert new employer data into the database
$insertQuery = "INSERT INTO employers (company_name, email, password) VALUES (?, ?, ?)";
$stmt = $conn->prepare($insertQuery);
$stmt->bind_param("sss", $company_name, $email, $password);

if ($stmt->execute()) {
    echo json_encode(['message' => 'Employer added successfully']);
} else {
    echo json_encode(['error' => 'Error adding employer: ' . $stmt->error]);
}

// Close prepared statement and database connection
$stmt->close();
$conn->close();
?>
