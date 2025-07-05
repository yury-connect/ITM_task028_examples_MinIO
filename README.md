# MinIO

### Тестирую функционал MinIO

### Простое приложение.

Пишу с нуля, момогает DeepSeek

---
## Запуск и тестирование
### 1. Запустите MinIO через Docker:
```bash
docker run -p 9000:9000 -p 9001:9001 minio/minio server /data --console-address ":9001"
````

### 2. Запустите приложение:
```bash
mvn spring-boot:run
```

### 3. Тестируйте API:
#### Загрузка файла:
```bash
curl -X POST -F "file=@/path/to/your/file.jpg" http://localhost:8080/api/files 
```

#### Скачивание файла:
```bash
curl -OJ http://localhost:8080/api/files/filename.jpg
```

#### Удаление файла:
```bash
curl -X DELETE http://localhost:8080/api/files/filename.jpg
```

#### Список бакетов:
```bash
curl http://localhost:8080/api/files/buckets
```

#### Получение списка файлов:








---
#### Для HTTPS используйте:
```yaml
minio:
  endpoint: https://your-minio-server.com
  # остальные параметры
```

#### Для HTTP используйте:
```yaml
minio:
  endpoint: http://localhost:9000
  # остальные параметры
```

---

