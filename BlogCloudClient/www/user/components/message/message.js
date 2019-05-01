(function (global) {
    define(["text!./message.xhtml", "css!./message.css"], function (pageView) {
        function MessageModel(params, componentInfo) {




        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MessageModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
