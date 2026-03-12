#!/bin/bash



BACKUP_DIR="${BACKUP_DIR:-$(pwd)/../backups}"
DB_NAME="messenger_db"
MAX_AGE_HOURS=25

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${GREEN}==================================${NC}"
echo -e "${GREEN}Project: messenger_db${NC}"
echo -e "${GREEN}User: gfedorov${NC}"
echo -e "${GREEN}==================================${NC}"
echo ""
echo "Checking backups in: $BACKUP_DIR"
echo "Database: $DB_NAME"
echo "Max age: $MAX_AGE_HOURS hours"
echo ""

# Проверка директории
if [ ! -d "$BACKUP_DIR" ]; then
    echo -e "${RED} Backup directory does not exist: $BACKUP_DIR${NC}"
    exit 1
fi

# Поиск последнего бэкапа
LATEST_BACKUP=$(ls -t "$BACKUP_DIR"/backup_${DB_NAME}_*.sql.gz 2>/dev/null | head -1)

if [ -z "$LATEST_BACKUP" ]; then
    echo -e "${RED}❌ No backups found!${NC}"
    echo ""
    echo "Run backup first:"
    echo "  bash scripts/backup.sh"
    exit 1
fi

echo -e "${GREEN}✓ Latest backup:${NC} $LATEST_BACKUP"

# Проверка возраста (Windows совместимо)
BACKUP_TIME=$(stat -c %Y "$LATEST_BACKUP" 2>/dev/null || stat -f %m "$LATEST_BACKUP" 2>/dev/null)

if [ -z "$BACKUP_TIME" ]; then
    # Для Windows через PowerShell
    BACKUP_TIME=$(powershell -c "(Get-Item '$LATEST_BACKUP').LastWriteTime.ToFileTime()" 2>/dev/null)
fi

CURRENT_TIME=$(date +%s)
AGE_HOURS=$(( (CURRENT_TIME - BACKUP_TIME) / 3600 ))

echo "  Age: $AGE_HOURS hours"

if [ $AGE_HOURS -gt $MAX_AGE_HOURS ]; then
    echo -e "${RED}❌ Backup is too old! (older than $MAX_AGE_HOURS hours)${NC}"
    echo ""
    echo "Create new backup:"
    echo "  bash scripts/backup.sh"
    exit 1
else
    echo -e "${GREEN}✓ Backup age is OK${NC}"
fi

# Проверка целостности
echo ""
echo "Checking integrity..."
if gzip -t "$LATEST_BACKUP" 2>/dev/null; then
    echo -e "${GREEN}✓ Backup integrity OK${NC}"
else
    echo -e "${RED}❌ Backup is corrupted!${NC}"
    exit 1
fi

# Размер
BACKUP_SIZE=$(du -h "$LATEST_BACKUP" 2>/dev/null | cut -f1)
if [ -z "$BACKUP_SIZE" ]; then
    # Для Windows
    BACKUP_SIZE=$(powershell -c "(Get-Item '$LATEST_BACKUP').Length / 1KB" 2>/dev/null | xargs printf "%.0f KB")
fi
echo "  Size: $BACKUP_SIZE"

# Все бэкапы
echo ""
echo "All backups:"
ls -lht "$BACKUP_DIR"/backup_${DB_NAME}_*.sql.gz 2>/dev/null | head -10

# Статистика
TOTAL_BACKUPS=$(find "$BACKUP_DIR" -name "backup_${DB_NAME}_*.sql.gz" 2>/dev/null | wc -l)
TOTAL_SIZE=$(du -sh "$BACKUP_DIR" 2>/dev/null | cut -f1 || echo "unknown")

echo ""
echo "Statistics:"
echo "  Total backups: $TOTAL_BACKUPS"
echo "  Total size: $TOTAL_SIZE"

echo ""
echo -e "${GREEN}==================================${NC}"
echo -e "${GREEN}✅ All checks passed!${NC}"
echo -e "${GREEN}==================================${NC}"
exit 0