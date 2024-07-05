## Запуск
команда:

`java -jar your-jar-file.jar  --config.path=<path-to-inputConfig.xml> --spring.config.location=file:/path/to/application.properties`

запуск с конфигами из этой папки:

`java -jar automatedAccountingSystem-1.0-SNAPSHOT.jar --config.path=inputConfig.xml --spring.config.location=file:application.properties`

## Конфиги
config.path=inputConfig.xml - конфиг с данными для чтения
spring.config.location=file:application.properties - конфиг с настройками баз данных

## Зависимости
java 21

