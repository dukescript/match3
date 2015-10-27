ko.bindingHandlers['swap'] = {
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        var direction = ko.unwrap(value);
        if (direction !== "none") {
            $(element).addClass(direction, 100, "linear", function () {
                $(element).removeClass(direction, 100, "linear", function(){
                    value("none");
                });
            }
            );
        }
    }
};
