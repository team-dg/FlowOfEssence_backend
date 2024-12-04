set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE authentication_management_server;
    CREATE DATABASE userprofile_db;
    CREATE DATABASE pinpoint_db;
EOSQL