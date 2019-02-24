(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            var defaultValue = {
            };
            var self = $.extend(this, defaultValue, params);

            self.uploadFilesMenuItems = [
                new MenuTab("上传文件", {icon: "fa-upload"}),
                new MenuTab("上传文件夹", {icon: "fa-upload"})
            ];



//            let fileUpload = document.getElementById("fileUpload");
//            fileUpload.addEventListener("change", function (evt) {
////                fileUpload.files
//                console.log("---------------");
//                evt.preventDefault();
//            });




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
