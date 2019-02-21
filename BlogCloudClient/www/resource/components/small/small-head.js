(function () {
    define(["text!./small-head.xhtml", "css!./small-head.css"], function (pageView) {
        function SmallMenuModel(params, componentInfo) {
            var defaultValue = {
                leftIcon: 'fa-bars',
                leftTemplateName: "defaultleftTemplateData",
                leftTemplateData: {},
                rightIcon: 'fa-plus',
                rightTemplateName: "defaultleftTemplateData",
                rightTemplateData: {},
                centerIcon: 'fa-book',
                centerText: '我的博客'
            };
            var self = $.extend(this, defaultValue, params);
            self.isShowLeftTemplate = ko.observable(false);
            self.isShowRightTemplate = ko.observable(false);
            self.leftClick = function () {
                showDialog($.extend({
                    isHideHeader: true,
                    isHideFoot: true,
                    isCloseDialog: false,
                    bodyTemplate: self.leftTemplateName,
                    dialogClass: "left-template"
                }, self.leftTemplateData));
            };


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new SmallMenuModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})();
