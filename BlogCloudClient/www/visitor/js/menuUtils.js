function MenuUtils(root) {
    var self = root || this;

    self.menuList = ko.observableArray([]);
    self.currentMenu = ko.observable("");

    self.getMenu = function (callback) {
        if (self.menuList().length > 0) {
            if (isFunction(callback)) {
                callback(self.menuList());
            }
            return;
        }
        getRequest("/menu/hash/" + self.getHash(), {accept: "application/x-protobuf"}, function (data) {
            let menuList = bcstore.MenuList.decode(data);
            self.menuList().length = 0;
            ko.utils.arrayForEach(menuList.items, function (menu) {
                self.menuList.push(new Menu(menu));
            });
            if (isFunction(callback)) {
                callback(self.menuList());
            }
        });
    };

}
