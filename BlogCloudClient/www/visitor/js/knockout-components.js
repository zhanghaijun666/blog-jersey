//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    ko.components.register('login-page', {require: 'static/visitor/components/login/login.js'});
    ko.components.register('home-nav-tabs', {require: 'static/visitor/components/home-nav-tabs/home-nav-tabs.js'});
    ko.components.register('twaver-spring', {require: 'static/visitor/components/twaver/twaver-spring.js'});

})(this, ko);
