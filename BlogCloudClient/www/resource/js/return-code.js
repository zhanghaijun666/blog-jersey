(function (global) {
    global.toastShow = function (options) {
        var toastEle = "<!-- ko template: 'template-toast' --><!-- /ko -->";
        var container = document.createElement("div");
        container.innerHTML = toastEle;
        document.body.appendChild(container);
        ko.applyBindings(new ToastRoot(options), container);
        window.setTimeout(function () {
            document.body.removeChild(container);
        }, 1500);
    };
    global.toastShowCode = function (code) {
        toastShow({returnCode: code});
    };
    global.toastShowMsg = function (code) {
        toastShow({message: code});
    };

    function ToastRoot(options) {
        var self = this;
        options = options || {};
        self.returnCode = options.returnCode;
        self.message = options.message || "未知提示";
        self.headText = options.headText || "提示信息";

    }
    ToastRoot.prototype.getMessage = function () {
        if (this.returnCode && window.bcs) {
            return l10n('returnCode.' + window.bcs.lookupEnum("ReturnCode").valuesById[this.returnCode].toLowerCase());
        } else {
            return this.message;
        }
    };
})(this);
