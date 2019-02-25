const server = window.location.protocol + "//" + window.location.host;

function getServerUrl(url) {
    if (url.startsWith("/")) {
        url = server + url;

    } else {
        url = server + "/" + url;
    }
    return url;
}

var requestValue = {
    method: "GET",
    async: true,
    accept: "text/plain",
    timeout: 2000,
    withCredentials: true,
    "Content-Type": "application/json; charset=UTF-8"
};

function getRequest(url, options, callback, erroCallback) {
    var xhr = new XMLHttpRequest();
    xhr.open(options.method || requestValue.method, url, options.async || requestValue.async);
    let accept = options.accept || requestValue.accept;
    xhr.timeout = options.timeout || requestValue.timeout;
    xhr.setRequestHeader("Accept", accept);
    xhr.responseType = options.responseType || accept.indexOf("text/") > -1 ? "text" : "arraybuffer";

    if (options.data) {
        xhr.setRequestHeader("Content-Type", options.type || requestValue["Content-Type"]);
        xhr.send(options.data);
    } else if (options.formData) {
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send(jQuery.param(options.formdata, false));
    } else {
        xhr.send();
    }

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




}
