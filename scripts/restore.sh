#!/bin/bash

PG_DUMP="C:/Program Files/PostgreSQL/18/bin/pg_dump.exe"
PG_PSQL="C:/Program Files/PostgreSQL/18/bin/psql.exe"


# Настройки
DB_NAME="messenger_db"
DB_USER="${DB_USER:-gfedorov}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

# Цвета
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() { echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')]${NC} $1"; }
error() { echo -e "${RED}[$(date '+%Y-%m-%d %H:%M:%S')] ERROR:${NC} $1" >&2; }
warn() { echo -e "${YELLOW}[$(date '+%Y-%m-%d %H:%M:%S')] WARN:${NC} $1"; }
info() { echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')] INFO:${NC} $1"; }

# Проверяем аргументы
if [ -z "$1" ]; then
    echo -e "${RED}Usage:${NC} $0 <backup_file.sql.gz>"
    echo ""
    echo "Examples:"
    echo "  $0 ./backups/backup_messenger_db_2026-03-09_12-00-00.sql.gz"
    echo "  $0 /var/backups/postgresql/backup_messenger_db_2026-03-09_12-00-00.sql.gz"
    echo ""
    echo "Available backups:"
    ls -lht ./backups/backup_*.sql.gz 2>/dev/null | head -5 || echo "  No backups found in ./backups/"
    exit 1
fi

BACKUP_FILE="$1"

if [ ! -f "$BACKUP_FILE" ]; then
    error "Backup file not found: $BACKUP_FILE"
    exit 1
fi

info "=================================="
info "Project: messenger_db"
info "=================================="
info "Backup file: $BACKUP_FILE"
info "File size: $(du -h "$BACKUP_FILE" | cut -f1)"
info "Created: $(stat -c %y "$BACKUP_FILE" 2>/dev/null || stat -f %Sm "$BACKUP_FILE")"
echo ""

warn "⚠️  WARNING: This will OVERWRITE the database '$DB_NAME'"
warn "⚠️  All current data will be lost!"
echo ""
read -p "Type 'YES' to confirm restore: " CONFIRM

if [ "$CONFIRM" != "YES" ]; then
    log "Cancelled by user"
    exit 0
fi

# Бэкап текущей базы
CURRENT_BACKUP="./backups/pre_restore_$(date +%Y-%m-%d_%H-%M-%S).sql"
log "Creating backup of current database..."
if pg_dump -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME" > "$CURRENT_BACKUP" 2>/dev/null; then
    gzip "$CURRENT_BACKUP"
    log "Current database backed up to: ${CURRENT_BACKUP}.gz"
else
    warn "Failed to backup current database, continuing anyway..."
fi

# Восстанавливаем
log "Starting restore..."

if [[ "$BACKUP_FILE" == *.gz ]]; then
    log "Decompressing and restoring..."
    if gunzip -c "$BACKUP_FILE" | "$PG_PSQL" -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" 2>/dev/null; then
        log "Restore completed successfully ✓"
    else
        error "Restore failed!"
        exit 1
    fi
else
    log "Restoring from SQL file..."
    if psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" < "$BACKUP_FILE" 2>/dev/null; then
        log "Restore completed successfully ✓"
    else
        error "Restore failed!"
        exit 1
    fi
fi

# Проверяем
log "Verifying restore..."
TABLE_COUNT=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null)
log "Tables in database: $TABLE_COUNT"

log "=================================="
log "Restore completed successfully ✓"
log "Database: $DB_NAME"
log "Tables: $TABLE_COUNT"
log "=================================="

exit 0