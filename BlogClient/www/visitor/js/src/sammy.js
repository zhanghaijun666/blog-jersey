/* global FileUrl */

function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get(/\#login(.*)/, function () {
            root.setRootTemplate('login-page');
        });
        this.get(/\#file(.*)/, function () {
            var path = this.params['splat'][0];
            if (!FileUrl.isValidFilePath(path)) {
                path = "default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/";
            }
            root.currentFilePath(path);
            root.setRootTemplate('blog-file');
        });
        this.get(/\#menu(.*)/, function () {
            var menu = this.params['splat'][0];
            root.currentMenu(menu.substring(1));
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
            this.redirect("#file");
        });
        this.around(function (callback) {
            root.getUser(function () {
                if (root.isLogin() && RootView.isHash("login")) {
                    this.redirect("#file");
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
