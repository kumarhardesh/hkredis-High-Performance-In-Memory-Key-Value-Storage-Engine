$client = New-Object System.Net.Sockets.TcpClient('localhost', 30007)
$stream = $client.GetStream()
$writer = New-Object System.IO.StreamWriter($stream)
$reader = New-Object System.IO.StreamReader($stream)

# 1. PING with payload argument
$writer.Write("*2`r`n`$4`r`nPING`r`n`$12`r`nbenchmarking`r`n")
$writer.Flush()
$len = $reader.ReadLine()
$val = $reader.ReadLine()
Write-Host "PING Payload Test: $val"

# 2. COMMAND endpoint metadata test
$writer.Write("*1`r`n`$7`r`nCOMMAND`r`n")
$writer.Flush()
$cmdRes =$reader.ReadLine()
Write-Host "COMMAND Metadata Response: $cmdRes"

$client.Close()