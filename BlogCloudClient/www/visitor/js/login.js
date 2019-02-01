(function (ko, global) {
    function LoginJs() {
        var self = this;
        self.getUser = function () {
            getRequest("/user", {accept: "application/x-protobuf"}, function (data) {
                var user = bcstore.User.decode(data);
                if (user.userId) {
                    self.user.setData(new User(user));
                }
            });
        };
        self.dologin = function () {

        };
        self.forgetPassword = function () {

        };
        return self;
    }
    global.LoginJs = LoginJs;
})(ko, window);

