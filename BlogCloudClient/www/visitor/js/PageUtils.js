function PageUtils(root) {
    var self = root || this;
    self.RootPage = ko.observable();

    self.isSmallScreen = ko.computed(function () {
        return window.innerWidth < 992;
    }, self);
    self.isShowHomeFoot = ko.computed(function () {
        return self.isSmallScreen() && !self.isHash("login");
    }, self);

    self.blogNavigator = function () {
        let arr = [];
        arr.push(
                {text: '我的博客', icon: 'fa-book', click: self.changeHash.bind(null, "home"),
                    isSelect: ko.observable(true)
                }
        );
        arr.push(
                {text: '消息', icon: 'fa-comments-o', click: self.changeHash.bind(null, "messages"),
                    isSelect: ko.observable(false)
                }
        );
        arr.push(
                {text: '系统管理', icon: 'fa-coffee', click: self.changeHash.bind(null, "admin"),
                    isSelect: ko.observable(false)
                }
        );
        if (self.isSmallScreen()) {
            arr.push(
                    {text: '我的', icon: 'fa-coffee', click: self.changeHash.bind(null, "admin"),
                        isSelect: ko.computed(function () {
                            return self.isHash("admin");
                        })
                    }
            );
        }
        return arr;
    };
    self.PersonalCenter = [
        new DropdownMenu(l10n('user.userName'), {icon: 'fa-user', select: true}),
        new DropdownMenu(l10n('user.userNick'), {icon: 'fa-user'}),
        new DropdownMenu(l10n('user.userMail'), {icon: 'fa-envelope-o'}),
        new DropdownMenu(l10n('user.userPhone'), {icon: 'fa-phone'})
    ];


}
