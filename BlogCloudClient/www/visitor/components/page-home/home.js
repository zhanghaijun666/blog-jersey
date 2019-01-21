(function (global) {
    define(["knockout", "text!./home.xhtml"], function (ko, pageView) {
        function PageHomeModel(params, componentInfo) {
            var self = this;
            self.navTab = ko.observable({});    
            self.tabs = ko.observableArray([
                new MenuTab("home", "我的首页"),
                new MenuTab("protein", "蛋白列表", {component: "page-protein"}),
                new MenuTab("user", "成员信息"),
                new MenuTab("topology", "拓扑信息")
            ]);
            self.tabs()[0].isActive(true);
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new PageHomeModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
