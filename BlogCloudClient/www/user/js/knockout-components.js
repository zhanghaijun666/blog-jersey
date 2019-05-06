//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {

    ko.components.register('blog-file', {require: 'static/user/components/blog/blog.js'});
    ko.components.register('user-home', {require: 'static/user/components/user-home/home.js'});
    ko.components.register('message', {require: 'static/user/components/message/message.js'});

})(this, ko);
