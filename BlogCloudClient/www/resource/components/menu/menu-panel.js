(function (global) {
    define(["knockout", "text!./menu-panel.xhtml", "css!./menu-panel.css"], function (ko, pageView) {
        function MenuPanel(params, componentInfo) {
            var defaultMenuId = 0;
            var defaultValue = {
                menuList: ko.observableArray([])
            };
            var self = $.extend(this, defaultValue, params);

            self.getMenuArray = function () {
                let menuArray = new Array();
                if (ko.unwrap(self.menuList) && ko.unwrap(self.menuList).length > 0) {
                    let menuMap = {};
                    ko.utils.arrayForEach(ko.unwrap(self.menuList), function (menu) {
                        if (menuMap[ko.unwrap(menu.parentId)]) {
                            menuMap[ko.unwrap(menu.parentId)].push(menu);
                        } else {
                            menuMap[ko.unwrap(menu.parentId)] = [menu];
                        }
                    });
                    if (menuMap && menuMap[defaultMenuId]) {
                        ko.utils.arrayForEach(menuMap[defaultMenuId], function (menu) {
                            menu.items = menuMap[menu.menuId];
                            menuArray.push(menu);
                        });
                    }
                }
                return menuArray;
            };
            self.switchMenu = function (data, event) {
                if (RootView.isHash("menu") && ko.unwrap(data.name)) {
                    RootView.changeHash("#menu/" + ko.unwrap(data.name));
                }
            };
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MenuPanel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
