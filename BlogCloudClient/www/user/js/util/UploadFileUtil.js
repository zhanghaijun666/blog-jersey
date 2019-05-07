/* global Blob, simulateClick */
(function (exports) {
    exports.hash_chunksize = 8 * 1024 * 1024;
    exports.uploadAllFile = function (fullPath, callback, isMultiple, accept) {
        var fileToUpload = document.createElement("input");
        fileToUpload.setAttribute("type", "file");
        fileToUpload.setAttribute("style", "display: none");
        if (isMultiple) {
            fileToUpload.setAttribute("multiple", "multiple");
        }
        if (accept) {
            fileToUpload.setAttribute("accept", accept);//"image/*"
        }
        $("#not_display_lements_id").empty();
        document.getElementById('not_display_lements_id').appendChild(fileToUpload);
        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            if (fileToUpload.files.length === 0) {
                return;
            }
            var file = fileToUpload.files[0];
            getRequest("/file/upload/" + new FileUrl(fullPath).getChild(file.name).getOriginPath(), {method: "POST", type: "application/x-protobuf", accept: "application/x-protobuf", data: file}, function (data) {
                var rspInfo = bcstore.RspInfo.decode(data);
                if (isFunction(callback)) {
                    callback(rspInfo);
                }
            });
        });
        simulateClick(fileToUpload);
    };
})(this);


//(function () {
//    setTimeout(function () {
//        var fileToUpload = document.getElementById("fileToUpload");
//        fileToUpload.addEventListener("change", function (evt) {
//            evt.preventDefault();
//            var uploadPool = new UploadFilePool(fileToUpload);
//            uploadPool.startAll();
//        });
//    }, 1000);
//})();
//function UploadFilePool(inputFile) {
//    var self = this;
//    self.syncUploadNumber = 20;
//    self.fileList = [];
//    var files = inputFile && inputFile.files ? inputFile.files : [];
//    for (var i = 0; i < files.length; i++) {
//        self.fileList.push(new UploadFile(files[i]));
//    }
//}
//UploadFilePool.prototype.startAll = function () {
//    var uploadIndex = 0;
//    while (uploadIndex >= this.syncUploadNumber || uploadIndex < this.fileList.length) {
//        this.fileList[uploadIndex].readerFile();
//        uploadIndex = uploadIndex + 1;
//    }
//};
//UploadFilePool.prototype.abortAll = function () {
//
//};
//UploadFilePool.prototype.successAll = function () {
//
//};
//function UploadFile(file) {
//    var self = this;
//    self.file = file;
//    self.blobList = [];
//    self.total = 0;
//
//    self.reader = new FileReader();
//}
//UploadFile.prototype.readerFile = function () {
//    var self = this;
//    if (!(self.file instanceof Blob)) {
//        return;
//    }
//    self.sendUplodFile();
////    self.reader.onloadend = function (evt) {
////        var arraybuffer = evt.target.result;
////        self.total = evt.target.result.byteLength || evt.total;
////        self.blobList = [];
////        for (var startIndex = 0; startIndex < self.total; startIndex += window.hash_chunksize) {
////            self.blobList.push({
////                data: arraybuffer.slice(startIndex, Math.min(startIndex + window.hash_chunksize, self.total)),
////                blobIndex: startIndex
////            });
////        }
////        console.log(self.blobList.length);
////        crypto.subtle.digest("SHA-1", self.blobList[0].data).then(function (hash) {
////            self.blobList[0].hash = new Rusha().digest(hash);
//////                Rusha.createHash().update(hash).digest('hex');
////            console.log(new Rusha().digest(hash));
////        }, function (error) {
////            console.error("Unable to hash:" + error);
////        });
////        setTimeout(function () {
////            self.sendUplodFile();
////        }, 50);
////    };
////    self.reader.readAsArrayBuffer(self.file);
//};
//UploadFile.prototype.abort = function () {
//
//};
//UploadFile.prototype.sendUplodFile = function () {
//    var self = this;
//    console.log(self);
//    getRequest("/file/upload/default/" + bcstore.GtypeEnum.User + "/" + RootView.user().userId + "/directory/" + self.file.name, {method: "POST", type: "application/x-protobuf", accept: "application/x-protobuf", data: self.file}, function (data) {
//        var rspInfo = bcstore.RspInfo.decode(data);
//        toastShowCode(rspInfo.code);
//    });
//};
