#!/bin/bash
# 장애 복구 스크립트: node0을 원래 Primary로 복구하는 스크립트

# 인자: 노드 ID, 호스트, 포트
node_id=$1
host=$2
port=$3

# 장애 복구 프로세스 시작
echo "Starting failback for node $node_id at $host:$port..."

# pg_basebackup을 사용하여 현재 Primary의 데이터를 node0으로 복사
# node_id가 0일 경우에만 동기화 진행 (원래 Primary 노드가 복구된 경우)
if [ "$node_id" -eq 0 ]; then
  # 동기화 작업 실행
  pg_basebackup -h $host -D /var/lib/postgresql/data -U repli -v -P --wal-method=stream
  echo "Data synchronization from current primary to node0 completed."

  # pgpool 재시작하여 node0을 마스터로 복구
  pgpool -d  # -d 옵션 사용하여 pgpool 데몬 재시작
  echo "Node0 restored as master."
else
  echo "Node $node_id is not the original master; no action taken."
fi

exit 0