Param(
  [string]$Tag = "0.0.1",
  [string]$Platform = "linux/amd64",
  [string]$Profile = "minikube"
)

$ErrorActionPreference = "Stop"

$Builds = @(
  @{ Image = "exchange"; Dockerfile = "DockerfileExchange" },
  @{ Image = "exchange-generator"; Dockerfile = "DockerfileExchangeGenerator" }
#,
#  @{ Image = "accounts"; Dockerfile = "DockerfileAccounts" },
#  @{ Image = "front";    Dockerfile = "DockerfileFront" }
)

function Build-And-Load($image, $dockerfile, $tag, $platform, $profile) {
  Write-Host "Building $($image):$tag  Dockerfile=$dockerfile, platform=$platform"
  docker build  -t "$($image):$tag"  -f "$dockerfile"  --platform "$platform" .
  if ($LASTEXITCODE -ne 0) { throw "Build failed: $($image):$tag" }

  Write-Host "Loading into minikube: $($image):$tag"
  minikube image load $($image):$tag -p $profile --overwrite
  if ($LASTEXITCODE -ne 0) { throw "Minikube load failed: $($image):$tag" }

  Write-Host "Done: $($image):$tag"
}

foreach ($b in $Builds) {
  Build-And-Load -image $b.Image -dockerfile $b.Dockerfile -tag $Tag -platform $Platform -profile $Profile
}

Write-Host "🎉 All builds done."