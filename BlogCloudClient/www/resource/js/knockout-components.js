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

})(this, ko);
