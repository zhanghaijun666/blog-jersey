function BlogSocket() {
    if (!("WebSocket" in window)) {
        alert("您的浏览器不支持 WebSocket!");
    }
    var self = this;
    var blogSocket = new WebSocket((/^https/.test(location.protocol) ? "wss://" : "ws://") + window.location.host + "/socket");
//    self.blogSocket.binaryType = "arraybuffer";
    self.blogSocket.onopen = function (event) {
        self.blogSocket.send("发送数据");
        console.log("webSocket 连接已建立,{}", event);
    };
    self.blogSocket.onmessage = function (event) {
        console.log(event.data + "webSocket 消息");
    };
    self.blogSocket.onclose = function (event) {
        console.log("webSocket 连接已关闭,{}", event);
    };
    self.blogSocket.onerror = function (event) {
        console.log("webSocket 异常,{}", event);
    };
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        self.blogSocket.close();
    };
}
BlogSocket.prototype.send = function (message) {
    this.blogSocket.send(message);
};
