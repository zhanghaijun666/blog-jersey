requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    function RootViewModel() {
        var self = this;
        window.RootView = self;
        self.RootPage = ko.observable();
        self.user = ko.observable();
        self.app = new SammyPage({view: self});

        self.getUser = function () {
            getRequest("/user", {accept: "application/x-protobuf"}, function (data) {
                var user = bcstore.User.decode(data);
                if (user.userId) {
                    self.user(new User(user));
                }
            });
        };
        self.isLogin = function () {
            return self.user() && self.user().userId && RootView.user().username();
        };


        self.getUser();
    }

    ko.applyBindings(new RootViewModel());
});