(function () {
    //https://blog.csdn.net/cuixiping/article/details/7911198
    //https://www.jianshu.com/p/5b78ea20c92b
    //https://blog.csdn.net/xiaoshuo566/article/details/81702690

    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");
        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            var uploadPool = new UploadFilePool(fileToUpload);
            uploadPool.startAll();
//            uploadFile("upload/file/daa", fileToUpload);
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


    function selfile() {
        const LENGTH = 1024 * 1024 * 10;//每次上传的大小
        var file = document.getElementsByName('video')[0].files[0];//文件对象
        var totalSize = file.size;//文件总大小
        var start = 0;//每次上传的开始字节
        var end = start + LENGTH;//每次上传的结尾字节
        var fd = null//创建表单数据对象
        var blob = null;//二进制对象
        var xhr = null;//xhr对象
        while (start < totalSize) {
            fd = new FormData();//每一次需要重新创建
            xhr = new XMLHttpRequest();//需要每次创建并设置参数
            xhr.open('POST', '13-slice-upload.php', false);
            blob = file.slice(start, end);//根据长度截取每次需要上传的数据
            fd.append('video', blob);//添加数据到fd对象中
            xhr.send(fd);//将fd数据上传

            //重新设置开始和结尾
            start = end;
            end = start + LENGTH;
        }
    }

})();
