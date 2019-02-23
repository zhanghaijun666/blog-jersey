(function (global) {
    define(["knockout", "text!./user.xhtml", "css!./user.css"], function (ko, pageView) {
        function UserListModel(params, componentInfo) {
            var defaultValue = {
                searchItems: [
                    new MenuTab(l10n('user.userName'), {icon: 'fa-user', select: true}),
                    new MenuTab(l10n('user.userNick'), {icon: 'fa-user'}),
                    new MenuTab(l10n('user.userMail'), {icon: 'fa-envelope-o'}),
                    new MenuTab(l10n('user.userPhone'), {icon: 'fa-phone'})
                ]
            };
            var self = $.extend(this, defaultValue, params);

            self.userList = ko.observableArray([]);
            function initMOdel() {
                getUser();
                self.userOperateMenu = [
                    new MenuTab(l10n('operate.edit'), {icon: 'fa-pencil-square-o', clickFun: self.editUserInfo, menuType: CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.SingleSlection}),
                    new MenuTab(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.MultipleSelection})
                ];
            }

            self.createUser = function () {
                userInfoDialog({}, function (user, callback) {
                    doCreateUser(user, callback);
                });
            };
            self.editUserInfo = function (user) {
                userInfoDialog(user, function (user, callback) {
                    doUpdateUser(user, callback);
                });
            };
            self.confirmDeleteUser = function (user, event) {
                showDialog({
                    header: l10n("operate.tips"),
                    bodyTemplate: "template-small-dialogMsg",
                    dialogClass: "modal-sm",
                    success: function () {
                        doDeleteUser(user instanceof Array ? user : [user]);
                    }
                });
            };
            function userInfoDialog(user = {}, callback){
                let item = new User(user, true, true);
                item.confirmPassword = ko.observable("").extend({equal: {params: item.password, message: l10n("user.pwdNotSame")}});
                showDialog($.extend({
                    header: l10n("user.userInfo"),
                    bodyTemplate: "template-user-info",
                    isCloseDialog: false,
                    success: function (dialog) {
                        let errors = ko.validation.group(dialog.user);
                        if (errors().length !== 0) {
                            errors.showAllMessages();
                        } else if (isFunction(callback)) {
                            callback(dialog.user, function () {
                                dialog.dialogRef.modal("hide");
                            });
                        }
                    }}, {user: item}));
            }
            function doDeleteUser(users) {
                var req = bcstore.UserList.create();
                ko.utils.arrayForEach(users, function (user) {
                    req.items.push(ko.deepObservableClone(user));
                });
                getRequest("/user", {method: "DELETE", type: "application/x-protobuf", accept: "application/x-protobuf", data: bcstore.UserList.encode(req).finish()}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.OK) {
                        getUser();
                    }
                });
            }
            function doUpdateUser(user, callBack) {
                getRequest("/user", {method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: user.toArrayBuffer()}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.OK) {
                        getUser();
                        if (callBack) {
                            callBack();
                        }
                    }
                });
            }
            function doCreateUser(user, callBack) {
                getRequest("/user/create", {method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: user.toArrayBuffer()}, function (data) {
                    var rspInfo = bcstore.RspInfo.decode(data);
                    toastShowCode(rspInfo.code);
                    if (rspInfo.code === bcstore.ReturnCode.OK) {
                        getUser();
                        if (callBack) {
                            callBack();
                        }
                    }
                });
            }
            function getUser() {
                getRequest("/user/all/false", {accept: "application/x-protobuf"}, function (data) {
                    let userList = bcstore.UserList.decode(data);
                    if (userList && userList.items) {
                        self.userList().length = 0;
                        userList.items.forEach(function (item) {
                            self.userList.push(new User(item, true));
                        });
                    }
                });
            }
            initMOdel();
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new UserListModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
