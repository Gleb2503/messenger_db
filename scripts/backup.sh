#!/bin/bash


PG_DUMP="C:/Program Files/PostgreSQL/18/bin/pg_dump.exe"
PG_PSQL="C:/Program Files/PostgreSQL/18/bin/psql.exe"
export PGPASSWORD="270105"

# Проверка
if [ ! -f "$PG_DUMP" ]; then
    echo "pg_dump.exe не найден!"
    echo "Проверьте путь: $PG_DUMP"
    exit 1
fi

echo "✓ Используем PostgreSQL 18"

# Настройки
BACKUP_DIR="${BACKUP_DIR:-$(pwd)/backups}"
DB_NAME="messenger_db"
DB_USER="${DB_USER:-gfedorov}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
RETENTION_DAYS="${RETENTION_DAYS:-7}"

# Цвета
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() { echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')]${NC} $1"; }
error() { echo -e "${RED}[$(date '+%Y-%m-%d %H:%M:%S')] ERROR:${NC} $1" >&2; }
warn() { echo -e "${YELLOW}[$(date '+%Y-%m-%d %H:%M:%S')] WARN:${NC} $1"; }
info() { echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')] INFO:${NC} $1"; }

# Создаём директорию
mkdir -p "$BACKUP_DIR"

if [ ! -d "$BACKUP_DIR" ]; then
    error "Cannot create backup directory: $BACKUP_DIR"
    exit 1
fi

# Имя файла
DATE=$(date +%Y-%m-%d_%H-%M-%S)
BACKUP_FILE="$BACKUP_DIR/backup_${DB_NAME}_$DATE.sql"
BACKUP_FILE_GZ="${BACKUP_FILE}.gz"

info "=================================="
info "Project: messenger_db"
info "PostgreSQL: 18"
info "=================================="
log "Starting backup of database '$DB_NAME'"
log "Host: $DB_HOST:$DB_PORT"
log "User: $DB_USER"
log "Backup directory: $BACKUP_DIR"

# Создаём бэкап
log "Creating backup..."
if "$PG_DUMP" -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME" > "$BACKUP_FILE" 2>/dev/null; then
    log "Backup created: $BACKUP_FILE"
else
    error "Failed to create backup. Check database connection."
    error "Make sure PostgreSQL is running and password is correct"
    exit 1
fi

# Размер
BACKUP_SIZE=$(du -h "$BACKUP_FILE" 2>/dev/null | cut -f1 || echo "unknown")
log "Backup size: $BACKUP_SIZE"

# Сжимаем
log "Compressing backup..."
if gzip "$BACKUP_FILE"; then
    log "Backup compressed: $BACKUP_FILE_GZ"
    COMPRESSED_SIZE=$(du -h "$BACKUP_FILE_GZ" 2>/dev/null | cut -f1 || echo "unknown")
    log "Compressed size: $COMPRESSED_SIZE"
else
    error "Failed to compress backup"
    exit 1
fi

# Проверяем целостность
log "Verifying backup integrity..."
if gzip -t "$BACKUP_FILE_GZ"; then
    log "Backup integrity verified ✓"
else
    error "Backup integrity check failed!"
    exit 1
fi

# Удаляем старые
log "Cleaning up backups older than $RETENTION_DAYS days..."
OLD_BACKUPS=$(find "$BACKUP_DIR" -name "backup_${DB_NAME}_*.sql.gz" -mtime +$RETENTION_DAYS 2>/dev/null)

if [ -n "$OLD_BACKUPS" ]; then
    echo "$OLD_BACKUPS" | while read -r old_backup; do
        warn "Removing: $old_backup"
        rm -f "$old_backup"
    done
    log "Old backups removed"
else
    log "No old backups to remove"
fi

# Статистика
TOTAL_BACKUPS=$(find "$BACKUP_DIR" -name "backup_${DB_NAME}_*.sql.gz" 2>/dev/null | wc -l)
TOTAL_SIZE=$(du -sh "$BACKUP_DIR" 2>/dev/null | cut -f1 || echo "unknown")

log "=================================="
log "Backup completed successfully ✓"
log "Total backups: $TOTAL_BACKUPS"
log "Total size: $TOTAL_SIZE"
log "Latest backup: $BACKUP_FILE_GZ"
log "=================================="

exit 0