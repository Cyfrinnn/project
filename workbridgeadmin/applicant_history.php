<?php
include 'config.php';

// Get the applicant ID from the URL
$applicant_id = isset($_GET['id']) ? (int)$_GET['id'] : 0;

// Fetch applicant details
$sql = "SELECT * FROM applicants WHERE id = $applicant_id";
$result = $conn->query($sql);
$applicant = $result->fetch_assoc();

// Fetch hiring history (adjust table and column names based on your database structure)
$sql_history = "SELECT company_name, hired_date FROM hiring_history WHERE applicant_id = $applicant_id";
$result_history = $conn->query($sql_history);

if (!$applicant) {
    header('Location: applicants.php');
    exit();
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Applicant History</title>
    <link rel="stylesheet" href="applicants.css">
</head>
<body>
    <div class="content">
        <h1>History of <?php echo $applicant['full_name']; ?></h1>
        <p><strong>Email:</strong> <?php echo $applicant['email']; ?></p>
        <p><strong>Address:</strong> <?php echo $applicant['address']; ?></p>

        <h2>Hiring History</h2>
        <table class="applicants-table">
            <thead>
                <tr>
                    <th>Company Name</th>
                    <th>Date Hired</th>
                </tr>
            </thead>
            <tbody>
                <?php
                if ($result_history->num_rows > 0) {
                    while ($row = $result_history->fetch_assoc()) {
                        echo "<tr>
                            <td>{$row['company_name']}</td>
                            <td>{$row['hired_date']}</td>
                        </tr>";
                    }
                } else {
                    echo "<tr><td colspan='4'>No hiring history found</td></tr>";
                }
                ?>
            </tbody>
        </table>
        <a href="applicants.php"><p>Back</p></a>
    </div>
</body>
</html>
