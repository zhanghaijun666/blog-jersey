//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {

    ko.components.register('blog', {require: 'static/user/components/blog/blog.js'});
    ko.components.register('user-home', {require: 'static/user/components/user-home/home.js'});

})(this, ko);
