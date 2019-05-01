(function (global) {
    define(["text!./message.xhtml", "css!./message.css"], function (pageView) {
        function MessageModel(params, componentInfo) {
            var defaultValue = {
            };
            var self = $.extend(this, defaultValue, params);
            self.contactsList = ko.observableArray([]);

            self.getContactsList = function () {
                self.contactsList.push(new User({username: "sssss", nickname: "王者灭霸归来"}));
                self.contactsList.push(new User({username: "zhangsan1", nickname: "张三1"}));
                self.contactsList.push(new User({username: "zhangsan2", nickname: "张三2"}));
                self.contactsList.push(new User({username: "zhangsan3", nickname: "张三3"}));
                self.contactsList.push(new User({username: "zhangsan4", nickname: "张三4"}));
                self.contactsList.push(new User({username: "zhangsan5", nickname: "张三5"}));
                self.contactsList.push(new User({username: "zhangsan6", nickname: "张三6"}));
                self.contactsList.push(new User({username: "zhangsan7", nickname: "张三7"}));
                self.contactsList.push(new User({username: "zhangsan8", nickname: "张三8"}));
            };



            self.getContactsList();
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MessageModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
