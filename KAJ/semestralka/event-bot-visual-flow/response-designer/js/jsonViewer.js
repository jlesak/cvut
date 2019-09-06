class JsonViewer {
    /**
     * Prevadi JSON na HTML a vypisuje do daneho prvku
     * @param target prvek do ktereho ma byt JSON vepsan
     * @param json ktery ma byt vypsan
     */
    render(target, json) {
        let html = this.parseJson(json);
        if (this.isCollapsible(json)) {
            html = '<a href class="json-toggle"></a>' + html;
        }
        target.innerHTML = html;

        $('.json-toggle').on('click', function(e) {
            e.preventDefault();
            const target = $(this).toggleClass('collapsed').siblings('ul.json-dict, ul.json-array');
            target.toggle();
            const count = target.children('li').length;
            if (target.is(':visible')) {
                target.siblings('.json-placeholder').remove();
            }
            else {
                const placeholder = count + (count > 1 ? ' items' : ' item');
                target.after('<span class="json-placeholder">' + placeholder + '</span>');
            }
            return false;
        });
    };

    isCollapsible(arg)
    {
        return arg instanceof Object && Object.keys(arg).length > 0;
    }

    /**
     * rekurzivne prevede JSON na HTML znaky
     * @param json
     * @returns {string}
     */
    parseJson(json)
    {
        let html = '';
        if (json instanceof Array) {
            if (json.length > 0) {
                html += '[<ul class="json-array">';
                for (const i in json) {
                    html += '<li>';

                    if (this.isCollapsible(json[i])){
                        html += '<a href class="json-toggle"></a>';
                    }
                    html += this.parseJson(json[i]);

                    if (i < json.length - 1) {
                        html += ',';
                    }
                    html += '</li>';
                }
                html += '</ul>]';
            }
            else {
                html += '[]';
            }
        }

        else if (typeof json === 'string') {
            html += '<span class="json-string">"' + json + '"</span>';
        }
        else if (typeof json === 'number' || typeof json === 'boolean' || json === null) {
            html += '<span class="json-literal">' + json + '</span>';
        }

        else {
            let key_count = Object.keys(json).length;
            if (key_count > 0) {
                html += '{<ul class="json-dict">';
                for (const i in json) {
                    if (json.hasOwnProperty(i)) {
                        html += '<li>';

                        if (this.isCollapsible(json[i])) {
                            html += '<a href class="json-toggle"></a>';
                        }
                        html += '"'+i+'"' + ': ' + this.parseJson(json[i]);

                        if (--key_count > 0) {
                            html += ',';
                        }
                        html += '</li>';
                    }
                }
                html += '</ul>}';
            }
            else {
                html += '{}';
            }
        }
        return html;
    }
}
