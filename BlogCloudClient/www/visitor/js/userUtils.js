function UserUtils(root) {
    var self = root || this;
    self.user = ko.observable();
    self.getUser = function () {
        getRequest("/user", {accept: "application/x-protobuf"}, function (data) {
            var user = bcstore.User.decode(data);
            if (user.userId) {
                self.user(new User(user));
                self.app.refresh();
            }
        });
    };
    self.logout = function () {
        getRequest("/user/logout", {accept: "application/x-protobuf"}, function (data) {
            var rspInfo = bcstore.RspInfo.decode(data);
            toastShowCode(rspInfo.code);
            if (rspInfo.code === bcstore.ReturnCode.OK) {
                self.user(null);
                self.app.refresh();
            }
        });
    };

    self.isLogin = ko.computed(function () {
        return !!(self.user() && self.user().userId && self.user().username && self.user().username());
    }, self);
}
