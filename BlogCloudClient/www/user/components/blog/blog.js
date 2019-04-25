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
            self.blogFileLsit = ko.observableArray([]);
            self.blogOperateMenu = [
//                new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.SingleSlection}),
//                new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.MultipleSelection})
            ];

            self.getBlogFile = function () {
                getRequest("/file/get/default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/", {accept: "application/x-protobuf"}, function (data) {
                    var fileList = bcstore.FileItemList.decode(data);
                    self.blogFileLsit(fileList.item);
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
