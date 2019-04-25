(function () {
    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");
        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            var uploadPool = new UploadFilePool(fileToUpload);
            uploadPool.startAll();
        });
    }, 1000);
})();
