requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    function RootViewModel() {
        var self = this;
        window.RootView = self;
        self.app = new SammyPage();
        self.LoginJs = LoginJs();
        
        
    }

    ko.applyBindings(new RootViewModel());
});