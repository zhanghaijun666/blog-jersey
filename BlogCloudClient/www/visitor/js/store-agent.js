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
        self.username = ko.observable(ko.unwrap(self.username));
        self.password = ko.observable(ko.unwrap(self.password));
        self.nickname = ko.observable(ko.unwrap(self.nickname));
        self.phone = ko.observable(ko.unwrap(self.phone));
        self.email = ko.observable(ko.unwrap(self.email));
        self.rememberMe = ko.observable(!!ko.unwrap(self.rememberMe));
        if (isValidate) {
            self.username.extend({required: {message: l10n('validation.nameEmpty')}});
            self.password.extend({required: {message: l10n('validation.passwordEmpty')}});
            self.email.extend({email: {message: l10n('validation.emailError')}});
            self.phone.extend({phoneUS: {message: l10n('validation.phoneError')}});
        }
    }
    User.prototype = new Message("User");
    User.prototype.getFullName = function () {
        return ko.unwrap(this.nickname) + ' ( ' + ko.unwrap(this.username) + ' ) ';
    };

























    window.User = User;
}
