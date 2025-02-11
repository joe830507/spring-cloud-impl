# wait-for-config-server.ps1
$configServerUrl = "http://localhost:8888/actuator/health"
$maxAttempts = 60
$attempt = 0
$delaySeconds = 2

Write-Host "Waiting for Config Server to start..."

while ($attempt -lt $maxAttempts) {
    try {
        $response = Invoke-WebRequest -Uri $configServerUrl -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            Write-Host "Config Server is up and running!"
            exit 0
        }
    }
    catch {
        $attempt++
        if ($attempt -lt $maxAttempts) {
            Write-Host "Attempt ${$attempt} of ${$maxAttempts}: Config Server not ready yet..."
            Start-Sleep -Seconds $delaySeconds
        }
        else {
            Write-Host "Config Server did not start within the expected time."
            exit 1
        }
    }
}