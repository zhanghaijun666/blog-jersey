function PageUtils(root) {
    var self = root || this;
    self.rootPageViewModel = ko.observable();

    self.isSmallScreen = ko.computed(function () {
        return window.innerWidth < 992;
    }, self);
    self.isShowHomeFoot = ko.computed(function () {
        return self.isSmallScreen() && !self.isHash("login");
    }, self);

    self.blogNavigator = function () {
        let arr = [];
        arr.push(new MenuTab('我的博客', {icon: 'fa-book', isActive: true, clickFun: self.changeHash.bind(null, "menu")}));
        arr.push(new MenuTab('消息', {icon: 'fa-comments-o', isActive: false, clickFun: self.changeHash.bind(null, "messages")}));
        arr.push(new MenuTab('系统管理', {icon: 'fa-coffee', isActive: false, clickFun: self.changeHash.bind(null, "admin")}));
        if (self.isSmallScreen()) {
            arr.push(new MenuTab('我的', {icon: 'fa-home', isActive: false, clickFun: self.changeHash.bind(null, "menu")}));
        }
        return arr;
    };
    self.getPersonalCenter = function () {
        let menuArr = new Array();
        menuArr.push(new MenuTab(l10n('user.signOut'), {icon: 'fa-sign-out', clickFun: self.logout}));
        return menuArr;
    };


}
