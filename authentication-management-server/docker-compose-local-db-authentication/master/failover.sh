#!/bin/bash

failed_node=$1
trigger_file=$2

# 기본 노드가 실패한 경우에만 장애 조치 수행
# failed_node가 Primary 노드(0번 노드)일 경우
if [ $failed_node -eq 0 ]; then
  echo "Primary node failed. Triggering failover..."
  
  # 복제본 중 하나에 트리거 파일 생성하여 승격
  touch $trigger_file
else
  # 복제본 장애 발생 시 별도 조치 없음
  echo "Replica node failed. No failover action needed."
  exit 0
fi

exit 0