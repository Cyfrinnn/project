<?php

// Enable detailed error reporting for debugging
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database connection
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "employer_db";

$conn = new mysqli($servername, $username, $password, $dbname);

// Debugging: Log all incoming raw JSON data for review
$input = file_get_contents("php://input");
file_put_contents("debug.log", date("Y-m-d H:i:s") . " - Raw Input Data: " . $input . PHP_EOL, FILE_APPEND);

// Check database connection
if ($conn->connect_error) {
    error_log("Database connection failed: " . $conn->connect_error);
    echo json_encode(["status" => "error", "message" => "Database connection failed: " . $conn->connect_error]);
    exit();
}

// Decode JSON payload
$data = json_decode($input, true);

if (!$data) {
    error_log("Invalid JSON received: " . $input);
    echo json_encode(["status" => "error", "message" => "Invalid JSON payload"]);
    exit();
}

// Retrieve and sanitize JSON data
$employer_id = isset($data['employer_id']) ? $conn->real_escape_string($data['employer_id']) : null;
$employer_email = isset($data['employer_email']) ? $conn->real_escape_string($data['employer_email']) : null;
$job_title = isset($data['job_title']) ? $conn->real_escape_string($data['job_title']) : null;
$company_name = isset($data['company_name']) ? $conn->real_escape_string($data['company_name']) : null;
$job_description = isset($data['job_description']) ? $conn->real_escape_string($data['job_description']) : null;
$salary_range = isset($data['salary_range']) ? $conn->real_escape_string($data['salary_range']) : null;
$job_type = isset($data['job_type']) ? $conn->real_escape_string($data['job_type']) : null;
$job_location = isset($data['job_location']) ? $conn->real_escape_string($data['job_location']) : null;

// Check for missing required fields
$required_fields = ['employer_id', 'job_title', 'company_name', 'job_description', 'job_location'];
$missing_fields = [];

foreach ($required_fields as $field) {
    if (empty($data[$field])) {
        $missing_fields[] = $field;
    }
}

if (!empty($missing_fields)) {
    error_log("Missing required fields: " . implode(", ", $missing_fields));
    echo json_encode(["status" => "error", "message" => "Missing required fields: " . implode(", ", $missing_fields)]);
    exit();
}

// Validate employer_id
$validate_employer_sql = "SELECT id FROM employers WHERE id = ?";
$stmt = $conn->prepare($validate_employer_sql);
$stmt->bind_param("s", $employer_id);
$stmt->execute();
$stmt->store_result();

if ($stmt->num_rows === 0) {
    error_log("Invalid employer ID: $employer_id");
    echo json_encode(["status" => "error", "message" => "Invalid employer ID!"]);
    $stmt->close();
    exit();
}
$stmt->close();

// Insert job post into the database
$insert_sql = "INSERT INTO job_posts (employer_id, job_title, company_name, job_description, salary_range, job_type, job_location) 
               VALUES (?, ?, ?, ?, ?, ?, ?)";
$stmt = $conn->prepare($insert_sql);
$stmt->bind_param(
    "sssssss",
    $employer_id,
    $job_title,
    $company_name,
    $job_description,
    $salary_range,
    $job_type,
    $job_location
);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Job post added successfully!"]);
    error_log("Job post added successfully for employer ID: $employer_id");
} else {
    error_log("Database error while adding job post: " . $stmt->error);
    echo json_encode(["status" => "error", "message" => "Error: " . $stmt->error]);
}
$stmt->close();

// Close database connection
$conn->close();
?>
