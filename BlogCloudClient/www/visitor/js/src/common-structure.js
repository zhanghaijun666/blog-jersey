function MenuTab(text, options) {
    options = options || {};
    this.text = text;
    this.icon = options.icon;
    this.title = options.title || "";
    this.isActive = ko.observable(!!ko.unwrap(options.isActive));
    this.clickFun = options.clickFun;
    
    this.menuType = options.menuType; //CustomMenuType
}

CustomMenuType = {
    SingleSlection: 1,
    MultipleSelection: 2
};
