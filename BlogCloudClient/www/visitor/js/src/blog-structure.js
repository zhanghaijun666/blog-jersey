function Message(type) {
    this.$type = bcstore.lookupType(type);
}
Message.prototype.toArrayBuffer = function () {
    
    if (bcstore[fileds] && bcstore[fileds].fields) {
        ko.utils.arrayForEach(Object.keys(bcstore[fileds].fields), function (Field) {
            options[Field] = null;
        });
    }
    
    return this.$type.encode(unwrapedMsg).finish();
};

function getStoreFileds(fileds) {
    options = {};
    if (bcstore[fileds] && bcstore[fileds].fields) {
        ko.utils.arrayForEach(Object.keys(bcstore[fileds].fields), function (Field) {
            options[Field] = null;
        });
    }
    return options;
}

function User(options) {
    let self = $.extend(this, getStoreFileds(User), options);
//    self.
}
//User.prototype = new Message("User");