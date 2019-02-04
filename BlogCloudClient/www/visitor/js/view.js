requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    function RootViewModel() {
        var self = this;
        window.RootView = self;
        self.RootPage = ko.observable();
        self.user = ko.observable();
        self.getUser = function () {
            getRequest("/user", {accept: "application/x-protobuf"}, function (data) {
                var user = bcstore.User.decode(data);
                if (user.userId) {
                    self.user(new User(user));
                    self.app.refresh();
                }
            });
        };
        self.isLogin = function () {
            return self.user() && self.user().userId && RootView.user().username();
        };
        self.getHash = function (localhash) {
            var regExp = /^#([^/?]+)/;
            var hash = localhash || window.location.hash;
            if (regExp.test(hash)) {
                return regExp.exec(hash)[1];
            } else {
                return "";
            }
        };
        self.changeHash = function (hash) {
            if (!hash) {
                return;
            }
            if (hash.slice(0, 1) !== "#") {
                hash = "#" + hash;
            }
            if ("#" + self.getHash() === hash) {
                return;
            }
            window.location.hash = hash;
        };
        self.blogNavigator = [
            {text: '我的博客', icon: 'fa-book', click: self.changeHash("home"), isSelect: function () {}},
            {text: '消息', icon: 'fa-comments-o', click: function () {}, isSelect: function () {}},
            {text: '系统管理', icon: 'fa-coffee', click: function () {}, isSelect: function () {}}
        ];


        self.getUser();
        self.app = new SammyPage({view: self});
    }

    ko.applyBindings(new RootViewModel());
});
