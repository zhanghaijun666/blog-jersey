(function (global) {
    define(["knockout", "text!./menu-nav-tabs.xhtml", "css!./menu-nav-tabs.css"], function (ko, pageView) {
        function MenuNavTabs(params, componentInfo) {
            var defaultValue = {
                menuList: ko.observableArray([]),
                currentMenu: ko.observable(""),
                centreView: ""
            };
            var self = $.extend(this, defaultValue, params);
            self.isShowLargeLeftMenu = ko.observable(true);
            self.menuTabs = ko.observableArray([]);
            self.showMenu = ko.observable();

            self.getAllMenuTemplate = function () {
                let menuArray = new Array();
                if (ko.unwrap(self.menuList) && ko.unwrap(self.menuList).length > 0) {
                    ko.utils.arrayForEach(ko.unwrap(self.menuList), function (menu) {
                        if (menu.template) {
                            menuArray.push(new Menu(menu));
                        }
                    });
                }
                return menuArray;
            };


            ko.computed(function () {
                var isEmpty = !!(ko.unwrap(self.menuTabs).length === 0);
                if (ko.unwrap(self.menuList) && ko.unwrap(self.menuList).length > 0) {
                    ko.utils.arrayForEach(ko.unwrap(self.menuList), function (menu) {
                        if (isEmpty && ko.unwrap(menu.isDefaultShow) && ko.unwrap(menu.template)) {
                            self.menuTabs.push(new Menu(menu));
                        }
                        if (ko.unwrap(self.currentMenu) && ko.unwrap(self.currentMenu) === menu.name) {
                            self.showMenu(new Menu(menu));
                        }
                    });
                }
                if (ko.unwrap(self.showMenu)) {
                    var isExistShowMenu = false;
                    for (var i = 0; i < ko.unwrap(self.menuTabs).length; i++) {
                        if (ko.unwrap(self.showMenu().name) === ko.unwrap(self.menuTabs()[i].name)) {
                            self.showMenu(self.menuTabs()[i]);
                            isExistShowMenu = true;
                            break;
                        }
                    }
                    if (!isExistShowMenu) {
                        self.menuTabs.push(new Menu(ko.unwrap(self.showMenu())));
                    }
                } else if (ko.unwrap(self.menuTabs).length > 0) {
                    self.showMenu(ko.unwrap(self.menuTabs)[ko.unwrap(self.menuTabs).length - 1]);
                } else {
                    self.showMenu(null);
                }
                setTimeout(function () {
                    if (self.showMenu()) {
                        $(ko.unwrap(self.showMenu().element)).find('a').tab('show');
                    }
                }, 50);
            });

            self.getLsatMenuTab = function () {
                if (ko.unwrap(self.menuTabs) && ko.unwrap(self.menuTabs).length > 0) {
                    return ko.unwrap(self.menuTabs)[ko.unwrap(self.menuTabs).length - 1];
                }
                return {};
            };
            self.romveMenuTabs = function (data, event) {
                self.menuTabs.remove(function (menu) {
                    return ko.unwrap(menu.name) === ko.unwrap(data.name);
                });
            };
            self.isShowDeletable = function (data) {
                return data.isDeletable && data.name !== self.showMenu().name;
            };


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MenuNavTabs(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
