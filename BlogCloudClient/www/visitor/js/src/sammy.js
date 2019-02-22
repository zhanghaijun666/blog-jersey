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
                root.RootPage("login-page");
            }
        });
        this.get("#menu", function () {
            if (!root.isLogin()) {
                this.redirect("#login");
                return;
            }
            root.getMenu();
            root.RootPage("home-nav-tabs");
        });
        this.get("#messages", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
        this.get("#admin", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
    }).run();
    return sammy;
}
window.SammyPage = SammyPage;
