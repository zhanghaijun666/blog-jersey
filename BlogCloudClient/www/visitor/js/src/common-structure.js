(function (exports) {
    exports.DIRECTORY_CONTENTTYPE = "application/cc-directory";
    exports.standUrlPattern = new RegExp("\\/?(?<rootHash>[^/]+)?\\/(?<gtype>[\\d]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");
    exports.CustomMenuType = {
        SingleSlection: 1,
        MultipleSelection: 2
    };
    exports.getStoreArray = function (storeKey) {
        var storage = window.localStorage;
        if (storage) {
            var str = storage.getItem(storeKey);
            if (str) {
                //需要加密
                if (str) {
                    try {
                        var obj = JSON.parse(str);
                        str = null;
                        return obj;
                    } catch (e) {
                    }
                }
            }
        }
        return null;
    };
    exports.storeLocalArray = function (storeKey, obj) {
        var storage = window.localStorage;
        if (storage) {
            if (typeof obj === "string") {
                obj = obj.trim();
            }
            var str = JSON.stringify(obj);
            //需要解密
            storage.setItem(storeKey, str);
        }
    };
    exports.isFunction = function (method) {
        return typeof (method) === 'function';
    };
    exports.simulateClick = function (element) {
        element.addEventListener("click", function () {
            clicked = true;
            element.removeEventListener("click", arguments.callee, true);
        }, true);
        try {
            element.click();
        } catch (e) {
        }
    };
    exports.formatLongSize = function (limit) {
        var size = "";
        if (limit < 0.1 * 1024) { //如果小于0.1KB转化成B  
            size = limit.toFixed(2) + "B";
        } else if (limit < 0.1 * 1024 * 1024) {//如果小于0.1MB转化成KB  
            size = (limit / 1024).toFixed(2) + "KB";
        } else if (limit < 0.1 * 1024 * 1024 * 1024) { //如果小于0.1GB转化成MB  
            size = (limit / (1024 * 1024)).toFixed(2) + "MB";
        } else { //其他转化成GB  
            size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
        }
        var sizestr = size + "";
        var len = sizestr.indexOf("\.");
        var dec = sizestr.substr(len + 1, 2);
        if (dec === "00") {//当小数点后为00时 去掉小数部分  
            return sizestr.substring(0, len) + sizestr.substr(len + 3, 2);
        }
        return sizestr;
    };
    exports.getParentFilUrlList = function (originfileUrl) {
        var pathList = new Array();
        if (originfileUrl instanceof FileUrl) {
            var fileurl = originfileUrl;
            while (fileurl.path) {
                pathList.unshift(fileurl);
                var path;
                if (new RegExp("^\/[^\/]+$").test(fileurl.path)) {
                    path = "/";
                } else {
                    path = fileurl.path.substring(0, fileurl.path.lastIndexOf("/"));
                }
                fileurl = new FileUrl(fileurl.getPathPrefix() + path);
            }
        }
        return pathList;
    };
    exports.getFileName = function (filepath) {
        return filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length);
    };
//--------------------------------*****************************-------------------------------
    MenuTab = function (text, options) {
        options = options || {};
        this.text = text; //用于页面显示
        this.value = options.value; //用于js逻辑处理
        this.icon = options.icon;
        this.title = options.title || "";
        this.isActive = ko.observable(!!ko.unwrap(options.isActive));
        this.clickFun = options.clickFun;
        this.menuType = options.menuType; //CustomMenuType
    };
    FileUrl = function (originPath) {
        var matcher = {groups: {}};
        if (exports.standUrlPattern.test(originPath)) {
            matcher = exports.standUrlPattern.exec(originPath);
        }
        this.originPath = originPath;
        this.rootHash = matcher.groups.rootHash;
        this.gpType = matcher.groups.gtype;
        this.gpId = matcher.groups.gpid;
        this.bucket = matcher.groups.bucket;
        this.path = matcher.groups.path;
    };
    FileUrl.prototype.getPathPrefix = function () {
        return this.rootHash + "/" + this.gpType + "/" + this.gpId + "/" + this.bucket;
    };
    FileUrl.prototype.getFileName = function () {
        return exports.getFileName(this.path);
    };
    FileUrl.prototype.getParent = function () {
        var arr = getParentFilUrlList(this);
        if (arr.length > 1) {
            return arr[arr.length - 2];
        }
        return null;
    };
    FileUrl.prototype.getChild = function (childName) {
        if (this.path && childName) {
            if (new RegExp("\/$").test(this.originPath)) {
                return new FileUrl(this.originPath + childName);
            } else {
                return new FileUrl(this.originPath + "/" + childName);
            }
        }
        return null;
    };
    PathEntry = function (originPath, changePath) {
        this.pathList = exports.getParentFilUrlList(new FileUrl(originPath));
        this.changePath = isFunction(changePath) ? changePath : function (fileUrl) {};
    };
    PathEntry.prototype.getCurrentUrl = function () {
        if (this.pathList.length > 0) {

            return this.pathList[this.pathList.length - 1];
        }
        return null;
    };
    PathEntry.prototype.getPrevUrl = function () {
        var current = this.getCurrentUrl();
        if (current) {
            return current.getParent();
        }
        return null;
    };
    function DataPaging(totalNumber, updateDataFun) {
        this.totalNumber = totalNumber && totalNumber > 0 ? totalNumber : 1;
        this.currentPage = ko.observable(1);
        this.updateDataFun = isFunction(updateDataFun) ? updateDataFun : function (perPage, currentPage) {};
        this.jumpValue = ko.observable(ko.unwrap(this.currentPage));
        function pageSizeFun(perPage, currentPage) {
            this.currentPage(1);
            this.updateDataFun(perPage, 1);
        }
        this.pageSizeOption = ko.observableArray([
            new MenuTab("05/页", {value: 5, isActive: false, clickFun: pageSizeFun.bind(this, 5, 1)}),
            new MenuTab("10/页", {value: 10, isActive: true, clickFun: pageSizeFun.bind(this, 10, 1)}),
            new MenuTab("15/页", {value: 15, isActive: false, clickFun: pageSizeFun.bind(this, 15, 1)}),
            new MenuTab("20/页", {value: 20, isActive: false, clickFun: pageSizeFun.bind(this, 20, 1)})
        ]);
        this.updateDataFun(this.getPerPageNumber(), ko.unwrap(this.currentPage));
    }
    DataPaging.prototype.getPerPageNumber = function () {
        var arr = this.pageSizeOption();
        if (arr instanceof Array) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] instanceof MenuTab && ko.unwrap(arr[i].isActive)) {
                    return ko.unwrap(arr[i].value);
                }
            }
        }
        return null;
    };
    DataPaging.prototype.isShowLastButton = function () {
        return ko.unwrap(this.currentPage) > 1;
    };
    DataPaging.prototype.isShowNextButton = function () {
        return ko.unwrap(this.currentPage) * this.getPerPageNumber() < this.totalNumber;
    };
    DataPaging.prototype.getTotalPage  = function () {
        return Math.ceil(this.totalNumber / this.getPerPageNumber());
    };
    DataPaging.prototype.getPageNumerArray = function () {
        function getStartPage(currentPage) {
            var index = 1;
            while (true) {
                if (currentPage <= 5 * index) {
                    return index === 1 ? 1 : 5 * (index - 1);
                }
                index = index + 1;
            }
            return 1;
        }
        var maxPage = this.getTotalPage();
        var startNumber = Math.max(1, maxPage - ko.unwrap(this.currentPage) < 5 ? maxPage - 10 + 1 : getStartPage(ko.unwrap(this.currentPage)));
        var endNumber = Math.min(maxPage, startNumber + 10 - 1);
        var arr = new Array();
        var index = 0;
        while (startNumber + index <= endNumber) {
            arr.push(startNumber + index);
            index = index + 1;
        }
        return arr;
    };
    DataPaging.prototype.changePage = function (pageNumber) {
        if (pageNumber < 1 || pageNumber === ko.unwrap(this.currentPage)) {
            return;
        }
        this.currentPage(pageNumber);
        this.updateDataFun(this.getPerPageNumber(), pageNumber);
    };
    exports.MenuTab = MenuTab;
    exports.FileUrl = FileUrl;
    exports.PathEntry = PathEntry;
    exports.DataPaging = DataPaging;
})(this);


