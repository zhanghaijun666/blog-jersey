requirejs(["bcs", "protobufjs/light"], function (bcs, protobuf) {
    window.bcs = bcs;
    function RootView() {
        var self = this;
        window.rootView = self;
        self.page = ko.observable("page-home");
        self.isLogin = ko.observable(false);
        self.app = new SammyPage();

        
    }

    ko.applyBindings(new RootView());
});