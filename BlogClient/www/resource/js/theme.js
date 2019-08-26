function BlogTheme() {
    function Theme(name, path) {
        this.name = name;
        this.path = path;
    }
    var self = this;
    self.themeList = [
        new Theme('gray', '/static/visitor/css/theme/gray/'),
        new Theme('green', '/static/visitor/css/theme/green/'),
        new Theme('red', '/static/visitor/css/theme/red/')
    ];
    self.getCurrentTheme = function () {
        let theme = self.getCurrentThemeName();
        for (var i = 0; i < self.themeList.length; i++) {
            if (self.themeList[i].name === theme) {
                return self.themeList[i];
            }
        }
        return new Theme();
    };
    self.getCurrentThemeName = function () {
        let theme = getStoreArray("local-theme");
        if (self.isValidate(theme)) {
            return theme;
        } else {
            return 'gray';
        }
    };
    self.isValidate = function (theme) {
        for (var i = 0; i < self.themeList.length; i++) {
            if (self.themeList[i].name === theme) {
                return true;
            }
        }
        return false;
    };

    self.loadTheme = function () {
        var head = document.getElementsByTagName('head')[0];
        var link = document.createElement('link');
        link.href = "/static/visitor/css/theme/" + self.getCurrentThemeName() + "/css.css";
        link.rel = 'stylesheet';
        head.appendChild(link);
    };
}
