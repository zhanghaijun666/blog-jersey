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
    ko.components.register('drop-down', {require: 'static/visitor/components-common/drop-down/dropdown.js'});
    ko.components.register('custom-table', {require: 'static/visitor/components-common/custom-table/custom-table.js'});
    ko.components.register('menu-list', {require: 'static/visitor/components-common/menu/menu-list.js'});
    ko.components.register('small-head', {require: 'static/visitor/components-common/small/small-head.js'});

    ko.components.register('clock', {require: 'static/visitor/components-common/clock/clock.js'});
    
    ko.components.register('menu-panel', {require: 'static/visitor/components-common/menu/menu-panel.js'});
    ko.components.register('menu-nav-tabs', {require: 'static/visitor/components-common/menu/menu-nav-tabs.js'});

})(this, ko);
