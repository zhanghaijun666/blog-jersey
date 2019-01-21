//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {

    ko.components.register('page-home', {require: 'static/visitor/components/page-home/home.js'});
    ko.components.register('home-nav-tabs', {require: 'static/visitor/components/home-nav-tabs/home-nav-tabs.js'});

    /* common or utils component start */
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
    ko.components.register('drop-down', {require: 'static/visitor/components/drop-down/dropdown.js'});
    ko.components.register('custom-table', {require: 'static/visitor/components/custom-table/custom-table.js'});

    /* common or utils component end */
    /* action component start */
    ko.components.register('user-list', {require: 'static/visitor/components/user-list/user.js'});
    ko.components.register('twaver-spring', {require: 'static/visitor/components/twaver/twaver-spring.js'});

    /* action component end */

})(this, ko);
