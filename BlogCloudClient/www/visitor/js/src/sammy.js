function SammyPage() {
    var sammy = Sammy(function () {
        this.get("/", function () {
            this.redirect("#home");
        });

        this.get("#home", function () {
        });

    }).run();
    return sammy;
}
window.SammyPage = SammyPage;