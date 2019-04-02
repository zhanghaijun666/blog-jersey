(function () {
    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");

        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            uploadFile("upload/file/daa", fileToUpload);
        });

    }, 1000);

    function uploadFile(url, file) {
        if (!url) {
            return;
        }
        var fileItemList = bcstore.TreeUpdateItemList.create();
        for (var i = 0; i < file.files.length; i++) {
            var fileItem = file.files[0];
            if (fileItem instanceof Blob) {
                var treeItem = bcstore.TreeUpdateItem.create();
                treeItem.fileName = fileItem.name;
                treeItem.size = fileItem.size;
                fileItemList.item.push(treeItem);
            }
        }
        var form = new FormData();
        form.append("file", file.files[0]);
        //application/x-www-form-urlencoded
        //application/cc-update-index
        
        sendRequest(url, {cmd: "POST", accept: "application/x-protobuf",
//            data: bcstore.TreeUpdateItemList.encode(fileItemList).finish(),
            formData: file
        }, function (data) {
            console.log(bcstore.RspInfoList.decode(data).code);
        });
    }
})();
