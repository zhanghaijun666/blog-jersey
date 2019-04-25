(function (global) {
    define(["knockout", "text!./custom-table.xhtml", "css!./custom-table.css"], function (ko, pageView) {
        function CustomTableModel(params, componentInfo) {
            var defaultValue = {
                itemList: [], // 数据源
                cardHead: [], // 数据头信息
                operateMenu: []     //数据操作，包括单选操作和多选操作 CustomMenuType枚举
            };
            var self = $.extend(this, defaultValue, params);
            self.itemSelect = new ItemSelect(self.itemList);






            self.operateMenuClick = function (data, event, item) {
                var item = item;
                if (data.menuType === global.CustomMenuType.MultipleSelection) {
                    item = self.itemSelect.getSelectedItem();
                }
                if (isFunction(data.clickFun)) {
                    data.clickFun(item);
                }
            };


            self.getMultipleOperateMenu = function () {
                return self.getOperateMenu(window.CustomMenuType.MultipleSelection);
            };
            self.isShowMultipleOperateMenu = function () {
                return self.getMultipleOperateMenu().length > 0;
            };
            self.getSingleOperateMenu = function () {
                return self.getOperateMenu(window.CustomMenuType.SingleSlection);
            };
            self.isShowSingleOperateMenu = function () {
                return self.getSingleOperateMenu().length > 0;
            };
            self.getOperateMenu = function (customMenuType) {
                var array = new Array();
                ko.utils.arrayForEach(self.operateMenu, function (menu) {
                    if (menu.menuType === customMenuType) {
                        array.push(menu);
                    }
                });
                return array;
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