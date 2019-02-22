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
    ko.components.register('drop-down', {require: 'static/resource/components/drop-down/dropdown.js'});
    ko.components.register('custom-table', {require: 'static/resource/components/custom-table/custom-table.js'});
    ko.components.register('menu-list', {require: 'static/resource/components/menu/menu-list.js'});
    ko.components.register('small-head', {require: 'static/resource/components/small/small-head.js'});

    ko.components.register('clock', {require: 'static/resource/components/clock/clock.js'});
    
    ko.components.register('menu-panel', {require: 'static/resource/components/menu/menu-panel.js'});
    ko.components.register('menu-nav-tabs', {require: 'static/resource/components/menu/menu-nav-tabs.js'});

})(this, ko);
