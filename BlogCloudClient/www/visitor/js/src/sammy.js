function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#login");
        });
        this.get(/\#login(.*)/, function () {
            root.setRootTemplate('login-page');
        });
        this.get(/\#menu(.*)/, function () {
            var params = this.params['splat'][0];
            root.currentMenu(params.substring(1));
            root.getMenu();
            root.setRootTemplate('menu-nav-tabs-template');
        });
        this.get(/\#message(.*)/, function () {
            root.setRootTemplate('message');
        });
        this.get(/\#admin(.*)/, function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
        this.get(/.+/, function () {
            this.redirect("#menu");
        });
        this.around(function (callback) {
            root.getUser(function () {
                if (root.isLogin() && RootView.isHash("login")) {
                    this.redirect("#menu");
                    return;
                } else if (!root.isLogin() && !RootView.isHash("login")) {
                    this.redirect("#login");
                } else {
                    callback();
                }
            }.bind(this));
        });
    }).run();
    return sammy;
}
window.SammyPage = SammyPage;
