(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            var defaultValue = {
            };
            var self = $.extend(this, defaultValue, params);

            self.uploadFilesMenuItems = function () {
                return [
                    new MenuTab("上传文件", {icon: "fa-upload", clickFun: global.simulateClick.bind(null, document.getElementById('fileToUpload'))}),
                    new MenuTab("上传文件夹", {icon: "fa-upload"})
                ];
            };
            self.getBlogFile = function () {
                getRequest("/file/get/default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/", {accept: "application/x-protobuf"}, function (data) {
                    var fileList = bcstore.FileItemList.decode(data);
                    console.log(fileList);
                });
            };



            self.getBlogFile();

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
