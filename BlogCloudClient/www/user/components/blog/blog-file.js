(function (global) {
    define(["text!./blog-file.xhtml", "css!./blog-file.css"], function (pageView) {
        function BlogFileModel(params, componentInfo) {
            var defaultValue = {
                fileUrl: new FileUrl("default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/")
            };
            var self = $.extend(this, defaultValue, params);
            self.leftMenuList = ko.observableArray([
                new Menu({name: '我的文件', icon: 'fa fa-book', menuId: 1001, isActive: true, clickFun: menuClickFun}),
                new Menu({name: '图片', icon: 'fa fa-picture-o', parentId: 1001, clickFun: menuClickFun}),
                new Menu({name: '文档', icon: 'fa fa-file-text-o', parentId: 1001, clickFun: menuClickFun}),
                new Menu({name: '视频', icon: 'fa fa-file-video-o', parentId: 1001, clickFun: menuClickFun}),
                new Menu({name: '音乐', icon: 'fa fa-headphones', parentId: 1001, clickFun: menuClickFun}),
                new Menu({name: '其他', icon: 'fa fa-file-o', parentId: 1001, clickFun: menuClickFun})
            ]);
            function menuClickFun(menu) {
                console.log(menu);
            }








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
