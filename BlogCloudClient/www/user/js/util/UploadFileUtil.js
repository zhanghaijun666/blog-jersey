window.hash_chunksize = 8 * 1024 * 1024;
function UploadFilePool(inputFile) {
    var self = this;
    self.syncUploadNumber = 3;
    self.fileList = [];
    var files = inputFile && inputFile.files ? inputFile.files : [];
    for (var i = 0; i < files.length; i++) {
        self.fileList.push(new UploadFile(files[i]));
    }
}
UploadFilePool.prototype.startAll = function () {
    var uploadIndex = 0;
    while (uploadIndex >= this.syncUploadNumber || uploadIndex < this.fileList.length) {
        this.fileList[uploadIndex].readerFile();
        uploadIndex = uploadIndex + 1;
    }
};
UploadFilePool.prototype.abortAll = function () {

};
UploadFilePool.prototype.successAll = function () {

};
function UploadFile(file) {
    var self = this;
    self.file = file;
    self.blobList = [];
    self.total = 0;

    self.reader = new FileReader();
}
UploadFile.prototype.readerFile = function () {
    var self = this;
    if (!(self.file instanceof Blob)) {
        return;
    }
    self.reader.onloadend = function (evt) {
        var arraybuffer = evt.target.result;
        self.total = evt.target.result.byteLength || evt.total;
        var hashQueue = [];
        for (var startIndex = 0; startIndex < self.total; startIndex += window.hash_chunksize) {
            var newBuf = arraybuffer.slice(startIndex, Math.min(startIndex + window.hash_chunksize, self.total));
            crypto.subtle.digest("SHA-1", newBuf).then(function (hash) {
//                Rusha.createHash().update(hash).digest('hex')
//                new Rusha().digest(hash);
                console.log(new Rusha().digest(hash));
            }, function (error) {
                console.error("Unable to hash:" + error);
            });
            hashQueue.push({data: newBuf});
        }
        self.blobList = hashQueue;

    };
    self.reader.readAsArrayBuffer(self.file);
};
UploadFile.prototype.abort = function () {

};
