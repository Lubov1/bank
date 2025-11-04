#!/usr/bin/env bash
set -euo pipefail


TAG="${TAG:-0.0.1}"
PLATFORM="${PLATFORM:-linux/amd64}"

DOCKER_REGISTRY="${DOCKER_REGISTRY:-ghcr.io/lubov1}"

IMAGES=(
  "exchange:DockerfileExchange"
  "exchange-generator:DockerfileExchangeGenerator"
  "accounts:DockerfileAccountService"
  "front:DockerfileFront"
  "blocker:DockerfileBlocker"
  "cash:DockerfileCashService"
  "notifications:DockerfileNotificationService"
  "transfer:DockerfileTransfer"
)

build_image() {
  local image="$1"
  local dockerfile="$2"

  echo "🧱 Building ${image}:${TAG} (Dockerfile=${dockerfile}, platform=${PLATFORM})"
  docker build \
    -t "${image}:${TAG}" \
    -f "${dockerfile}" \
    --platform "${PLATFORM}" .

  echo "✅ Built ${image}:${TAG}"

  # Если указан реестр — пушим туда
  if [[ -n "${DOCKER_REGISTRY}" ]]; then
    local target="${DOCKER_REGISTRY}/${image}:${TAG}"
    echo "📤 Pushing ${target}"
    docker tag "${image}:${TAG}" "${target}"
    docker push "${target}"
  fi
}

for entry in "${IMAGES[@]}"; do
  IFS=":" read -r IMAGE_NAME DOCKERFILE_NAME <<< "${entry}"
  build_image "${IMAGE_NAME}" "${DOCKERFILE_NAME}"
done

echo "🎉 All images built${DOCKER_REGISTRY:+ and pushed}."