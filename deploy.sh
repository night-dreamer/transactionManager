#!/bin/bash

PROJECT_NAME="transaction-manager"
JAR_NAME="TransactionManager-0.0.1-SNAPSHOT.jar"
IMAGE_NAME="${PROJECT_NAME}:latest"
CONTAINER_NAME="${PROJECT_NAME}-container"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# 1. 检查 JAR 文件是否存在
if [ ! -f "target/${JAR_NAME}" ]; then
    echo -e "${RED}错误：JAR 文件 target/${JAR_NAME} 不存在！请先运行 'mvn package' 构建项目。${NC}"
    exit 1
fi

# 2. 停止并删除旧容器（如果存在）
if [ "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    echo -e "${GREEN}停止旧容器...${NC}"
    docker stop ${CONTAINER_NAME}
    echo -e "${GREEN}删除旧容器...${NC}"
    docker rm ${CONTAINER_NAME}
fi

# 3. 构建 Docker 镜像
echo -e "${GREEN}构建 Docker 镜像（使用预构建的 JAR 文件）...${NC}"
docker build -t ${IMAGE_NAME} .

# 4. 启动容器（使用 docker-compose）
echo -e "${GREEN}启动服务...${NC}"
docker-compose up -d

# 5. 检查容器状态
echo -e "${GREEN}检查容器状态...${NC}"
sleep 5 # 等待容器启动
docker ps -a | grep ${CONTAINER_NAME}


echo -e "${GREEN}部署完成！访问 http://ip:8080/ 检查应用状态。${NC}"