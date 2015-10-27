ko.bindingHandlers['drop'] = {
    init: function (element, valueAccessor, allBindings, data, bindingContext) {
        var value = valueAccessor();
        var row = ko.unwrap(value);
        var parent = bindingContext.$parent;
        if (row !== "none" && data.type() !== -1) {         
            $(element).addClass("drop-" + row, .00001, "linear", function () {
                $(element).removeClass("drop-" + row, 1000, "easeOutBack", function () {
                    value("none");
                    parent.dropped(data);
                });
            }
            );
        }
    }
}
;
