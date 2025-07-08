# MinIO

### Тестирую функционал MinIO

### Простое приложение.

Пишу с нуля, момогает DeepSeek

---
## Запуск и тестирование

## 1. Запустите MinIO через Docker:
```bash
docker run -p 9000:9000 -p 9001:9001 minio/minio server /data --console-address ":9001"
````

---
## 2. Запустите приложение:
```bash
mvn spring-boot:run
```

---
## 3. Тестируйте API:

### Убедитесь, что файл существует:
```bash
 Test-Path "in_out_src_folder/UpLoad/file.jpg"
```

---
### Загрузка файла:
Перейти в корень в `powershell` проекта и выполнить:
```powershell
curl.exe -X POST -F "file=@in_out_src_folder/UpLoad/file.jpg" http://localhost:8080/api/files
```
тот-же эффект через `CMD`
```bash
curl.exe -X POST -F "file=@in_out_src_folder/UpLoad/file.jpg" http://localhost:8080/api/files
```

---
### Скачивание файла:
1. Использование wget (аналог в PowerShell):
```bash
Invoke-WebRequest -Uri "http://localhost:8080/api/files/file.jpg" -OutFile "in_out_src_folder/DownLoad/file.jpg"
```

2. Явное указание curl.exe с полным путем:

Указание полного пути для сохранения (лучший способ)
```bash
C:\Windows\System32\curl.exe -o "C:\Users\Yury\IdeaProjects\IT-Mentor\ITM_tasks\ITM_task028_examples_MinIO\in_out_src_folder\DownLoad\file.jpg" http://localhost:8080/api/files/file.jpg
```

Относительный путь _(если запускаете из корня проекта)_
```bash
C:\Windows\System32\curl.exe -o "in_out_src_folder\DownLoad\file.jpg" http://localhost:8080/api/files/file.jpg 
```
Почему -O не работает как нужно: Флаг -O (большая латинская O) в curl всегда сохраняет файл в текущую директорию с оригинальным именем. Для указания пути нужно использовать -o (маленькая латинская o).

3. Если нужно сохранить под другим именем:
```bash
curl.exe -o "in_out_src_folder/DownLoad/my_file.jpg" "http://localhost:8080/api/files/file.jpg"
```

---
### Удаление файла:

Для команды `Invoke-WebRequest`
```bash
Invoke-WebRequest -Uri "http://localhost:8080/api/files/file.jpg" -Method DELETE
```

Или коротко:
```bash
iwr http://localhost:8080/api/files/file.jpg -Method DELETE
````

Либо
```bash
curl.exe -X DELETE http://localhost:8080/api/files/file.jpg
```

---
### Список бакетов:
```bash
curl.exe -O http://localhost:8080/api/files/file.jpg
```

---
### Получение списка файлов:

---
#### [Ссылка](https://lunar-equinox-676595.postman.co/workspace/ITM_Yury_Workspace~8f191f88-e8ec-40af-b710-248189b66b12/collection/8967691-6fed959b-969e-4a1a-a070-37388fae7ce1?action=share&creator=8967691) на коллекцию запросов в **Postman**

#### [Ссылка](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/main/Documents/TECHNOLOGIES/S3/MinIO/MinIO_info.md) на теорию по **MinIO**  

---








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
# Зайти через Web - интерфейс: 
###### Вводим данные из _конфигурации_ `application.yml`:
- URL: [http://localhost:9001/](http://localhost:9001/browser/my-bucket)
- Username: `minioadmin`
- Password: `minioadmin`

Web - интерфейс позволяет работать с загруженными файлами и бакетами.

---
