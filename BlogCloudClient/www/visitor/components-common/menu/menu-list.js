(function () {
    define(["text!./menu-list.xhtml", "css!./menu-list.css"], function (pageView) {
        function MenuModel(params, componentInfo) {
            var defaultParams = {
                menuClass: "default-menu",
                menus: [], //new MenuTab() / {template: '', data: {}}
                isCenter: false
            };
            var self = this;
            self = $.extend(this, defaultParams, params);
            self.menuWidth = "";
            if (!!ko.unwrap(self.isCenter) && ko.unwrap(self.menus) && ko.unwrap(self.menus).length > 0) {
                self.menuWidth = (1 / (ko.unwrap(self.menus).length)) * 100 + "%";
            }


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MenuModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})();
