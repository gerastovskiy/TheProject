#!/bin/bash

# ========== КОНФИГУРАЦИЯ ==========
BASE_DIR=$(dirname "$0")
DOCKER_IMAGE_TAG="0.1"
DOCKER_COMPOSE_FILE="./docker/docker-compose.yaml"

# Список сервисов
SERVICES=(
    "./gateway"
    "./auth-service"
    "./user-service"
    "./billing-service"
    "./order-service"
    "./notification-service"
)


# Сборка Java-приложения
build_application() {
    local service_path="$1"
    echo -e "\n\033[1;34m[BUILD] Собираю $service_path...\033[0m"
    
    if [ ! -f "$service_path/gradlew" ]; then
        echo -e "\033[1;31mОшибка: gradlew не найден в $service_path\033[0m"
        return 1
    fi
    
    (cd "$service_path" && ./gradlew clean build)
    if [ $? -eq 0 ]; then
        echo -e "\033[1;32m[BUILD] Успешно: $service_path\033[0m"
        return 0
    else
        echo -e "\033[1;31m[BUILD] Ошибка при сборке $service_path\033[0m"
        return 1
    fi
}

# Сборка Docker-образа
build_docker_image() {
    local service_path="$1"
    local service_name=$(basename "$service_path")
    echo -e "\n\033[1;34m[DOCKER] Собираю образ $service_name:$DOCKER_IMAGE_TAG...\033[0m"
    
    if [ ! -f "$service_path/Dockerfile" ]; then
        echo -e "\033[1;31mОшибка: Dockerfile не найден в $service_path\033[0m"
        return 1
    fi
    
    (cd "$service_path" && docker build -t "${service_name}:${DOCKER_IMAGE_TAG}" .)
    if [ $? -eq 0 ]; then
        echo -e "\033[1;32m[DOCKER] Образ $service_name:$DOCKER_IMAGE_TAG успешно собран\033[0m"
        return 0
    else
        echo -e "\033[1;31m[DOCKER] Ошибка при сборке образа $service_name\033[0m"
        return 1
    fi
}

# Запуск через docker-compose
run_with_docker_compose() {
    echo -e "\n\033[1;34m[DOCKER] Запускаю сервисы через docker-compose...\033[0m"
    
    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        echo -e "\033[1;31mОшибка: $DOCKER_COMPOSE_FILE не найден\033[0m"
        return 1
    fi
    
    docker-compose -f "$DOCKER_COMPOSE_FILE" up -d
    if [ $? -eq 0 ]; then
        echo -e "\033[1;32m[DOCKER] Сервисы успешно запущены\033[0m"
        return 0
    else
        echo -e "\033[1;31m[DOCKER] Ошибка при запуске сервисов\033[0m"
        return 1
    fi
}

# Полная очистка docker-compose ресурсов
clean_docker_compose() {
    echo -e "\n\033[1;34m[DOCKER] Останавливаю и удаляю все сервисы из docker-compose...\033[0m"
    
    if [ -f "$DOCKER_COMPOSE_FILE" ]; then
        docker-compose -f "$DOCKER_COMPOSE_FILE" down --rmi all --volumes --remove-orphans
        if [ $? -eq 0 ]; then
            echo -e "\033[1;32m[DOCKER] Все ресурсы docker-compose удалены\033[0m"
            return 0
        else
            echo -e "\033[1;31m[DOCKER] Ошибка при удалении ресурсов\033[0m"
            return 1
        fi
    else
        echo -e "\033[1;33m[DOCKER] Файл $DOCKER_COMPOSE_FILE не найден, пропускаю очистку\033[0m"
        return 0
    fi
}

