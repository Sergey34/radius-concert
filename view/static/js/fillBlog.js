let blog_stored = [];

const blogTemplate = "{{#.}}\n" +
    "<div class=\"col-md-4 d-flex ftco-animate\">\n" +
    "    <div class=\"blog-entry align-self-stretch\">\n" +
    "        <a class=\"block-20 rounded\" href=\"/blog/{{id}}\"\n" +
    "           style=\"background-image: url({{mainImege}});\">\n" +
    "        </a>\n" +
    "        <div class=\"text mt-3 text-center\">\n" +
    "            <div class=\"meta mb-2\">\n" +
    "                <div><a href=\"#\">{{createdWhen}}</a></div>\n" +
    "                <div><a href=\"#\">{{author.login}}</a></div>\n" +
    "                <div><a class=\"meta-chat\" href=\"#\"><span class=\"icon-chat\"></span> 3</a></div>\n" +
    "            </div>\n" +
    "            <h3 class=\"heading\"><a href=\"#\">{{title}}</a></h3>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>" +
    "{{/.}}";

let blogLastLoaded;

function fillBlogs(from, to) {
    if (to > blog_stored.length) {
        to = blog_stored;
    }
    if (from >= to) {
        return
    }
    blogLastLoaded = to;
    let blogs = blog_stored.slice(from, to);
    for (let i = 0, order_md_last = 2; i < blogs.length; i++, order_md_last++) {
        if (order_md_last === 0 || order_md_last === 1) {
            blogs[i].order_md_last = true;
            blogs[i].class = 'right-arrow';
        } else {
            blogs[i].class = 'left-arrow';
        }
        if (order_md_last === 3) {
            order_md_last = -1;
        }
    }
    let eventsRender = Mustache.render(blogTemplate, blogs);
    $('#blog').append(eventsRender);
    contentWayPoint();
}
