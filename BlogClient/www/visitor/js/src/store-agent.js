function initStore() {
    function Message(type) {
        this.type = type;
    }
    Message.prototype.toArrayBuffer = function () {
        var store = ko.deepObservableClone(this);
        var $type = bcstore.lookupType(this.type);
        var errors = $type.verify(store);
        if (errors) {
            throw new Error(errors);
        }
        return $type.encode(store).finish();
    };

    function getStoreFileds(fileds) {
        var options = {};
        if (bcstore[fileds] && bcstore[fileds].fields) {
            ko.utils.arrayForEach(Object.keys(bcstore[fileds].fields), function (Field) {
                options[Field] = null;
            });
        }
        return options;
    }

    function User(options, isObservable, isValidate) {
        var self = $.extend(this, getStoreFileds("User"), options);
        if (isObservable) {
            self.username = ko.observable(ko.unwrap(self.username));
            self.password = ko.observable(ko.unwrap(self.password));
            self.nickname = ko.observable(ko.unwrap(self.nickname));
            self.phone = ko.observable(ko.unwrap(self.phone));
            self.email = ko.observable(ko.unwrap(self.email));
            self.rememberMe = ko.observable(!!ko.unwrap(self.rememberMe));
        }
        if (isObservable && isValidate) {
            self.username.extend({required: {message: l10n('validation.nameEmpty')}});
            self.password.extend({required: {message: l10n('validation.passwordEmpty')}});
            self.email.extend({email: {message: l10n('validation.emailError')}});
            self.phone.extend({phoneUS: {message: l10n('validation.phoneError')}});
        }
    }
    User.prototype = new Message("UserInfo");
    User.prototype.getFullName = function () {
        return ko.unwrap(this.nickname) + ' ( ' + ko.unwrap(this.username) + ' ) ';
    };

    function Menu(options, isObservable, isValidate) {
        var self = $.extend(this, getStoreFileds("Menu"), options);
        self.parentId = self.parentId || 0;
        self.isActive = ko.observable(!!ko.unwrap(self.isActive));
        self.element = ko.observable();
        if (isObservable) {
            self.parentId = ko.observable(ko.unwrap(self.parentId));
            self.name = ko.observable(ko.unwrap(self.name));
            self.icon = ko.observable(ko.unwrap(self.icon));
            self.template = ko.observable(ko.unwrap(self.template));
            self.hash = ko.observable(ko.unwrap(self.hash));
            self.isDeletable = ko.observable(!!ko.unwrap(self.isDeletable));
            self.isDefaultShow = ko.observable(!!ko.unwrap(self.isDefaultShow));
            self.status = ko.observable(ko.unwrap(self.status));
        }
        if (isObservable && isValidate) {
            self.name.extend({required: {message: l10n('validation.nameEmpty')}});
            self.hash.extend({required: {message: l10n('validation.nameEmpty')}});
        }
    }
    Menu.prototype = new Message("Menu");
    Menu.prototype.getMenuName = function () {
        if (ko.unwrap(this.name)) {
            var name = l10n('menu.' + ko.unwrap(this.name));
            return name.indexOf(".") === -1 ? name : this.name;
        }
        return "";
    };
    function FileItem(options, isObservable) {
        var self = $.extend(this, getStoreFileds("FileItem"), options);
        if (isObservable) {
            self.select = ko.observable(false);
            self.fileName = ko.observable(self.fileName);
        }
    }
    FileItem.prototype = new Message("FileItem");


























    window.User = User;
    window.Menu = Menu;
    window.FileItem = FileItem;
}
