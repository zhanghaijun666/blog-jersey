function MenuTab(name, options) {
    options = options || {};
    this.name = name;
    this.title = options.title || "";
    this.isActive = ko.observable(!!ko.unwrap(options.isActive));
    this.icon = options.icon;
    this.click = options.click;
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
