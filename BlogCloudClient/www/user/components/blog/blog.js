(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            
            


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new BlogModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
