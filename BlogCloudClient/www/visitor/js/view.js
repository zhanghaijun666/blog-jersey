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


        self.getUser();
        self.app = new SammyPage({view: self});
    }

    ko.applyBindings(new RootViewModel());
});