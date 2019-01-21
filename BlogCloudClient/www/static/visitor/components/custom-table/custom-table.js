(function (global) {
    define(["knockout", "text!./custom-table.xhtml", "css!./custom-table.css"], function (ko, pageView) {
        function CustomTableModel(params, componentInfo) {
            var defaultValue = {
                itemList: [],
                operateMenu: []
            };
            var self = $.extend(this, defaultValue, params);
            self.itemSelect = new ItemSelect(self.itemList);




            self.getOperateMenu = function (customMenuType) {
                var array = new Array();
                ko.utils.arrayForEach(self.operateMenu, function (menu) {
                    if (menu.menuType === customMenuType) {
                        array.push(menu);
                    }
                });
                return array;
            };

            self.operateMenuClick = function (data, event, item) {
                var item = item;
                if (data.menuType === global.CustomMenuType.MultipleSelection) {
                    item = self.itemSelect.getSelectedItem();
                }
                if (isFunction(data.clickFun)) {
                    data.clickFun(item);
                }
            };



        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new CustomTableModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);