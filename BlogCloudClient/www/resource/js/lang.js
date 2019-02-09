(function (global) {
    global.l10n = function (str) {
        var strs = str.split(".");
        if (strs.length === 2) {
            let lang = global.i18nlans[getStoreArray("local-lang") || "zh-CN"];
            if (lang[strs[0]] && lang[strs[0]][strs[1]]) {
                return lang[strs[0]][strs[1]];
            }
        }
        return str;
    };
    global.switchLocalLang = function () {
        let langArray = Object.keys(global.i18nlans);
        let index = langArray.indexOf(getStoreArray("local-lang") || "zh-CN");
        if (index === langArray.length - 1) {
            index = 0;
        } else {
            index = index + 1;
        }
        storeLocalArray("local-lang", langArray[index]);
        location.reload();
    };
})(this);
