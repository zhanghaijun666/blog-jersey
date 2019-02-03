(function (global) {
    define(["knockout", "text!./clock.xhtml", "css!./clock.css"], function (ko, pageView) {
        function ClockModel(params, componentInfo) {
            var self = this;
            self.imgArray = componentInfo.element.getElementsByTagName("img");

            function renderingPage() {
                self.nowDate = new Date();
                var prevtime = toZero(self.nowDate.getHours()) + toZero(self.nowDate.getMinutes()) + toZero(self.nowDate.getSeconds());
                for (var i = 0; i < self.imgArray.length; i++) {
                    self.imgArray[i].src = "/static/resource/images/clock/" + prevtime.charAt(i) + ".png";
                }
            }
            setInterval(renderingPage, 1000);
            renderingPage();




            function initClock() {
                var aImg = document.getElementById("clock").getElementsByTagName("img");
                var now = new Date();
                var prevtime = toZero(now.getHours()) + toZero(now.getMinutes()) + toZero(now.getSeconds());
                var nexttime = "";
                var arr = [];
                var timer = null;


                for (var i = 0; i < aImg.length; i++) {
                    aImg[i].src = "./images/" + prevtime.charAt(i) + ".png";
                }
                setInterval(toChange, 1000);
                function toChange() {
                    var date = new Date();
                    nexttime = toZero(date.getHours()) + toZero(date.getMinutes()) + toZero(date.getSeconds());
                    toCom(prevtime, nexttime);
                    prevtime = nexttime;
                }

                function toCom(str1, str2) {
                    //每次清空数组里面的数据
                    arr = [];
                    for (var i = 0; i < str1.length; i++) {
                        if (str1.charAt(i) != str2.charAt(i)) {
                            arr.push(i);
                        }
                    }
                    startMove();
                }
                //上下翻转效果：利用数字高度减少至0再增加回来实现视觉差翻转
                function startMove() {
                    var speed = -4;
                    timer = setInterval(function () {
                        for (var i = 0; i < arr.length; i++) {
                            if (aImg[arr[i]].offsetHeight === 0) {
                                speed = 4;
                                aImg[arr[i]].src = "./images/" + nexttime.charAt(arr[i]) + ".png"
                            }
                            //改变数字高度时默认向底线减少，所以手动改变数字的top向上移动
                            aImg[arr[i]].style.height = aImg[arr[i]].offsetHeight + speed + "px";
                            aImg[arr[i]].style.top = aImg[arr[i]].offsetHeight / 2 - 18 + "px";
                            if (aImg[arr[i]].offsetHeight === 36) {
                                clearInterval(timer);
                            }
                        }
                    }, 10);
                }

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
})(this);
