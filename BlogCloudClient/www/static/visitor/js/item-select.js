(function (global, ko) {
    function ItemSelect(items) {
        var self = this;
        self.items = items;
        self.isAll = ko.observable(false);
        self.isAll.subscribe(function () {
            self.checkAll();
        });
    }
    ItemSelect.prototype.checkAll = function () {
        var self = this;
        ko.utils.arrayForEach(this.items(), function (item) {
            if (ko.isObservable(item.select)) {
                item.select(ko.unwrap(self.isAll));
            }
        });
    };
    ItemSelect.prototype.getSelectedConut = function () {
        var count = 0;
        ko.utils.arrayForEach(this.items(), function (item) {
            if (ko.unwrap(item.select)) {
                count = count + 1;
            }
        });
        return count;
    };
    ItemSelect.prototype.getSelectedStr = function () {
        return this.getSelectedConut() + "/" + this.items().length;
    };
    ItemSelect.prototype.getSelectedItem = function () {
        var itemArray = new Array();
        ko.utils.arrayForEach(this.items(), function (item) {
            if (ko.unwrap(item.select)) {
                itemArray.push(item);
            }
        });
        return itemArray;
    };







    global.ItemSelect = ItemSelect;
})(this, ko);
