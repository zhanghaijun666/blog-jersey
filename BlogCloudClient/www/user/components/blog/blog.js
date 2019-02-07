(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            var self = this;
            let fileUpload = document.getElementById("fileUpload");
            fileUpload.addEventListener("change", function (evt) {
//                fileUpload.files
                console.log("---------------");
                evt.preventDefault();
            });




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
