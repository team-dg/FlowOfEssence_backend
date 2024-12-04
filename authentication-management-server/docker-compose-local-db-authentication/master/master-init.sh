#!/bin/bash

# 설정 파일이 있는지 확인하고 없으면 생성
if [ ! -f "$PGDATA/postgresql.conf" ]; then
    # 기본 PostgreSQL 설정 파일 복사
    cp /usr/share/postgresql/postgresql.conf.sample $PGDATA/postgresql.conf
    cp /usr/share/postgresql/pg_hba.conf.sample $PGDATA/pg_hba.conf
fi

# postgresql.conf 수정
cat >> "$PGDATA/postgresql.conf" << EOF
listen_addresses = '*'
port = 5433
synchronous_commit = remote_apply
synchronous_standby_names = '*'
wal_level = replica
max_wal_senders = 10
wal_keep_size = 1GB
wal_log_hints = on
hot_standby = on
EOF

# pg_hba.conf 설정
cat >> "$PGDATA/pg_hba.conf" << EOF
host replication repli 0.0.0.0/0 trust
EOF

# 복제 사용자 생성 (PostgreSQL이 실행 중일 때만)
psql -U postgres -c "CREATE USER repli REPLICATION LOGIN PASSWORD '1234';"
