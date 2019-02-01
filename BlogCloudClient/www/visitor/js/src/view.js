requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    function RootViewModel() {
        var self = this;
        window.RootView = self;
        self.app = new SammyPage();
        self.LoginJs = LoginJs();
        
        self.user = ko.observable(new User({}, true));
        self.LoginJs.getUser();
        
    }

    ko.applyBindings(new RootViewModel());
});