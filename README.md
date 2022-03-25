# О проекте

Выпускной проект на курсе "Разработчик на Spring Framework otus.ru" - Личный кабинет клиентов

В ЛК клиент может увидеть свой баланс, начисления, оплаты, оставлять заявки, видеть решения по ним.
Предполагается, что сторонняя система управляет личным кабинетом через API. 
Вариантом использования личного кабинета может выступать набор скриптов, проводящих расчеты (начисления 
и оплаты), где ЛК выступает как хранилище данных и интерфейс для пользователей.

Подробнее о проекте в [презентации](https://docs.google.com/presentation/d/1tP2gbR3EyUPWD4xpa2CZtd_OGXcJbHaZ1lr32rpKonQ/edit?usp=sharing).


## Использованные технологии

* Spring Boot
* Data JPA + Hibernate + Liquibase + Postgres/H2
* Rest controller для API и UI
* UI: Ajax/SPA, Vue.js и jQuery
* Spring Security: form-based для UI и Basic для API
* Spring Integration для уведомлений клиентов по почте (Spring Mail) и смс
* Config server, Eureka и Feign
* Spring Shell в примерах работы с API
* Docker


