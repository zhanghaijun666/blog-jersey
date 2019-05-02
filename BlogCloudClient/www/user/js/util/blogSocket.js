function BlogSocket() {
    if (!("WebSocket" in window)) {
        alert("您的浏览器不支持 WebSocket!");
    }
    var blogSocket = new WebSocket("ws://" + getServerUrl("/socket"));
    blogSocket.binaryType = "arraybuffer";
    blogSocket.onopen = function (event) {
        console.log("webSocket 连接已建立,{}", event);
    };
    blogSocket.onmessage = function (event) {
        console.log(event.data + "webSocket 消息");
    };
    blogSocket.onclose = function (event) {
        console.log("webSocket 连接已关闭,{}", event);
    };
    blogSocket.onerror = function (event) {
        console.log("webSocket 异常,{}", event);
    };
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        blogSocket.close();
    };
}
