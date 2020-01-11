# Что это?
Агрегатор концертов (возможно и других мероприятий)

# Зачем это?
ты хочешь на концерт, готов потратить на это мероприятие 2 дня,
или есть расстояние, которое ты готов преодолеть ради концерта.
Агрегатор покажет концерты в заданном радиусе (возможно рассчитает стоимость всего мероприятия)

# Для кого это?
для меня

# Какой план?
И снова планы поменялись (история в истории коммитов гита)
- есть статстичный список городов с координатами (довольно большой, возможно все населенные пункты России)
- парсится агрегатор концертов (и буду тырить каждую ночь (надеюсь найти с публичным АПИ иначе придется парсить))
- пользователь выбирает свой город/поселок/...
- если город определен то выводятся концерты в заданном радиусе
- если нет - получаем список населенных пунктов
- пользователь выбрает из списка свой населенный пункт и ему отображаются ивенты рядом
- при выборе более детальной информации по концерту будет выполнен пересчет расстояния (яндекс карты???)

# Что использовать?
да ничего нового
- spring-потому что удобно и знаю
- монго-потому что лень возиться с реляционной моделью и есть реактивный драйвер
- тимелиаф-даст шанс не сделать совсем ужасный фронтенд
- Котлин-java-скучно, Groovy-нет особой необходимости в динамизме, Scala-я ее так и не выучил - > больше ничего и не осталось
- jdk11-ну вот так

# А будут микросервисы?
будут, все будет, весь монолит в микросервисы, но потом. Прототип будет монолитом

есть большие планы потрогать спринговый клауд, с реактивным подходом и без нетфликса.

# что готово?
* парсится yandex afisha
* сохраняется в монгу
* есть фронт с выбором города, отображением ивентов
* ивенты можно найти в заданном радиусе
* есть авторизация через google и обычная - логин пароль base64
![title](https://github.com/Sergey34/radius-concert/blob/master/img/main.png)
![title](https://github.com/Sergey34/radius-concert/blob/master/img/events.png)


# ближайшие планы
* добавить тсраницу ивента, на которой будет потом реализовано детальное планирование
* добавить комментарии к ивенту
* добавить страницу создания блога/обзора/статьи (пока назвается блог, хотя это больше похоже на статьи) 

# далекие планы
* добавить планирования поездки на страницу ивента
