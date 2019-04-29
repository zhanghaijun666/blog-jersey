(function (exports) {
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

//--------------------------------*****************************-------------------------------

    exports.standUrlPattern = new RegExp("\\/?(?<rootHash>[^/]+)?\\/(?<gtype>[\\d]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");

    exports.CustomMenuType = {
        SingleSlection: 1,
        MultipleSelection: 2
    };
    exports.MenuTab = function (text, options) {
        options = options || {};
        this.text = text;
        this.icon = options.icon;
        this.title = options.title || "";
        this.isActive = ko.observable(!!ko.unwrap(options.isActive));
        this.clickFun = options.clickFun;
        this.menuType = options.menuType; //CustomMenuType
    };
    exports.FileUrl = function (originPath) {
        var matcher = {groups: {}};
        if (exports.standUrlPattern.test(originPath)) {
            matcher = exports.standUrlPattern.exec(originPath);
        }
        this.rootHash = matcher.groups.rootHash;
        this.gpType = matcher.groups.gtype;
        this.gpId = matcher.groups.gpid;
        this.bucket = matcher.groups.bucket;
        this.path = matcher.groups.path;
    };
    exports.PathEntry = function (originPath) {
        var fileurl = new FileUrl(originPath);
        this.pathNames = new Array();
        if (fileurl.path) {
            this.pathNames = fileurl.path.split("/");
        }

    };
})(this);


