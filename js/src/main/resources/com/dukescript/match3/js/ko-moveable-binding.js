ko.bindingHandlers['moveable'] = {
    init: function (element, valueAccessor, allBindingsAccessor, data, context) {
        var startPosition = {};
        startPosition.element = element;
        var handler = valueAccessor().bind(data);

        var mousehandler = function (event) {

            if (event.type === "mousedown" || event.type === "touchstart") {
                startPosition.x = event.clientX;
                startPosition.y = event.clientY;
                if (event.type === "touchstart") {
                    startPosition.x = event.changedTouches[0].pageX;
                    startPosition.y = event.changedTouches[0].pageY;
                }
            } else if (event.type === "mouseout" || event.type === "touchmove") {
                if (typeof (startPosition.x) !== 'undefined') {
                    var newX = event.clientX;
                    var newY = event.clientY;
                    var mouseout = event.type === "mouseout";
                    event.pos=context.$index();
                    if (event.type === "touchmove") {
                        newX = event.changedTouches[0].pageX;
                        newY = event.changedTouches[0].pageY;
                        var element = document.elementFromPoint(newX, newY);
                        mouseout = element !== startPosition.element;
                    }
                    if (mouseout) {
                        var deltaX = startPosition.x - newX;
                        var deltaY = startPosition.y - newY;

                        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX > 0) {
                            event.dir = "left";
                            handler(data, event);
                        } else if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX < 0) {
                            event.dir = "right";
                            handler(data, event);
                        } else if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY > 0) {
                            event.dir = "up";
                            handler(data, event);
                        } else if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY < 0) {
                            event.dir = "down";
                            handler(data, event);
                        }
                        delete startPosition.x;
                        delete startPosition.y;
                    }

                }
            }
        };
        element.addEventListener('mousedown', mousehandler);
        element.addEventListener('mouseout', mousehandler);
        element.addEventListener('touchstart', mousehandler);
        element.addEventListener('touchmove', mousehandler);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            element.removeEventListener('mousedown', mousehandler);
            element.removeEventListener('mousemove', mousehandler);
            element.removeEventListener('touchstart', mousehandler);
            element.removeEventListener('touchmove', mousehandler);
            return true;
        });
    }
};


