#!/bin/bash

if [ -f "$PGDATA/postgresql.conf" ]; then
    echo "Data directory already initialized. Skipping replication setup."
    rm -rf $PGDATA/*
fi

# PostgreSQL 서비스가 완전히 시작될 때까지 대기
until pg_isready -h postgres_master -p 5433 -U repli -d authentication_management_server
do
  echo "Waiting for master to be ready..."
  sleep 2
done

# 복제 서버 초기화 및 기본 설정
pg_basebackup -h postgres_master -p 5433 -D $PGDATA -U repli -v -P --wal-method=stream

# standby 설정
cat >> "$PGDATA/postgresql.conf" << EOF
listen_addresses = '*'
hot_standby = on
port = 5434
max_connections = 100
dynamic_shared_memory_type = posix
max_wal_senders = 10
wal_level = replica
effective_cache_size = 64MB
shared_buffers = 64MB
work_mem = 4MB
EOF
echo "promote_trigger_file = '$PGDATA/down.trg'" >> $PGDATA/postgresql.conf
echo "primary_conninfo = 'host=postgres_master port=5433 user=repli password=1234'" >> $PGDATA/postgresql.auto.conf

# standby.signal 파일 생성
touch "$PGDATA/standby.signal"

# pg_hba.conf 설정
cat >> "$PGDATA/pg_hba.conf" << EOF
host all all 0.0.0.0/0 trust
EOF

# 데이터 디렉토리 권한 설정
chown -R postgres:postgres $PGDATA
