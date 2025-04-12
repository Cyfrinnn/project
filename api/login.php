<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

session_start();

$postData = file_get_contents("php://input");
error_log("Raw POST data: " . $postData);

$request = json_decode($postData, true);

if (!$request || empty($request['email']) || empty($request['password']) || empty($request['loginType'])) {
    error_log("Error: Missing or invalid request parameters");
    echo json_encode(["error" => "Email, password, and login type are required"]);
    exit();
}

$email = $request['email'];
$password = $request['password'];
$loginType = $request['loginType'];
error_log("Email: $email, LoginType: $loginType");

// Connect to the database
$mysqli = new mysqli("localhost", "root", "", "employer_db");

// Check connection
if ($mysqli->connect_errno) {
    error_log("Database connection failed: " . $mysqli->connect_error);
    echo json_encode(["error" => "Database connection failed"]);
    exit();
}

// Prepare SQL statements based on login type
if ($loginType === 'applicant') {
    $stmt = $mysqli->prepare("SELECT id, password FROM applicants WHERE email = ?");
} elseif ($loginType === 'employer') {
    $stmt = $mysqli->prepare("SELECT id, password FROM employers WHERE email = ?");
} else {
    error_log("Error: Invalid login type provided: $loginType");
    echo json_encode(["error" => "Invalid login type"]);
    exit();
}
// Bind parameters and execute the query
$stmt->bind_param("s", $email);
$stmt->execute();
$stmt->store_result();

$response = [];

// Check if email exists
if ($stmt->num_rows > 0) {
    $stmt->bind_result($userId, $hashedPassword);
    $stmt->fetch();

    // Verify password
    if (password_verify($password, $hashedPassword)) {
        $_SESSION['user_id'] = $userId;
        $_SESSION['login_type'] = $loginType;

        $response["status"] = "success";
        $response["message"] = "Login successful";
        $response["role"] = $loginType;
        $response["user_id"] = $userId;

        // Fetch employer_id if login type is employer
        if ($loginType === 'employer') {
            $employerStmt = $mysqli->prepare("SELECT id FROM employers WHERE email = ?");
            $employerStmt->bind_param("s", $email);
            $employerStmt->execute();
            $employerStmt->bind_result($employerId);
            $employerStmt->fetch();

            if ($employerId) {
                $_SESSION['employer_id'] = $employerId;
                $response["employer_id"] = $employerId;
            } else {
                $response["employer_id"] = null; // Employer ID not found
            }

            $employerStmt->close();
        }
    } else {
        error_log("Password verification failed for email: $email");
        $response["error"] = "Invalid email or password";
    }
} else {
    error_log("No matching user found for email: $email");
    $response["error"] = "Invalid email or password";
}

// Send response
echo json_encode($response);

// Clean up
$stmt->close();
$mysqli->close();

?>
