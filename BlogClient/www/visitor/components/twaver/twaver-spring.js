//http://doc.servasoft.com/twaver-document-center/recommended/twaver-html5-guide/quick-start/
(function (global) {
    define(["knockout", "text!./twaver-spring.xhtml", "css!./twaver-spring.css"], function (ko, pageView) {
        function TwaverStringModel(params, componentInfo) {
            var defaultValue = {
                nodeList: [
                    {id: 001, name: '1号', location: {x: 100, y: 100}},
                    {id: 002, name: '2号', location: {x: 300, y: 100}},
                    {id: 003, name: '3号', location: {x: 100, y: 300}},
                    {id: 004, name: '4号', location: {x: 300, y: 300}}
                ],
                linkList: [
                    {id: 1, formId: 001, toId: 002, name: '1号到2号'},
                    {id: 2, formId: 001, toId: 003, name: '1号到号'},
                    {id: 3, formId: 001, toId: 004, name: '1号到4号'},
                    {id: 4, formId: 002, toId: 004, name: '2号到4号'},
                    {id: 5, formId: 002, toId: 003, name: '2号到3号'},
                    {id: 6, formId: 003, toId: 004, name: '3号到4号'}
                ]
            };
            var self = $.extend(this, defaultValue, params);
            self.nodeMap = {};
            self.box = new twaver.ElementBox();
            self.network = new twaver.vector.Network(self.box);
            self.tree = new twaver.controls.Tree(self.box);
            self.popupMenu = new twaver.controls.PopupMenu(self.network);

            function initNetwork() {
                let centerDiv = document.getElementById('twaverSpringId');
                let view = self.network.getView();
                view.style.backgroundColor = "#f3f3f3";
                view.style.width = "100%";
                view.style.height = "100%";
                centerDiv.appendChild(view);
                self.network.adjustBounds({x: 0, y: 0, width: 1000, height: 500});
            }
            function initDataBox() {
                for (let i = 0; i < self.nodeList.length; i++) {
                    let node = new twaver.Node(self.nodeList[i]);
//                    node.setImage("protein");
//                    node.setIcon("protein");
                    self.box.add(node);
                    self.nodeMap[self.nodeList[i].id] = node;
                }
                for (let i = 0; i < self.linkList.length; i++) {
                    let li = self.linkList[i];
                    let link = new twaver.Link(self.nodeMap[li.formId], self.nodeMap[li.toId]);
                    link.setName(li.name);
                    if (li.id === 3) {
                        link.setStyle('label.position', 'topleft.topleft');
//                        link.setStyle('arrow.from', true);
//                        link.setStyle('arrow.to', true);
                    }
                    link.setStyle('link.width', 2);
                    self.box.add(link);
                }
            }
            function initTree() {
                var treeDom = self.tree.getView();
                var westDiv = document.getElementById('twaverTreeId');
                treeDom.style.width = "100%";
                treeDom.style.height = "100%";
                treeDom.style.cursor = "pointer";
                westDiv.appendChild(treeDom);
                self.tree.setVisibleFunction(function (element) {
                    return element instanceof twaver.Node || element instanceof twaver.Link;
                });
                self.tree.expandAll();
            }
            function initPopupMenu() {
                self.popupMenu.setMenuItems([
                    {label: '添加设备'},
                    {label: '删除设备'},
                    {separator: true},
                    {label: '详细信息'}
                ]);
            }

            (function () {
                initNetwork();
                initDataBox();
                initTree();
                initPopupMenu();
            })();

        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new TwaverStringModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
