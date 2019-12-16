# Что это?
агрегатор концертов (и возможно дургих мероприятий)

# Зачем это?
ты хочешь на концерт, готов потратить на это мероприятие 2 дня, 
или есть расстояние, которое ты готов преодолеть ради конценра.
Агрегатор покажет концерты в заданном радиусе (возможно рассчитает стоимость всего мероприятия)

# Для кого это?
для меня

# Какой план?
пока план такой, но статистика говорит, что все будет совсем иначе:
- получить список говродов с других агрегаторов
- получить для них координаты центров (возьму апи яндекс карт и понадеюсь что меня не забанят от нагрузки)
- стырю с других агрегаторов 😈 концерты, и буду тырить каждую ночь 😈😈😈 (надеюсь найти с публичным API иначе придется парсить)
- выбираем свой город из ощего списка (взять с яндекса?) имеем координаты города и радиус, выбираем города попадающие в окружность выводим инфу
- при выборе более детальное информации по концерту будет выполнен пересчет расстояния (яндекс карты) 

# Что использовать?
да ничего нового
- spring - потому что удобно и знаю
- mongo - потому что лень возиться с реляционной моделью и есть реактивный драйвер
- thymeleaf - даст шанс не сделать совсем ужасный фронтенд
- kotlin - java - скучно, groovy - нет особой необходимсоти в динамизме, scala - я ее так и не выучил -> больше ничего и не осталось
- jdk11 - ну вот так

# А микросервисы будут?
будут, все будет, но потом. прототип будет монолитом

есть большие планы потрогать спринговый клауд, с реактивным подходом и без нетфликса.

# что готово?
* парсится kassir.ru
* сохраняется в монгу
* есть рест чтобы достать все данные, и чтобы заапдейтить данные.
* готов запрос на получение ивентов в заданном радиусе

# ближайшие планы
* прикрутить фронт (надеюсь несильно страшный)
* сделать рест для ивентов в заданном радиусе
