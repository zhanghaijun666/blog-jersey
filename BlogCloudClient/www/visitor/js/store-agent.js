function initStore() {
    function Message(type) {
        this.$type = bcstore.lookupType(type);
    }
    Message.prototype.toArrayBuffer = function () {
        let store = ko.deepObservableClone(this);
        let errors = this.$type.verify(store);
        if (errors) {
            throw new Error(errors);
        }
        return this.$type.encode(store).finish();
    };

    function getStoreFileds(fileds) {
        let options = {};
        if (bcstore[fileds] && bcstore[fileds].fields) {
            ko.utils.arrayForEach(Object.keys(bcstore[fileds].fields), function (Field) {
                options[Field] = null;
            });
        }
        return options;
    }

    function User(options, isValidate) {
        var self = $.extend(this, getStoreFileds("User"), options);
        self.username = ko.observable(self.username);
        self.password = ko.observable(self.password);
        self.nickname = ko.observable(self.nickname);
        self.phone = ko.observable(self.phone);
        self.email = ko.observable(self.email);
        self.rememberMe = ko.observable(self.rememberMe);
        if (isValidate) {
            self.username.extend({required: {message: '用户名称不能为空'}});
            self.password.extend({required: {message: '密码不能为空'}});
            self.email.extend({email: {}});
            self.phone.extend({phoneUS: {}});
        }
    }
    User.prototype = new Message("User");
    User.prototype.getFullName = function () {
        return ko.unwrap(this.nickname) + ' ( ' + ko.unwrap(this.username) + ' ) ';
    };

























    window.User = User;
}
