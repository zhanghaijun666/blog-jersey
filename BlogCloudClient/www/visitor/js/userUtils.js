function UserUtils(root) {
    var self = root || this;
    self.user = ko.observable();
    self.getUser = function (callback) {
        if (self.isLogin()) {
            if (isFunction(callback)) {
                callback();
            }
        } else {
            getRequest("/user", {accept: "application/x-protobuf", timeout: 2000}, function (data, xhr) {
                var user = bcstore.User.decode(data);
                if (user.userId) {
                    self.user(new User(user, true));
                }
                if (isFunction(callback)) {
                    callback();
                }
            }, function (xhr) {
                self.user(null);
                if (isFunction(callback)) {
                    callback();
                }
            });
        }
    };
    self.logout = function () {
        showDialog({
            header: l10n("operate.tips"),
            bodyTemplate: "template-small-dialogMsg",
            tipsMsg: l10n('operate.confirmLogout'),
            dialogClass: "modal-sm",
            success: function () {
                getRequest("/user/logout", {accept: "application/x-protobuf"}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.Return_OK) {
                        self.user(null);
                        self.app.refresh();
                    }
                });
            }
        });
    };

    self.isLogin = function () {
        return !!(self.user() && self.user().userId && self.user().username && self.user().username());
    };
}
