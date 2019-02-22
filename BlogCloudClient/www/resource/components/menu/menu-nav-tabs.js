(function (global) {
    define(["knockout", "text!./menu-nav-tabs.xhtml", "css!./menu-nav-tabs.css"], function (ko, pageView) {
        function MenuNavTabs(params, componentInfo) {
            var defaultValue = {
                menuList: ko.observableArray([])
            };
            var self = $.extend(this, defaultValue, params);
            if (params.ref) {
                params.ref(self);
            }
            self.menuList = params.menuList || ko.observableArray([]);

            self.defaultMenuTabs = ko.observableArray([]);

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
                let isAddDefaultMenuTab = ko.unwrap(self.defaultMenuTabs).length === 0;
                let hashMenuTabName = location.hash.indexOf("/") > 0 ? location.hash.substring(location.hash.indexOf("/") + 1) : "";
                let hashMenuTab = null;
                if (ko.unwrap(self.menuList) && ko.unwrap(self.menuList).length > 0) {
                    ko.utils.arrayForEach(ko.unwrap(self.menuList), function (menu) {
                        if (isAddDefaultMenuTab && ko.unwrap(menu.isDefaultShow) && ko.unwrap(menu.template)) {
                            self.defaultMenuTabs.push(menu);
                        }
                        if (hashMenuTabName && menu.name === hashMenuTabName) {
                            hashMenuTab = menu;
                        }
                    });
                    if (hashMenuTab) {
                        let isMenuExist = false;
                        for (let i = 0; i < ko.unwrap(self.defaultMenuTabs).length; i++) {
                            if (ko.unwrap(self.defaultMenuTabs)[i].menuId === hashMenuTab.menuId) {
                                ko.unwrap(self.defaultMenuTabs)[i].isActive(true);
                                isMenuExist = true;
                                break;
                            }
                        }
                        if (!isMenuExist) {
                            hashMenuTab.isActive(true);
                            self.defaultMenuTabs.push(hashMenuTab);
                        }
                    }
                } else if (ko.unwrap(self.defaultMenuTabs).length > 0) {
                    ko.unwrap(self.defaultMenuTabs)[ko.unwrap(self.defaultMenuTabs).length - 1].isActive(true);
                }
                return ko.unwrap(self.defaultMenuTabs);
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
