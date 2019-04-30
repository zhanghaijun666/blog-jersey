//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    ko.components.register('custom-checkbox', {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                function CheckboxViewModel(params, componentInfo) {
                    var defaultValue = {
                        checked: ko.observable(false),
                        label: ""
                    };
                    var self = $.extend(this, defaultValue, params);
                    if (!ko.isObservable(self.checked)) {
                        self.checked = ko.observable(!!ko.unwrap(self.checked));
                    }
                }
                return new CheckboxViewModel(params, componentInfo);
            }
        },
        template: {element: 'custom-checkbox-tmpl'}
    });
    ko.components.register('my-icon', {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                function CheckboxViewModel(params, componentInfo) {
                    var defaultValue = {
                        contentType: "",
                        clickFun: function () {}
                    };
                    var self = $.extend(this, defaultValue, params);
                    self.icon = ko.observable("fa fa-file");
                    if (ko.unwrap(self.contentType)) {
                        if (ko.unwrap(self.contentType) === global.directory_contenttype) {
                            self.icon("fa fa-folder-open-o");
                        } else if (new RegExp("^audio.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-sound-o");
                        } else if (new RegExp("^image.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-image-o");
                        } else if (new RegExp("^application\/pdf.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-pdf-o");
                        } else if (new RegExp("^text\/html.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-code-o");
                        } else if (new RegExp("^application\/x-javascript.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-code-o");
                        } else if (new RegExp("^application\/zip.*$").test(ko.unwrap(self.contentType))) {
                            self.icon("fa fa-file-zip-o");
                        } else {
                            self.icon("fa fa-file-o");
                        }
                    }
                }
                return new CheckboxViewModel(params, componentInfo);
            }
        },
        template: {element: 'my-icon-tmpl'}
    });

    ko.components.register('drop-down', {require: 'static/visitor/components-common/drop-down/dropdown.js'});
    ko.components.register('custom-table', {require: 'static/visitor/components-common/custom-table/custom-table.js'});
    ko.components.register('menu-list', {require: 'static/visitor/components-common/menu/menu-list.js'});
    ko.components.register('small-head', {require: 'static/visitor/components-common/small/small-head.js'});

    ko.components.register('clock', {require: 'static/visitor/components-common/clock/clock.js'});

    ko.components.register('menu-panel', {require: 'static/visitor/components-common/menu/menu-panel.js'});
    ko.components.register('menu-nav-tabs', {require: 'static/visitor/components-common/menu/menu-nav-tabs.js'});

})(this, ko);
