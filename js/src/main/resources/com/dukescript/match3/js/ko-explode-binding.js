    ko.bindingHandlers['explode'] = {
        update: function (element, valueAccessor, allBindings, data, bindingContext) {
            var value = valueAccessor();
            var tile = data;
            var parent = bindingContext.$parent;
            var remove = function () {
                tile.type(-1);       
                $(element).removeClass("explode");
                value(false);
                parent.remove();
            };
            var explode = ko.unwrap(value);
            if (explode) {
                $(element).addClass("explode");
                setTimeout(remove, 210);               
            }
        }
    };
    
