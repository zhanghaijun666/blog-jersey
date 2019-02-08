(function (global) {
    define(["text!./menu.xhtml", "css!./menu.css"], function (pageView) {
        function MenuModel(params, componentInfo) {
            var self = this;
            self.menus = params.menus || [];
            
            






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
})(this);
