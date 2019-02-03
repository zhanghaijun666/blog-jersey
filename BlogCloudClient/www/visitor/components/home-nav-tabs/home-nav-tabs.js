(function (global) {
    define(["knockout", "text!./home-nav-tabs.xhtml", "css!./home-nav-tabs.css"], function (ko, pageView) {
        function homeNavTabs(params, componentInfo) {
            var defaultMenuId = 0;
            var self = this;
            if (params.ref) {
                params.ref(self);
            }
            self.leftMenu = ko.observableArray([]);
            self.menuTabs = ko.observableArray([]);

            function init() {
                getRequest("/menu/hash/" + RootView.getHome(), {accept: "application/x-protobuf"}, function (data) {
                    var menuList = bcstore.MenuList.decode(data);
                    var menuMap = {};
                    ko.utils.arrayForEach(menuList.items, function (menu) {
                        menu.name = l10n('menu.' + menu.name);
                        if (menuMap[menu.parentId]) {
                            menuMap[menu.parentId].push(menu);
                        } else {
                            menuMap[menu.parentId] = [menu];
                        }
                        if (menu.defaultShow && menu.template) {
                            self.menuTabs.push(menu);
                        }
                    });
                    if (menuMap && menuMap[defaultMenuId]) {
                        ko.utils.arrayForEach(menuMap[defaultMenuId], function (menu) {
                            menu.items = menuMap[menu.menuId];
                            self.leftMenu.push(menu);
                        });
                    }
                });
            }
            self.addMenuTabs = function (menu, event) {
                if ($("#tab" + menu.menuId).length > 0) {
                    $("#tab" + menu.menuId).tab("show");
                } else {
                    self.menuTabs.push(menu);
                    $("#tab" + menu.menuId).tab("show");
                }
            };
            self.romveMenuTabs = function (menu, event) {
                self.menuTabs.remove(menu);
            };
            self.contentAfterRender = function (element) {
                $(element).parents(".centre_tabs").find('.nav-tabs li a:last').tab("show");
            };

            init();
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new homeNavTabs(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
