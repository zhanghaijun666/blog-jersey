function PageUtils(root) {
    var self = root || this;
    self.rootTemplate = ko.observable();

    self.setRootTemplate = function (template) {
        if (template && template !== self.rootTemplate()) {
            self.rootTemplate(template);
        }
    };

    self.isSmallScreen = ko.computed(function () {
        return window.innerWidth < 992;
    }, self);
    self.isShowHomeFoot = ko.computed(function () {
        return self.isSmallScreen() && !self.isHash("login");
    }, self);

    self.blogNavigator = function () {
        let arr = [];
        arr.push(new MenuTab('我的博客', {icon: 'fa-book', isActive: true, clickFun: self.changeHash.bind(null, "file")}));
        arr.push(new MenuTab('Demo', {icon: 'fa-book', isActive: false, clickFun: self.changeHash.bind(null, "menu")}));
        arr.push(new MenuTab('消息', {icon: 'fa-comments-o', isActive: false, clickFun: self.changeHash.bind(null, "message")}));
        arr.push(new MenuTab('系统管理', {icon: 'fa-coffee', isActive: false, clickFun: self.changeHash.bind(null, "admin")}));
        if (self.isSmallScreen()) {
            arr.push(new MenuTab('我的', {icon: 'fa-home', isActive: false, clickFun: self.changeHash.bind(null, "menu")}));
        }
        return arr;
    };
    self.getPersonalCenter = function () {
        let menuArr = new Array();
        menuArr.push(new MenuTab("", {icon: 'fa-magic', title: l10n('product.theme')}));
        menuArr.push(new MenuTab("", {icon: 'fa-language', title: l10n('product.switLang'), clickFun: window.switchLocalLang}));
        menuArr.push(new MenuTab("", {icon: 'fa-question-circle', title: l10n('product.help')}));
        menuArr.push({template: 'user-center-template', data: {
                headText: self.user().getFullName(),
                isShowSelect: false,
                dropdownCss: "center-dropdown",
                menuItems: [
                    new MenuTab(l10n('product.switLang'), {icon: 'fa-language', clickFun: window.switchLocalLang}),
                    new MenuTab(l10n('user.signOut'), {icon: 'fa-sign-out', clickFun: self.logout})
                ]
            }});
        return menuArr;
    };


}
