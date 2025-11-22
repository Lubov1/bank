Param(
  [string]$Tag = "0.0.1",
  [string]$Platform = "linux/amd64",
  [string]$Profile = "minikube"
)

$ErrorActionPreference = "Stop"

$Builds = @(
  @{ Image = "exchange"; Dockerfile = "DockerfileExchange" },
  @{ Image = "exchange-generator"; Dockerfile = "DockerfileExchangeGenerator" }
,
  @{ Image = "accounts"; Dockerfile = "DockerfileAccountService" },
  @{ Image = "front";    Dockerfile = "DockerfileFront" }
,
  @{ Image = "blocker"; Dockerfile = "DockerfileBlocker" },
  @{ Image = "cash";    Dockerfile = "DockerfileCashService" },
  @{ Image = "notifications"; Dockerfile = "DockerfileNotificationService" },
  @{ Image = "transfer";    Dockerfile = "DockerfileTransfer" }

)

#function Build-And-Load($image, $dockerfile, $tag, $platform, $profile) {
#  Write-Host "Building $($image):$tag  Dockerfile=$dockerfile, platform=$platform"
#  docker build  -t "$($image):$tag"  -f "$dockerfile"  --platform "$platform" .
#  if ($LASTEXITCODE -ne 0) { throw "Build failed: $($image):$tag" }
#
#  Write-Host "Loading into minikube: $($image):$tag"
#  minikube image load $($image):$tag -p $profile --overwrite
#  if ($LASTEXITCODE -ne 0) { throw "Minikube load failed: $($image):$tag" }
#
#  Write-Host "Done: $($image):$tag"
#}
$RootDir = $PSScriptRoot
$jobs = foreach ($b in $Builds) {
  Start-Job -Name $b.Image -ScriptBlock {
    param($image, $dockerfile, $tag, $platform, $profile, $rootDir)

    try {
      Set-Location $rootDir
      Write-Host "Building $($image):$tag  Dockerfile=$dockerfile, platform=$platform"
      docker build  -t "$($image):$tag"  -f "$dockerfile"  --platform "$platform" . 2>&1 |
      ForEach-Object { Write-Host $_ }

      if ($LASTEXITCODE -ne 0) { throw "Build failed: $($image):$tag" }

      Write-Host "Loading into minikube: $($image):$tag"

      minikube image load $($image):$tag -p $profile --overwrite 2>&1 |
      ForEach-Object { Write-Host $_ }

      if ($LASTEXITCODE -ne 0) { throw "Minikube load failed: $($image):$tag" }

      Write-Host "Done: $($image):$tag"
    }
    catch {
      Write-Error $_
      exit 1
    }
  } -ArgumentList $b.Image, $b.Dockerfile, $Tag, $Platform, $Profile, $RootDir
}
while ($jobs.Count -gt 0) {
  $done = Wait-Job -Job $jobs -Any   # ждём любой завершившийся job
  Receive-Job -Job $done -Keep       # выводим его логи
  $jobs = $jobs | Where-Object Id -ne $done.Id  # убираем его из списка
}

## ждём завершения всех задач
#Wait-Job $jobs | Out-Null
#
## забираем вывод всех
#Receive-Job $jobs -Keep
#
## проверяем, кто упал
#$failed = $jobs | Where-Object State -ne 'Completed'
#if ($failed) {
#  Write-Host "❌ Failed jobs:"
#  $failed | Select-Object Id, Name, State
#  exit 1
#}
#else {
#  Write-Host "🎉 All builds done."
#}