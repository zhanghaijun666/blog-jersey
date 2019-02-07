function HashUtils(root) {
    var self = root || this;

    self.isHash = function (hash) {
        if (!hash) {
            return false;
        }
        if (hash.slice(0, 1) === "#") {
            return "#" + self.getHash() === hash;
        } else {
            return self.getHash() === hash;
        }
    };
    self.getHash = function (localhash) {
        var regExp = /^#([^/?]+)/;
        var hash = localhash || window.location.hash;
        if (regExp.test(hash)) {
            return regExp.exec(hash)[1];
        } else {
            return "";
        }
    };
    self.changeHash = function (hash) {
        if (!hash) {
            return;
        }
        if (hash.slice(0, 1) !== "#") {
            hash = "#" + hash;
        }
        if ("#" + self.getHash() === hash) {
            return;
        }
        window.location.hash = hash;
    };
}
