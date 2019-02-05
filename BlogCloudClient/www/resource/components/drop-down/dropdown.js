(function (global) {
    define(["knockout", "text!./dropdown.xhtml", "css!./dropdown.css"], function (ko, pageView) {
        function DropdownModel(params, componentInfo) {
            var defaultValue = {
                menuItems: [],
                headText: '请选择',
                menuClick: function () {},
                dropdownCss: '',
                isShowSelect: true,
                isSingleSelected: true
            };
            var self = $.extend(this, defaultValue, params);


            self.menuMinWidth = function () {
                return $(componentInfo.element).find("[data-toggle='dropdown']").outerWidth() + "px";
            };

            self.dropdownMenuClick = function (menu, event) {
                console.log(event);
                if (!ko.isObservable(menu.select)) {
                    event.stopPropagation();
                    return;
                }
                if (self.isSingleSelected) {
                    ko.utils.arrayForEach(ko.unwrap(self.menuItems), function (item) {
                        item.select(item == menu);
                    });
                } else {
                    event.stopPropagation();
                    menu.select(!ko.unwrap(menu.select));
                }
                if (menu.clickFun) {
                    menu.clickFun();
                }
            };
            self.getHeadText = function () {
                var text = "";
                if (self.isShowSelect) {
                    ko.utils.arrayForEach(self.getSelected(), function (item) {
                        text = text + ko.unwrap(item.name) + ",";
                    });
                    text = text.substring(0, text.length - 1)
                }
                if (text === "") {
                    text = ko.unwrap(self.headText);
                }
                return text;
            };
            self.getSelected = function () {
                var itemArray = new Array();
                ko.utils.arrayForEach(ko.unwrap(self.menuItems), function (item) {
                    if (ko.unwrap(item.select)) {
                        itemArray.push(item);
                    }
                });
                return itemArray;
            };


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new DropdownModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