# Очистка всех docker-ресурсов для сервисов
clean_docker_resources() {
    echo -e "\n\033[1;34m[DOCKER] Начинаю полную очистку Docker-ресурсов...\033[0m"
    
    # Очищаем docker-compose
    clean_docker_compose
    
    # Удаляем отдельные образы сервисов
    for service_path in "${SERVICES[@]}"; do
        local service_name=$(basename "$service_path")
        echo -e "\n\033[1;36m[DOCKER] Удаляю образ $service_name:$DOCKER_IMAGE_TAG...\033[0m"
        docker rmi -f "${service_name}:${DOCKER_IMAGE_TAG}" 2>/dev/null
    done
    
    # Очищаем оставшиеся ресурсы
    echo -e "\n\033[1;36m[DOCKER] Очищаю оставшиеся ресурсы...\033[0m"
    docker system prune -af --volumes
    
    echo -e "\n\033[1;32m[DOCKER] Все ресурсы успешно удалены\033[0m"
}

# Проверка путей сервисов
validate_services() {
    echo -e "\n\033[1;35m[VALIDATE] Проверяю сервисы...\033[0m"
    local valid=true
    
    for service_path in "${SERVICES[@]}"; do
        local abs_path=$(realpath "$BASE_DIR/$service_path" 2>/dev/null)
        
        if [ ! -d "$abs_path" ]; then
            echo -e "\033[1;31m[VALIDATE] Ошибка: сервис $service_path не найден ($abs_path)\033[0m"
            valid=false
        else
            echo -e "\033[1;32m[VALIDATE] OK: $service_path -> $abs_path\033[0m"
        fi
    done
    
    $valid
}

# Главное меню
show_menu() {
    echo -e "\n\033[1;33m========== МЕНЮ УПРАВЛЕНИЯ ==========\033[0m"
    echo "1) Собрать все Java-приложения"
    echo "2) Собрать все Docker-образы"
    echo "3) Запустить через docker-compose"
    echo "4) Полный цикл (сборка+запуск)"
    echo "5) Полная очистка Docker-ресурсов"
    echo "6) Остановить только docker-compose"
    echo "7) Проверить конфигурацию"
    echo "8) Выход"
    echo -e "\033[1;33m===================================\033[0m"
}

# Переходим в директорию скрипта
cd "$BASE_DIR" || { echo -e "\033[1;31mОшибка: невозможно перейти в $BASE_DIR\033[0m"; exit 1; }

# Главный цикл
while true; do
    show_menu
    read -p "Выберите действие (1-8): " choice
    
    case $choice in
        1)  # Сборка приложений
            for service_path in "${SERVICES[@]}"; do
                abs_path=$(realpath "$BASE_DIR/$service_path")
                if ! build_application "$abs_path"; then
                    echo "Ошибка сборки приложения">&2
                    return 1
                fi
            done
            ;;
        2)  # Сборка образов
            for service_path in "${SERVICES[@]}"; do
                abs_path=$(realpath "$BASE_DIR/$service_path")
                if ! build_docker_image "$abs_path"; then
                    echo "Ошибка сборки образов">&2
                    return 1
             fi
            done
            ;;
        3)  # Запуск
            if ! run_with_docker_compose; then
                echo "Ошибка запуска docker_compose">&2
                return 1
            fi
            ;;
        4)  # Полный цикл
            for service_path in "${SERVICES[@]}"; do
                abs_path=$(realpath "$BASE_DIR/$service_path")
                if ! build_application "$abs_path"; then
                    echo "Ошибка сборки приложения">&2
                    return 1
                fi
                if ! build_docker_image "$abs_path"; then
                    echo "Ошибка сборки образов">&2
                    return 1
                fi
            done
            
            if ! run_with_docker_compose; then
                echo "Ошибка запуска docker_compose">&2
                return 1
            fi
            ;;
        5)  # Полная очистка
            clean_docker_resources
            ;;
        6)  # Остановка docker-compose
            clean_docker_compose
            ;;
        7)  # Проверка конфигурации
            validate_services
            ;;
        8)  # Выход
            echo -e "\n\033[1;32mРабота скрипта завершена\033[0m"
            exit 0
            ;;
        *)
            echo -e "\n\033[1;31mНеверный выбор. Пожалуйста, выберите вариант от 1 до 8.\033[0m"
            ;;
    esac
    
    read -p "Нажмите Enter чтобы продолжить..."
done