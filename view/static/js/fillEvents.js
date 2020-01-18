let events_stored = [];

const template = "{{#.}}\n" +
    "<div class=\"col-lg-6\">\n" +
    "    <div class=\"room-wrap d-md-flex ftco-animate fadeInUp\">\n" +
    "        <a class=\"img  {{#order_md_last}}order-md-last{{/order_md_last}}\" href=\"/event/{{id}}\" style=\"background-image: url({{event.image.microdata.url}});\"></a>\n" +
    "        <div class=\"half {{class}} d-flex align-items-center\">\n" +
    "            <div class=\"text p-4 text-center\">\n" +
    "                <p class=\"mb-0\">{{#event.tickets}}<span class=\"price mr-1\">{{price.min}}-{{price.max}}р</span>{{/event.tickets}}" +
    "                <p class=\"mb-0\"><span class=\"price mr-1\">{{city.name}}, {{scheduleInfo.regularity.singleShowtime}}</span>" +
    "                <h3 class=\"mb-3\"><a href=\"/event/{{id}}\">{{event.title}}</a></h3>\n" +
    "                <p class=\"pt-1\"><a class=\"btn-custom px-3 py-2 rounded\" href=\"/event/{{id}}\">Подробнее" +
    "                    <span class=\"icon-long-arrow-right\"></span></a></p>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>" +
    "{{/.}}";

let lastLoaded;

const similarEventsTemplate = '{{#.}}<div class="block-21 mb-4 d-flex">\n' +
    '            <a class="blog-img mr-4" style="background-image: url({{event.image.microdata.url}});"></a>\n' +
    '            <div class="text">\n' +
    '                <h3 class="heading"><a href="/event/{{id}}">{{event.title}}</a></h3>\n' +
    '                <div class="meta">\n' +
    '                    <div><a href="/event/{{id}}"><span class="icon-calendar"></span> {{scheduleInfo.regularity.singleShowtime}}</a></div>\n' +
    '                    <div><a href="/events/{{city.name}}"><span class="icon-location_city"></span> {{city.name}}</a></div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>{{/.}}';

function fillSimilarEvents(events) {
    let eventsRender = Mustache.render(similarEventsTemplate, events);
    $('#similar_events').html(eventsRender);
}

function fillEvents(from, to) {
    if (to > events_stored.length) {
        to = events_stored.length;
    }
    if (from >= to) {
        return
    }
    lastLoaded = to;
    let events = events_stored.slice(from, to);
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
    $('#events').append(eventsRender);
    contentWayPoint();
}
