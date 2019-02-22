(function (exports) {
    var defaultProps = {
        dialogClass: "",
        header: "",
        headerTemplate: "default-dialog-header",
        isHideHeader: false,
        bodyTemplate: "",
        footerTemplate: "default-dialog-footer",
        isHideFoot: false,
        isCloseDialog: true,
        success: function () {}
    };
    function showDialog(options) {
        var dialogEle = "<div class=\"modal fade\" tabIndex=\"-1\" role=\"dialog\">" +
                "	<div class=\"modal-dialog\" role=\"document\" data-bind=\"css: $data.dialogClass\" >" +
                "		<div class=\"modal-content\" >" +
                "			<!-- ko ifnot: $data.isHideHeader -->" +
                "			<div class=\"modal-header\">" +
                "				<!-- ko template: {name: $data.headerTemplate, data: $data } --> <!-- /ko -->" +
                "			</div>" +
                "			<!-- /ko -->" +
                "			<div class=\"modal-body\" style=\"padding: 0px;\">" +
                "                           <!-- ko if: ko.components.isRegistered($data.bodyTemplate) -->"+
                "                           <!-- ko component: {name: $data.bodyTemplate, params: $data } --><!-- /ko -->"+
                "                           <!-- /ko -->"+
                "                           <!-- ko ifnot: ko.components.isRegistered($data.bodyTemplate) -->"+
                "                           <!-- ko template: {name: $data.bodyTemplate, data: $data} --><!-- /ko -->"+
                "                           <!-- /ko -->"+
                "			</div>" +
                "			<!-- ko ifnot: $data.isHideFoot -->" +
                "			<div class=\"modal-footer\">" +
                "				<!-- ko template: {name: $data.footerTemplate, data: $data } --> <!-- /ko -->" +
                "			</div>" +
                "			<!-- /ko -->" +
                "		</div>" +
                "	</div>" +
                "</div>";
        var container = document.createElement("div");
        container.innerHTML = dialogEle;
        document.body.appendChild(container);
        var dialogRoot = new DialogRoot(container, options);
        ko.applyBindings(dialogRoot, container);
        dialogRoot.dialogRef.modal("show");
    }

    function DialogRoot(container, options) {
        var self = this;
        self = $.extend(this, defaultProps, options);
        self.dialogRef = $(container).find(".modal");
        self.dialogRef.on('hidden.bs.modal', function () {
            document.body.removeChild(container);
            if (self.onHide) {
                self.onHide(self);
            }
        });
        self.success = function () {
            if (self.isCloseDialog) {
                self.dialogRef.modal("hide");
            }
            if (options.success) {
                options.success(self);
            }
        };
    }
    exports.showDialog = showDialog;
})(this);
