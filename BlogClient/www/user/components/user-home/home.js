(function (global) {
    define(["text!./home.xhtml", "css!./home.css"], function (pageView) {
        function UserHomeModel(params, componentInfo) {
            
            


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new UserHomeModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
