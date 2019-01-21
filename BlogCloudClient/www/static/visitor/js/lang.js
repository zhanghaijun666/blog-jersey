(function (global) {
    define(["knockout", "/static/local/lang/en.js", "/static/local/lang/zh-CN.js"], function (ko, langEN, langCN) {
        global.localLang = ko.observable("zh-CN");
        global.l10n = function (str) {
            var strs = str.split(".");
            if (strs.length === 2) {
                let lang = langCN;
                if (localLang() === "en") {
                    lang = langEN;
                }
                if (lang[strs[0]] && lang[strs[0]][strs[1]]) {
                    return lang[strs[0]][strs[1]];
                }
            }
            return str;
        };
        global.switchLocalLang = function () {
            if (global.localLang() === "zh-CN") {
                global.localLang("en");
            }
            storeLocalArray("local-lang", global.localLang());
            location.reload();
        };
    });
})(this);
