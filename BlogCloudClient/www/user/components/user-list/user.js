(function (global) {
    define(["knockout", "text!./user.xhtml", "css!./user.css"], function (ko, pageView) {
        function UserListModel(params, componentInfo) {
            var defaultValue = {
                searchItems: [
                    new DropdownMenu(l10n('user.userName'), {icon: 'fa-user', select: true}),
                    new DropdownMenu(l10n('user.userNick'), {icon: 'fa-user'}),
                    new DropdownMenu(l10n('user.userMail'), {icon: 'fa-envelope-o'}),
                    new DropdownMenu(l10n('user.userPhone'), {icon: 'fa-phone'})
                ]
            };
            var self = $.extend(this, defaultValue, params);

            self.users = ko.observableArray([]);
            function initMOdel() {
                getUser();
                self.userOperateMenu = [
                    new CustomMenu(l10n('operate.edit'), {icon: 'fa-pencil-square-o', clickFun: self.editUserInfo, menuType: CustomMenuType.SingleSlection}),
                    new CustomMenu(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.SingleSlection}),
                    new CustomMenu(l10n('operate.delete'), {icon: 'fa-trash-o', clickFun: self.confirmDeleteUser, menuType: CustomMenuType.MultipleSelection})
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
                let item = new User(user);
                User.addVerification(item);
                item.confirmPassword = ko.observable("").extend({equal: {params: item.password(), message: l10n("user.pwdNotSame")}});
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
                var req = bcs.UserList.create();
                ko.utils.arrayForEach(users, function (user) {
                    req.items.push(user.unwrap());
                });
                ajax({url: "/users", method: "DELETE", type: "application/x-protobuf", accept: "application/x-protobuf", data: pbs.UserList.encode(req).finish(), complete: function (data) {
                        var rspInfo = bcs.RspInfo.decode(data);
                        toastShowCode(rspInfo.code);
                        if (rspInfo.code === bcs.ReturnCode.OK) {
                            getUser();
                        }
                    }
                });
            }
            function doUpdateUser(user, callBack) {
                ajax({url: "/users", method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: user.toArrayBuffer(), complete: function (data) {
                        var rspInfo = bcs.RspInfo.decode(data);
                        toastShowCode(rspInfo.code);
                        if (rspInfo.code === bcs.ReturnCode.OK) {
                            getUser();
                            if (callBack) {
                                callBack();
                            }
                        }
                    }
                });
            }
            function doCreateUser(user, callBack) {
                ajax({url: "/users/create", method: "PUT", type: "application/x-protobuf", accept: "application/x-protobuf", data: user.toArrayBuffer(), complete: function (data) {
                        var rspInfo = bcs.RspInfo.decode(data);
                        toastShowCode(rspInfo.code);
                        if (rspInfo.code === bcs.ReturnCode.OK) {
                            getUser();
                            if (callBack) {
                                callBack();
                            }
                        }
                    }
                });
            }
            function getUser() {
                ajax({url: "/users/all/false", accept: "application/x-protobuf", complete: function (data) {
                        var users = bcs.UserList.decode(data);
                        self.users(User.fromPbsUsers(users));
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
