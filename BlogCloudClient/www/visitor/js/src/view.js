requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    function RootViewModel() {
        var self = this;
        new UserUtils(self);
        new HashUtils(self);
        new PageUtils(self);
        self.app = new SammyPage({view: self});



        


        self.getUser();
    }
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView);
});
