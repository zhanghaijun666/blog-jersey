(function () {
    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");

        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            uploadFile("upload/file", fileToUpload);
        });

    }, 1000);


    function uploadFile(url, file) {
        url = url || "upload1";
        var xhr = createBinary(url, file);
        //file = document.getElementById("file").files[0];
        var form = new FormData();
        form.append("file", file);
        xhr.send(form);


    }
    function createBinary(url) {
        var xhr = new XMLHttpRequest();
        xhr.open("post", url, true);
        xhr.onload = function () {
            console.log("上传成功！");
        };
        xhr.onerror = function () {
            console.log("上传失败！");
        };
        xhr.upload.onloadstart = function () {
            console.log("开始上传。。。。。。");
        };
        xhr.upload.onprogress = function (evt) {
            console.log(evt.loaded);
            if (evt.lengthComputable) {
                var percentComplete = Math.round(evt.loaded * 100 / evt.total);
                console.log(percentComplete);
            }
        };
        return xhr;
    }
})();
