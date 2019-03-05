(function (exports) {
    exports.simulateClick = function (element) {
        element.addEventListener("click", function () {
            clicked = true;
            element.removeEventListener("click", arguments.callee, true);
        }, true);
        try {
            element.click();
        } catch (e) {
        }
    };
})(this);