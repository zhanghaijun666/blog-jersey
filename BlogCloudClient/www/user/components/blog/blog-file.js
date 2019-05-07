/* global FileUrl, CustomMenuType */

(function (global) {
    define(["text!./blog-file.xhtml", "./blog-util.js", "css!./blog-file.css"], function (pageView, myBlog) {
        function BlogFileModel(params, componentInfo) {
            var defaultValue = {
                filePath: RootView.currentFilePath,
            };
            var self = $.extend(this, defaultValue, params, myBlog);
            self.blogFileLsit = ko.observableArray([]);
            self.blogPathEntry = ko.observable();
            self.currentTemplate = ko.observable();
            self.leftMenuList = ko.observableArray();
            self.initFun = function () {
                self.leftMenuList(self.getLeftMenuList(menuClickFun));
                self.blogPathEntry(new PathEntry(ko.unwrap(self.filePath), self.openDirectory));
                self.currentTemplate("my-file-template");
                self.getBlogFile();
            };
            self.filePathSubscribe = null;
            if (ko.isObservable(self.filePath)) {
                self.filePathSubscribe = self.filePath.subscribe(function (value) {
                    self.initFun();
                });
            }
            self.elementDispose = function () {
                if (self.filePathSubscribe) {
                    self.filePathSubscribe.dispose();
                }
            };

            function menuClickFun(menu) {
                console.log(menu);
            }
            self.uploadFilesMenuItems = function () {
                return [
                    new MenuTab("上传文件", {icon: "fa-upload", clickFun: self.uploadFile})
                ];
            };
            self.openDirectory = function (item) {
                if (item instanceof FileItem && item.contentType === global.DIRECTORY_CONTENTTYPE) {
                    RootView.changeHash("#file" + (new RegExp("^\/").test(item.fullPath) ? "" : "/") + item.fullPath);
                } else if (item instanceof FileUrl && item.getOriginPath()) {
                    RootView.changeHash("#file/" + item.getOriginPath());
                }
            };
            self.uploadFile = function () {
                uploadAllFile(self.blogPathEntry().getCurrentUrl().getOriginPath(), function () {
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
                self.findFileItemList("/file/get/" + self.blogPathEntry().getCurrentUrl().getOriginPath(), function (fileList) {
                    console.log("data:" + fileList.length);
                    self.blogFileLsit(fileList);
                    console.log("file:" + self.blogFileLsit.length);
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
                    getRequest("/file/addfolder/" + self.blogPathEntry().getCurrentUrl().getOriginPath() + "/" + value, {method: "POST", type: "application/x-protobuf", accept: "application/x-protobuf"}, function (data) {
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
                window.open(getServerUrl("/file/download/" + fileIem.fullPath));
            };
            self.initFun();
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new BlogFileModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
