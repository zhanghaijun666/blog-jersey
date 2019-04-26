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

//--------------------------------*****************************-------------------------------

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
})(this);


