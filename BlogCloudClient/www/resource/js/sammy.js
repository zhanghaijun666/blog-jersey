function SammyPage(){
    var sammy = Sammy(function() {        
        this.get("#home", function(){
            window.rootView.page("page-home");
        });
        
        this.get("/", function() {
            this.redirect("#home");
        });
    }).run();
    
    return sammy;
}
window.SammyPage = SammyPage;