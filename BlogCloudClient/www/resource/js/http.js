(function () {
    function sendRequest(url, options, callback, erroCallback) {
        var defaultValue = {
            cmd: "GET",
            async: true,
            accept: "application/x-protobuf",
            timeout: isDebug() ? 0 : 2000,
            "Content-Type": "application/x-protobuf; charset=UTF-8"
        };
        options = $.extend({}, defaultValue, options);
        switch (options.cmd) {
            case "upload":
                window.requestUpload(url, options, callback, erroCallback);
                break;
            default:
                window.requestCommon(url, options, callback, erroCallback);
                break;
        }
    }
    window.requestCommon = function (url, options, callback, erroCallback) {
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
        xhr.open(options.cmd, url, options.async);
        xhr.timeout = options.timeout;
        xhr.setRequestHeader("Accept", options.accept);
        if (options.data) {
            xhr.setRequestHeader("Content-Type", options["Content-Type"]);
            xhr.send(options.data);
        } else if (options.formData) {
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            xhr.send(jQuery.param(options.formdata, false));
        } else {
            xhr.setRequestHeader("Content-Type", options["Content-Type"]);
            xhr.send();
        }
    };
    window.requestUpload = function (url, options, callback, erroCallback) {
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
        // 请求成功回调函数
        xhr.onload = e => {
            console.log('request success');
        };
        // 请求结束
        xhr.onloadend = e => {
            console.log('request loadend');
        };
        // 请求出错
        xhr.onerror = e => {
            console.error('request error');
        };
        // 请求超时
        xhr.ontimeout = e => {
            console.log('request timeout');
        };
        xhr.open("POST", url, options.async);
        xhr.timeout = options.timeout;
        xhr.setRequestHeader("Accept", options.accept);
        xhr.setRequestHeader("Content-Type", "application/cc-update-index");




    };
})();



