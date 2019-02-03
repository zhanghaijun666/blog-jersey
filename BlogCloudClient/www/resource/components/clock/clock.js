(function () {
    define(["text!./clock.xhtml", "css!./clock.css"], function (pageView) {
        function ClockModel(params, componentInfo) {
            var self = this;
            self.imgArray = componentInfo.element.getElementsByTagName("img");
            var nowDate = new Date();
            self.prevtime = toZero(nowDate.getHours()) + toZero(nowDate.getMinutes()) + toZero(nowDate.getSeconds());
            for (var i = 0; i < self.imgArray.length; i++) {
                self.imgArray[i].src = "/static/resource/images/clock/" + self.prevtime.charAt(i) + ".png";
            }

            function renderingPage() {
                var newNow = new Date();
                var newTime = toZero(newNow.getHours()) + toZero(newNow.getMinutes()) + toZero(newNow.getSeconds());
                toCom(self.prevtime, newTime);
                self.prevtime = newTime;
            }
            setInterval(renderingPage, 1000);

            //每次清空数组里面的数据
            function toCom(oldTime, newTime) {
                var arr = [];
                for (var i = 0; i < oldTime.length; i++) {
                    if (oldTime.charAt(i) !== newTime.charAt(i)) {
                        arr.push(i);
                    }
                }
                startMove(arr, newTime);
            }
            //上下翻转效果：利用数字高度减少至0再增加回来实现视觉差翻转
            function startMove(arr, newTime) {
                var speed = -4;
                timer = setInterval(function () {
                    for (var i = 0; i < arr.length; i++) {
                        if (self.imgArray[arr[i]].offsetHeight === 0) {
                            speed = 4;
                            self.imgArray[arr[i]].src = "/static/resource/images/clock/" + newTime.charAt(arr[i]) + ".png"
                        }
                        //改变数字高度时默认向底线减少，所以手动改变数字的top向上移动
                        self.imgArray[arr[i]].style.height = self.imgArray[arr[i]].offsetHeight + speed + "px";
                        self.imgArray[arr[i]].style.top = self.imgArray[arr[i]].offsetHeight / 2 - 18 + "px";
                        if (self.imgArray[arr[i]].offsetHeight === 36) {
                            clearInterval(timer);
                        }
                    }
                }, 10);
            }
            //补0操作，保证数字一直为六位数
            function toZero(num) {
                if (num < 10) {
                    return num = "0" + num;
                } else {
                    return num = num + "";
                }
            }
        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new ClockModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})();
