(function (global) {
    define(["text!./university.xhtml", "css!./university.css"], function (pageView) {
        function UniversityModel(params, componentInfo) {
            
            


        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new UniversityModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
