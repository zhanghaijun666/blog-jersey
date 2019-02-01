(function (global, ko) {

    //深度克隆
    function getDeepClone(obj) {
        var result, oClass = getClass(obj);
        //确定result的类型
        if (oClass === "Object") {
            result = {};
        } else if (oClass === "Array") {
            result = [];
        } else {
            return obj;
        }
        for (let key in obj) {
            var copy = obj[key];
            if (getClass(copy) === "Object" || getClass(copy) === "Array") {
                result[key] = arguments.callee(copy);//递归调用
            } else {
                result[key] = obj[key];
            }
        }
        return result;
    }
    //返回对象的类型
    function getClass(o) {
        if (o === null) {
            return "Null";
        }
        if (o === undefined) {
            return "Undefined";
        }
        //使用typeof（Array）返回的是object,所以不用typeof
        return Object.prototype.toString.call(o).slice(8, -1);
    }




    function undeepObservableClone(item) {
        item = ko.unwrap(item);
        if (typeof item === 'undefined' || item === null) {
            return null;
        }
    }








})(this, ko);