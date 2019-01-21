(function (global) {
    define(["knockout", "text!./protein.html"], function (ko, pageView) {
        function PageProteinModel(params, componentInfo) {
            var self = this;
            self.proteins = ko.observableArray([]);
            
            self.importProteins = function(data,event){
                var file = event.target.files[0];
                $(event.target).parent("form")[0].reset();
                ajax({url:"/proteins/import",method:"POST",formData:{file: file}, accept:"application/x-protobuf",success: function(){
                    self.getProteins();
                }});
            };
            
            self.getProteins = function(){
                ajax({url:"/proteins",method:"GET", accept:"application/x-protobuf",success: function(data){
                    var proteins = pbs.ProteinList.decode(data);
                    self.proteins(proteins.items);
                }});
            };
            
            self.getProteins();
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new PageProteinModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
