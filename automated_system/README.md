## Запуск
**Команда:**

`java -jar your-jar-file.jar  --config.path=<path-to-inputConfig.xml> --spring.config.location=file:/path/to/application.properties`

**Запуск с конфигами из этой папки:**

`java -jar automatedAccountingSystem-1.0-SNAPSHOT.jar --config.path=inputConfig.xml --spring.config.location=file:application.properties`

## Получение метрик
**Команда:**

`curl http://<ip>:<port>/<path>`
port задаётся в spring.config как server.port
path:
* statistic - вся статистика
* statistic/bufferedValueAmount - количество данных в буфере на данный момент
* statistic/bufferedValueProcent - процент заполнения буфера

* statistic/isNetworkConnected

* statistic/written - список последних записанных значений (размер буфера задаётся в spring.config как written_logs_buffer_capacity)
* statistic/thrown - список последних отброшенных значений (размер буфера задаётся в spring.config как thrown_logs_buffer_capacity)

**Bызов с текущими настройками:**

* `curl http://localhost:2444/statistic`


* `curl http://localhost:2444/statistic/bufferedValueAmount`


* `curl http://localhost:2444/statistic/bufferedValueProcent`


* `curl http://localhost:2444/statistic/isNetworkConnected`


* `curl http://localhost:2444/statistic/written`


* `curl http://localhost:2444/statistic/thrown`



## Конфиги
config.path=inputConfig.xml - конфиг с данными для чтения

spring.config.location=file:application.properties - конфиг с настройками баз данных

## Зависимости
java 21

