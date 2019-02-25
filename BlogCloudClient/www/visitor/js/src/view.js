requirejs(["bcstore"], function (bcstore) {
    window.bcstore = bcstore;
    initStore();
    window.theme = new BlogTheme();
    window.theme.loadTheme();
    function RootViewModel() {
        var self = this;
        new HashUtils(self);
        new MenuUtils(self);
        new PageUtils(self);
        new UserUtils(self);
        self.app = new SammyPage({view: self});
//        self.app.refresh();





    }
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView);
});
