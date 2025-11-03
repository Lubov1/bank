#!/usr/bin/env bash
set -e

TAG="0.0.1"
PLATFORM="linux/amd64"

DOCKERFILE="DockerfileExchange"
APP_NAME="exchange"

echo "🧱 Building image..."
docker build -t ${APP_NAME}:${TAG} -f ${DOCKERFILE} --platform ${PLATFORM} .

echo "📦 Loading image into minikube..."
minikube image load ${APP_NAME}:${TAG}

DOCKERFILE="DockerfileExchangeGenerator"
APP_NAME="exchange-generator"

echo "🧱 Building image..."
docker build -t ${APP_NAME}:${TAG} -f ${DOCKERFILE} --platform ${PLATFORM} .

echo "📦 Loading image into minikube..."
minikube image load ${APP_NAME}:${TAG}