(function (exports) {
    exports.blogServer = window.location.protocol + "//" + window.location.host;
    exports.isDebug = function () {
        return /^(localhost|127\.0\.0\.1)/.test(window.location.host);
    };
    exports.getServerUrl = function (url) {
        if (url.startsWith("/")) {
            url = blogServer + url;
        } else {
            url = blogServer + "/" + url;
        }
        return url;
    };
    exports.getRequest = function (url, options, callback, erroCallback) {
        var requestValue = {
            method: "GET",
            async: true,
            accept: "text/plain",
            timeout: isDebug() ? 0 : 2000,
            withCredentials: true,
            "Content-Type": "application/json; charset=UTF-8"
        };
        options = $.extend({}, requestValue, options);
        var xhr = createXMLHttpRequest(options, callback, erroCallback);
        xhr.open(options.method, url, options.async);
        xhr.timeout = options.timeout;
        xhr.setRequestHeader("Accept", options.accept);
        xhr.responseType = options.responseType || options.accept.indexOf("text/") > -1 ? "text" : "arraybuffer";

        if (options.data) {
            xhr.setRequestHeader("Content-Type", options["Content-Type"]);
            xhr.send(options.data);
        } else if (options.formdata) {
            var form = new FormData();
            form.append("file", options.formdata);
            xhr.send(form);
        } else {
            xhr.send();
        }
    };

    function createXMLHttpRequest(options, callback, erroCallback) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    let result;
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
//    xhr.upload.onprogress = function (e) {
//        if (e.lengthComputable) {
//            var percentage = (e.loaded / e.total) * 100;
//            console.log(percentage + "%");
//        }
//    };

//// 请求成功回调函数
//    xhr.onload = e => {
//        console.log('request success');
//    };
//// 请求结束
//    xhr.onloadend = e => {
//        console.log('request loadend');
//    };
//// 请求出错
//    xhr.onerror = e => {
//        console.log('request error');
//    };
//// 请求超时
//    xhr.ontimeout = e => {
//        console.log('request timeout');
//    };
        return xhr;
    }
})(this);
