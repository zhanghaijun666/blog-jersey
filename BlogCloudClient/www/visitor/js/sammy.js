function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#home");
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
        this.get("#blog", function () {
            root.RootPage("login-page");
        });


    }).run();
    return sammy;
}
window.SammyPage = SammyPage;