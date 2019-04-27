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
            self.getBlogOperateMenu = function () {
                return [
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: global.CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.rename'), {icon: 'fa-edit', clickFun: self.confirmRenameFile, menuType: global.CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: global.CustomMenuType.MultipleSelection})
                ];
            };

            self.getBlogFile = function () {
                getRequest("/file/get/default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/", {accept: "application/x-protobuf"}, function (data) {
                    var fileList = bcstore.FileItemList.decode(data);
                    if (fileList.item && fileList.item instanceof Array) {
                        for (var i = 0; i < fileList.item.length; i++) {
                            self.blogFileLsit.push(new FileItem(fileList.item[i], true));
                        }
                    }
                });
            };
            self.deleteFile = function (fileIemList) {
                showDialog({
                    header: l10n("operate.tips"),
                    bodyTemplate: "template-small-dialogMsg",
                    tipsMsg: l10n('operate.confirmDelete'),
                    dialogClass: "modal-sm",
                    success: function () {
                        fileIemList = fileIemList instanceof Array ? fileIemList : [fileIemList];
                        var req = bcstore.FileItemList.create();
                        for (var i = 0; i < fileIemList.length; i++) {
                            req.item.push(fileIemList[i]);
                        }
                        getRequest("/file/deldete", {method: "POST", type: "application/x-protobuf", accept: "application/x-protobuf", data: bcstore.FileItemList.encode(req).finish()}, function (data) {
                            var rspInfoList = bcstore.RspInfoList.decode(data);
                            toastShowCode(rspInfoList.code);
                            if (rspInfoList.code === bcstore.ReturnCode.Return_OK) {
                                self.getBlogFile();
                            }
                        });
                    }
                });
            };
            self.confirmRenameFile = function (fileIem) {
                self.renameFile(fileIem, "重命名文件名");
            };
            self.renameFile = function (fileIem, newFileName) {
                if (fileIem.fullPath) {
                    return;
                }
                var file = ko.deepObservableClone(fileIem);
                file.fileName = newFileName;
                getRequest("/file/rename", {method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: fileIem.toArrayBuffer()}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.Return_OK) {
                        self.getBlogFile();
                    }
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
