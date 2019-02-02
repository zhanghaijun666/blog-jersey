(function (global) {
    define(["text!./login.xhtml", "css!./login.css"], function (pageView) {
        function LoginViewModel(params, componentInfo) {
            var LoginTypeEnum = {
                LOGIN: 1,
                REGISTER: 2,
                FORGETPASSWORD: 3
            };
            var self = this;
            self.loginType = ko.observable(LoginTypeEnum.LOGIN);
            self.user = ko.observable(new User({}, true));
            self.getTemplateName = function () {
                switch (self.loginType()) {
                    case LoginTypeEnum.LOGIN:
                        return "login-template";
                    case LoginTypeEnum.REGISTER:
                        return "register-template";
                    case LoginTypeEnum.FORGETPASSWORD:
                        return "forgetpassword-template";
                }
                return "";
            };


            self.getUser = function () {
                getRequest("/user", {accept: "application/x-protobuf"}, function (data) {
                    var user = bcstore.User.decode(data);
                    if (user.userId) {
                        self.user.setData(new User(user));
                    }
                });
            };
            self.dologin = function (user) {
                getRequest("/user/login", {method: "POST", accept: "application/x-protobuf", type: "application/x-protobuf", data: user.toArrayBuffer()}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.OK) {
                        location.reload();
                    }
                });
            };
            self.logout = function () {
                getRequest("/user/logout", {accept: "application/x-protobuf"}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.OK) {
                        self.user.setData(new User());
                        location.hash = "#home";
                    }
                });
            };
            self.forgetPassword = function () {

            };
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LoginViewModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);