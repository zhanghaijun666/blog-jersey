function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#login");
        });
        this.get("#login", function () {
            if (root.isLogin()) {
                this.redirect("#home");
            } else {
                root.RootPage("login-page");
            }
        });
        this.get("#home", function () {
            if (root.isLogin()) {
                root.RootPage("home-nav-tabs");
            } else {
                this.redirect("#login");
            }
        });
        this.get("#messages", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
//            root.RootPage("login-page");
            this.redirect("#login");
        });
        this.get("#menu", function () {
            toastShowMsg("暂未开发，尽情期待！！！");
//            root.RootPage("login-page");
            this.redirect("#login");
        });


    }).run();
    return sammy;
}
window.SammyPage = SammyPage;
