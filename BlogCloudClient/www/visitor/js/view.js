requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    function RootViewModel() {
        var self = this;
        window.RootView = self;
        self.RootPage = ko.observable("login-page");
        self.user = ko.observable();
        self.app = new SammyPage({view: self});

    }

    ko.applyBindings(new RootViewModel());
});