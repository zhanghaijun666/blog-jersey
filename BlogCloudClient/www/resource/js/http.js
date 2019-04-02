(function () {
    window.sendRequest = function (url, options, callback, erroCallback) {
        var defaultValue = {
            cmd: "GET",
            async: true,
            accept: "application/x-protobuf",
            timeout: isDebug() ? 0 : 2000,
            type: "application/x-protobuf;charset=UTF-8"
        };
        options = $.extend({}, defaultValue, options);
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    var result;
                    if (xhr.responseType === 'text') {
                        result = xhr.responseText;
                    } else if (xhr.responseType === 'document') {
                        result = xhr.responseXML;
                    } else if (xhr.responseType === "arraybuffer") {
                        result = new Uint8Array(xhr.response);
                    } else {
                        result = xhr.response;
                    }
                    if (callback) {
                        callback(result, xhr);
                    }
                } else if (erroCallback) {
                    erroCallback(xhr);
                }
            }
        };
        xhr.upload.onprogress = function (e) {
            if (e.lengthComputable) {
                console.log("进度：" + ((e.loaded / e.total) * 100) + "%");
            }
        };
        xhr.open(options.cmd, url, options.async);
        xhr.timeout = options.timeout;
        xhr.setRequestHeader("Accept", options.accept);
        if (options.type) {
            xhr.setRequestHeader("Content-Type", options.type);
        }
        if (options.data) {
            xhr.send(options.data);
        } else {
            xhr.send();
        }
    }
})();



