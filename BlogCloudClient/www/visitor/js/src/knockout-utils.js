(function (global, ko) {
    //深度克隆
    function deepClone(object, isUnwrapObservable) {
        var result = {}, oClass = getClass(object);

        if (oClass === "Observable") {
            return isUnwrapObservable ? ko.unwrap(object) : ko.observable(ko.unwrap(object));
        } else if (oClass === "Object") {
            ko.utils.arrayForEach(Object.keys(object), function (obj) {
                if (obj.indexOf("$") === -1 && obj !== "constructor") {
                    result[obj] = deepClone(object[obj], isUnwrapObservable);
                }
            });
        } else if (oClass === "Array") {
            ko.utils.arrayForEach(object, function (obj) {
                result[obj] = deepClone(object[obj], isUnwrapObservable);
            });
        } else {
            return object;
        }
        return result;
    }
    //返回对象的类型
    function getClass(o) {
        if (ko.isObservable(o)) {
            return "Observable";
        }
        if (o === null) {
            return "Null";
        }
        if (o === undefined) {
            return "Undefined";
        }
        //使用typeof（Array）返回的是objectect,所以不用typeof
        return Object.prototype.toString.call(o).slice(8, -1);
    }

    ko.deepObservableClone = function (object) {
        return deepClone(object, true);
    };

    ko.bindingHandlers.elementCallback = {
        init: function (element, valueAccessor) {
            var params = valueAccessor();
            var initCallback = params.init;
            var disposeCallback = params.dispose;
            if (isFunction(initCallback)) {
                window.setTimeout(function () {
                    initCallback(element);
                }, 100);
            }
            ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
                if (isFunction(disposeCallback)) {
                    disposeCallback(element);
                }
            });
        },
        update: function (element, valueAccessor) {

        }
    };



    global.getClass = getClass;
    global.getDeepClone = function (object) {
        return deepClone(object, false);
    };
})(this, ko);
