Set-Location "C:\Users\suraw\Documents\GitHub\OOP-mini-project"
Write-Host "Compiling..."
javac -cp "lib/*" -d bin src\*.java App\*.java
Write-Host "Running..."
java -cp "bin;lib/*" MainDashboard
