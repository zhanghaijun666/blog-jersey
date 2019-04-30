/* global uploadAllFile, FileUrl, CustomMenuType, directory_contenttype */

(function (global) {
    define(["text!./blog.xhtml", "css!./blog.css"], function (pageView) {
        function BlogModel(params, componentInfo) {
            var defaultValue = {
                fileUrl: new FileUrl("default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/")
            };
            var self = $.extend(this, defaultValue, params);
            self.blogFileLsit = ko.observableArray([]);
            self.blogPathEntry = ko.observable(new PathEntry(self.fileUrl.originPath, self.openDirectory));
            self.uploadFilesMenuItems = function () {
                return [
                    new MenuTab("上传文件", {icon: "fa-upload", clickFun: self.uploadFile})
                ];
            };
//            self.blogPathEntry.subscribe(function (data) {
//                self.getBlogFile();
//            });
            self.openDirectory = function (item) {
                if (item instanceof FileItem && item.contentType === directory_contenttype) {
                    self.blogPathEntry(new PathEntry(item.fullPath, self.openDirectory));
                    self.getBlogFile();
                } else if (item instanceof FileUrl && item.originPath) {
                    self.blogPathEntry(new PathEntry(item.originPath, self.openDirectory));
                    self.getBlogFile();
                }
            };
            self.uploadFile = function () {
                uploadAllFile(self.blogPathEntry().getCurrentUrl().originPath, function () {
                    self.getBlogFile();
                });
            };
            self.getBlogOperateMenu = function () {
                return [
                    new MenuTab(l10n('operate.download'), {icon: 'fa-download', clickFun: self.fileDownload, menuType: CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.rename'), {icon: 'fa-edit', clickFun: self.renameFile, menuType: CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.deleteFile, menuType: CustomMenuType.MultipleSelection})
                ];
            };
            self.getBlogFile = function () {
                getRequest("/file/get/" + self.blogPathEntry().getCurrentUrl().originPath, {accept: "application/x-protobuf"}, function (data) {
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
            self.addFolder = function () {
                TipsInputDialog("", function (value) {
                    if (!value) {
                        return;
                    }
                    getRequest("/file/addfolder/" + self.blogPathEntry().getCurrentUrl().originPath + "/" + value, {method: "POST", type: "application/x-protobuf", accept: "application/x-protobuf"}, function (data) {
                        var rspInfo = bcstore.RspInfo.decode(data);
                        toastShowCode(rspInfo.code);
                        if (rspInfo.code === bcstore.ReturnCode.Return_OK) {
                            self.getBlogFile();
                        }
                    });
                });
            };
            self.fileDownload = function (fileIem) {
                if (!fileIem.fullPath) {
                    return;
                }
                getRequest("/file/download/" + fileIem.fullPath, {method: "GET", type: "application/x-protobuf", accept: "application/x-protobuf"});
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
