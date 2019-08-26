(function () {
    define([], function () {
        function BlogUtilModel() {
            var self = this;
            self.getLeftMenuList = function (menuClickFun) {
                return [
                    new Menu({name: '我的文件', icon: 'fa fa-book', menuId: 1001, isActive: true, clickFun: menuClickFun}),
                    new Menu({name: '图片', icon: 'fa fa-picture-o', parentId: 1001, clickFun: menuClickFun}),
                    new Menu({name: '文档', icon: 'fa fa-file-text-o', parentId: 1001, clickFun: menuClickFun}),
                    new Menu({name: '视频', icon: 'fa fa-file-video-o', parentId: 1001, clickFun: menuClickFun}),
                    new Menu({name: '音乐', icon: 'fa fa-headphones', parentId: 1001, clickFun: menuClickFun}),
                    new Menu({name: '其他', icon: 'fa fa-file-o', parentId: 1001, clickFun: menuClickFun})
                ];
            };
            self.findFileItemList = function (url, callback) {
                getRequest(url, {accept: "application/x-protobuf"}, function (data) {
                    var fileList = bcstore.FileItemList.decode(data);
                    var arr = new Array();
                    if (fileList.item && fileList.item instanceof Array) {
                        for (var i = 0; i < fileList.item.length; i++) {
                            arr.push(new FileItem(fileList.item[i], true));
                        }
                    }
                    if (isFunction(callback)) {
                        callback(arr);
                    }
                });
            };
        }
        return new BlogUtilModel();
    });
})();
