(function (global) {
    define(["knockout", "text!./menu-nav-tabs.xhtml", "css!./menu-nav-tabs.css"], function (ko, pageView) {
        function MenuNavTabs(params, componentInfo) {
            var defaultValue = {
                menuList: ko.observableArray([]),
                currentMenu: ko.observable("")
            };
            var self = $.extend(this, defaultValue, params);
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
            self.getMenuTab = function () {
                let isAddDefaultMenuTab = ko.unwrap(self.menuTabs).length === 0;
                let hashMenuTab = null;
                if (ko.unwrap(self.menuList) && ko.unwrap(self.menuList).length > 0) {
                    ko.utils.arrayForEach(ko.unwrap(self.menuList), function (menu) {
                        if (isAddDefaultMenuTab && ko.unwrap(menu.isDefaultShow) && ko.unwrap(menu.template)) {
                            self.menuTabs.push(new Menu(menu));
                        }
                        if (ko.unwrap(self.currentMenu) && ko.unwrap(self.currentMenu) === menu.name) {
                            hashMenuTab = menu;
                        }
                    });
                    if (hashMenuTab) {
                        let isMenuExist = false;
                        for (let i = 0; i < ko.unwrap(self.menuTabs).length; i++) {
                            if (ko.unwrap(self.menuTabs)[i].menuId === hashMenuTab.menuId) {
                                isMenuExist = true;
                                break;
                            }
                        }
                        if (!isMenuExist) {
                            self.menuTabs.push(hashMenuTab);
                        }
                    }
                }
                return ko.unwrap(self.menuTabs);
            };
            self.getLsatMenuTab = function () {
                let menuTabArray = self.getMenuTab();
                if (menuTabArray && menuTabArray.length > 0) {
                    return menuTabArray[menuTabArray.length - 1];
                }
                return {};
            };





//            function init() {
//                getRequest("/menu/hash/" + RootView.getHash(), {accept: "application/x-protobuf"}, function (data) {
//                    var menuList = bcstore.MenuList.decode(data);
//                    var menuMap = {};
//                    ko.utils.arrayForEach(menuList.items, function (menu) {
//                        menu.name = l10n('menu.' + menu.name);
//                        if (menuMap[menu.parentId]) {
//                            menuMap[menu.parentId].push(menu);
//                        } else {
//                            menuMap[menu.parentId] = [menu];
//                        }
//                        if (menu.isDefaultShow && menu.template) {
//                            self.menuTabs.push(menu);
//                        }
//                    });
//                    if (menuMap && menuMap[defaultMenuId]) {
//                        ko.utils.arrayForEach(menuMap[defaultMenuId], function (menu) {
//                            menu.items = menuMap[menu.menuId];
//                            self.leftMenu.push(menu);
//                        });
//                    }
//                });
//            }
//            self.addMenuTabs = function (menu, event) {
//                if ($("#tab" + menu.menuId).length > 0) {
//                    $("#tab" + menu.menuId).tab("show");
//                } else {
//                    self.menuTabs.push(menu);
//                    $("#tab" + menu.menuId).tab("show");
//                }
//            };
//            self.romveMenuTabs = function (menu, event) {
//                self.menuTabs.remove(menu);
//            };
//            self.contentAfterRender = function (element) {
//                $(element).parents(".centre_tabs").find('.nav-tabs li a:last').tab("show");
//            };
//
//            init();
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
