/* global uploadAllFile */

(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            var defaultValue = {
            };
            var self = $.extend(this, defaultValue, params);

            self.uploadFilesMenuItems = function () {
                return [
                    new MenuTab("上传文件", {icon: "fa-upload", clickFun: uploadFile})
//                    ,new MenuTab("上传文件夹", {icon: "fa-upload"})
                ];
            };
            function uploadFile() {
                uploadAllFile(function () {
                    self.getBlogFile();
                });
            }
            self.blogFileLsit = ko.observableArray([]);
            self.getBlogOperateMenu = function () {
                return [
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: global.CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.rename'), {icon: 'fa-edit', clickFun: self.renameFile, menuType: global.CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: global.CustomMenuType.MultipleSelection})
                ];
            };

            self.getBlogFile = function () {
                getRequest("/file/get/default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/", {accept: "application/x-protobuf"}, function (data) {
                    var fileList = bcstore.FileItemList.decode(data);
                    self.blogFileLsit([]);
                    if (fileList.item && fileList.item instanceof Array) {
                        for (var i = 0; i < fileList.item.length; i++) {
                            self.blogFileLsit.push(new FileItem(fileList.item[i], true));
                        }
                    }
                });
            };
            self.deleteFile = function (fileIemList) {
                confirmTipsDialog(l10n('operate.confirmDelete'), function () {
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
                });
            };
            self.renameFile = function (fileIem) {
                if (!fileIem.fullPath) {
                    return;
                }
                TipsInputDialog(fileIem.fileName, function (value) {
                    var file = new FileItem(fileIem);
                    file.fileName(value);
                    getRequest("/file/rename", {method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: fileIem.toArrayBuffer()}, function (data) {
                        var rspInfo = bcstore.RspInfo.decode(data);
                        toastShowCode(rspInfo.code);
                        if (rspInfo.code === bcstore.ReturnCode.Return_OK) {
                            self.getBlogFile();
                        }
                    });
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
