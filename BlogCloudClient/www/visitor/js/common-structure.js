function MenuTab(menuId, name, options) {
    options = options || {};
    this.menuId = menuId;
    this.name = name;
    this.isActive = ko.observable(false);
    this.icon = options.icon;
    this.deletable = options.deletable;
    this.click = options.click;
    this.component = options.component;
}
function DropdownMenu(name, options) {
    options = options || {};
    this.name = name;
    this.select = ko.observable(!!ko.unwrap(options.select));
    this.icon = options.icon;
    this.title = options.title || "";
    this.clickFun = options.clickFun;
}
function CustomMenu(text, options) {
    options = options || {};
    this.text = text;
    this.icon = options.icon;
    this.clickFun = options.clickFun;
    this.title = options.title;
    this.menuType = options.menuType;
}
CustomMenuType = {
    SingleSlection: 1,
    MultipleSelection: 2
};