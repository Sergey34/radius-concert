const template = "{{#.}}\n" +
    "<div class=\"col-lg-6\">\n" +
    "    <div class=\"room-wrap d-md-flex ftco-animate fadeInUp\">\n" +
    "        <a class=\"img  {{#order_md_last}}order-md-last{{/order_md_last}}\" href=\"{{url}}\" style=\"background-image: url({{image}});\"></a>\n" +
    "        <div class=\"half {{class}} d-flex align-items-center\">\n" +
    "            <div class=\"text p-4 text-center\">\n" +

    "                <p class=\"mb-0\"><span class=\"price mr-1\">{{minPrice}}-{{maxPrice}}р</span>" +
    "                <p class=\"mb-0\"><span class=\"price mr-1\">{{city.name}}, {{startDate}}-{{endDate}}</span>" +
    "                <h3 class=\"mb-3\"><a href=\"rooms.html\">{{name}}</a></h3>\n" +
    "                <p class=\"pt-1\"><a class=\"btn-custom px-3 py-2 rounded\" href=\"{{url}}\">Подробнее ({{#city}}{{type}}{{/city}})\n" +
    "                    <span class=\"icon-long-arrow-right\"></span></a></p>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>" +
    "{{/.}}";

function fillEvents(events) {
    for (let i = 0, order_md_last = 2; i < events.length; i++, order_md_last++) {
        if (order_md_last === 0 || order_md_last === 1) {
            events[i].order_md_last = true;
            events[i].class = 'right-arrow';
        } else {
            events[i].class = 'left-arrow';
        }
        if (order_md_last === 3) {
            order_md_last = -1;
        }
    }
    let eventsRender = Mustache.render(template, events);
    $('#events').html(eventsRender);
    contentWayPoint();
}
