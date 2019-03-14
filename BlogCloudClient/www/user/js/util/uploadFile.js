(function () {
    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");

        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            console.log("---------------");
            console.log(fileToUpload);
            console.log("---------------");
        });

    }, 1000);


    function uploadFile(url, file) {
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
        xhr.upload.onprogress = function(evt){
            if (evt.lengthComputable) {
                var percentComplete = Math.round(evt.loaded * 100 / evt.total);
                console.log(percentComplete);
            }
        };
        return xhr;
    }
})();
