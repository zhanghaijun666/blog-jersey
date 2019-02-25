function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#login");
        });
        this.get("#login", function () {
            if (root.isLogin()) {
                this.redirect("#menu");
            } else {
                root.setRootTemplate('login-page');
            }
        });
        this.get(/\#menu(.*)/, function () {
            if (!root.isLogin()) {
                this.redirect("#login");
                return;
            }
            var params = this.params['splat'][0];
            root.getMenu();
            root.setRootTemplate('menu-nav-tabs-template');
        });
        this.get("#messages", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
        this.get("#admin", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
        this.around(function (callback) {
            root.getUser(callback);
        });
    }).run();
    return sammy;
}
window.SammyPage = SammyPage;
