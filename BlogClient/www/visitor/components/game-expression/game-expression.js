(function (global) {
    define(["text!./game-expression.xhtml", "css!./game-expression.css"], function (pageView) {
        function GameExpressionViewModel(params, componentInfo) {
            var self = this;

            self.imgArray = [
                '/static/resource/images/expression/1.png',
                '/static/resource/images/expression/2.png',
                '/static/resource/images/expression/3.png',
                '/static/resource/images/expression/4.png',
                '/static/resource/images/expression/5.png',
                '/static/resource/images/expression/6.png',
                '/static/resource/images/expression/7.png',
                '/static/resource/images/expression/8.png',
                '/static/resource/images/expression/9.png',
                '/static/resource/images/expression/10.png',
                '/static/resource/images/expression/11.png'
            ];
            self.Score = ko.observable(0);//得分
            self.lose = ko.observable(0);//失分
            self.buttonName = ko.observable("开始游戏");
            self.sdNum = 1;//图片运行速度
            self.onOff = true;//一个开关

            self.divElement = null;
            self.imgElement = null;

            self.starGame = function () {
                if (ko.unwrap(self.buttonName) !== "开始游戏") {
                    return;
                }
                self.buttonName("游戏进行中......");
                self.Score(0);
                self.lose(0);

                self.divElement = document.querySelectorAll('.expression-bom')[0].getElementsByTagName('div')[1];
                var width = parseInt(getStyle(self.divElement, 'width'));	//获取图片活动的宽度
                var height = parseInt(getStyle(self.divElement, 'height')) - 24;//获取图片活动的高度，并且减去本身的高度
                self.imgElement = document.querySelectorAll('.expression-bom')[0].getElementsByTagName('img')[0];
                self.imgElement.style.display = 'block';	//让QQ表情显示出来
                aotu(width, height);
                self.imgElement.onclick = function () {
                    self.onOff = false;		//如果为false，哪怕QQ表情掉在最下面，也不会获得失分
                    self.imgElement.src = '/static/resource/images/expression/qq.png';	//点击最后，QQ表情变成哭的样子
                    self.sdNum = self.sdNum + 0.5;		//每一次点击，QQ表情下降的速度增加0.5
                    shake(self.imgElement, 'left', function () {	//点击之后开始抖
                        self.imgElement.style.top = '0px';	//然后QQ表情恢复到0px的位置
                        self.Score(ko.unwrap(self.Score) + 1);
                        aotu(width, height);	//继续执行代码
                    });
                };
            };

            function aotu(divWidth, divHeight) {
                self.onOff = true;//如果为true就表示QQ表情落到最下面时会获得失分
                self.imgElement.src = self.imgArray[Math.round(Math.random() * (self.imgArray.length - 1))];
                self.imgElement.style.left = Math.round(Math.random() * (divWidth - 24)) + 'px';			//让img随机在屏幕的位置
                doMove(self.imgElement, 'top', self.sdNum, divHeight, function () {//落下去的时候
                    if (self.onOff === true) {//如果为true就表示QQ表情落到最下面时会获得失分
                        shake(self.divElement, 'top', function () {	//掉下去后就开始抖整个窗口
                            self.imgElement.style.top = '0px';	//让QQ表情回到0px的位置
                            self.lose(ko.unwrap(self.lose) + 1);
                            if (ko.unwrap(self.lose) === 10) {			//如果失去分大于了10
                                //self.imgElement.style.display = 'none';	//如果游戏结束，将QQ表情清除了
                                self.buttonName("开始游戏");
                                alert('游戏结束，你共获得' + ko.unwrap(self.Score) + '分');
                            } else {
                                aotu(divWidth, divHeight);
                            }
                        });
                    }
                });
            }


            //------ utils ------start ------
            /* 
             Xie Kai's JavaScript Document.
             After Miaowei Classroom learning, completely write their own code library.
             */
            /*---------$()使用  开始---------*/
            function $(v) {
                if (typeof v === 'function') {	//如果等于函数，就在页面加载完之后执行代码
                    window.onload = v;
                } else if (typeof v === 'string') {	//如果等于字符串，那么就查找id
                    document.querySelectorAll(v)[0];
//                    return document.getElementById(v);
                } else if (typeof v === 'object') {	//如果等于对象，就直接返回对象
                    return v;
                }
            }
            /*---------$()使用  结束---------*/
            /*---------getStyle()，盒子计算后显示结果  开始---------*/

            //获取计算后的盒子样式，obj填写盒子，attr填写宽或者高，包括透明度等等
            function getStyle(obj, attr) {
                return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj)[attr];
            }
            /*---------getStyle()，盒子计算后显示结果  结束---------*/
            /*---------doMove()，可以动的盒子  开始---------*/

            //obj是盒子  attr是要走的宽或者高  dir是步长，target是到达的位置，endfn是可以继续执行的函数
            function doMove(obj, attr, dir, target, endFn) {
                dir = parseInt(getStyle(obj, attr)) < target ? dir : -dir;
                clearInterval(obj.timer);
                obj.timer = setInterval(function () {
                    var speed = parseInt(getStyle(obj, attr)) + dir;			// 步长
                    if (speed > target && dir > 0 || speed < target && dir < 0) {
                        speed = target;
                    }
                    obj.style[attr] = speed + 'px';

                    if (speed === target) {
                        clearInterval(obj.timer);
                        /*
                         if ( endFn ) {
                         endFn();
                         }
                         */
                        endFn && endFn();
                    }
                }, 30);
            }
            /*---------doMove()，可以动的盒子  结束--------*/
            /*---------shake()，可以抖动的盒子  开始---------*/
            //抖函数，obj是盒子，attr是top或者left，endFn是可以执行另外的函数 shake( this, 'left');
            function shake(obj, attr, endFn) {
                var pos = parseInt(getStyle(obj, attr));
                var arr = [];			// 20, -20, 18, -18 ..... 0
                var num = 0;
                var timer = null;

                for (var i = 20; i > 0; i -= 2) {
                    arr.push(i, -i);
                }
                arr.push(0);
                if (obj.onOff !== true) {	//开关如果不等于true，就执行下面的代码 注：避免重复使用抖
                    clearInterval(obj.shake);
                    obj.shake = setInterval(function () {
                        obj.onOff = true;	//开始执行的时候，一直为true，但是这里的true是关闭
                        obj.style[attr] = pos + arr[num] + 'px';
                        num++;
                        if (num === arr.length) {
                            clearInterval(obj.shake);
                            endFn && endFn();
                            obj.onOff = false;	//执行完之后，就变成flase，然后又可以开始点击
                        }
                    }, 50);
                }
            }

            /*---------shake()，可以抖动的盒子  结束---------*/


            /*---------hide()，渐隐 obj是盒子，sec是渐隐时间，endFn是继续执行的函数---------*/
            function hide(obj, cy, sec, endFn) {
                var timer = null;
                var fadeNum = Number(getStyle(obj, 'opacity') * 100);
                var fadeNum1 = Number(getStyle(obj, 'opacity'));

                timer = setInterval(function () {
                    fadeNum -= 10;
                    fadeNum1 -= 0.1;
                    obj.style.filter = "alpha(opacity=" + fadeNum + ")";
                    obj.style['-moz-opacity'] = fadeNum1;
                    obj.style['-khtml-opacity'] = fadeNum1;
                    obj.style.opacity = fadeNum1;
                    if (fadeNum === cy * 100 || fadeNum1 === cy) {
                        clearInterval(timer);
                        endFn && endFn();
                    }
                }, sec);
            }

            /*---------hide()，渐隐---------*/



            /*---------out()，渐出 obj是盒子，sec是渐出时间，endFn是继续执行的函数---------*/
            function out(obj, cy, sec, endFn) {
                var timer = null;
                var fadeNum = Number(getStyle(obj, 'opacity') * 100);
                var fadeNum1 = Number(getStyle(obj, 'opacity'));
                timer = setInterval(function () {
                    fadeNum += 10;
                    fadeNum1 += 0.1;
                    obj.style.filter = "alpha(opacity=" + fadeNum + ")";
                    obj.style['-moz-opacity'] = fadeNum1;
                    obj.style['-khtml-opacity'] = fadeNum1;
                    obj.style.opacity = fadeNum1;
                    if (fadeNum === cy * 100 || fadeNum1 === cy) {
                        clearInterval(timer);
                        endFn && endFn();
                    }
                }, sec);
            }

            /*---------out()，渐出---------*/
            //------ utils ------end ------





        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new GameExpressionViewModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);