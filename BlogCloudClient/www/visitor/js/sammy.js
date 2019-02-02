function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#home");
        });

        this.get("#home", function () {
            root.RootPage("login-page");
        });
        this.get("#login", function () {
            root.RootPage("login-page");
        });

    }).run();
    return sammy;
}
window.SammyPage = SammyPage;