$.get('/home', {'order': '', 'av': 0}).done(function (data) {
    let items = data;
    console.log(items);
    renderTable(items)
}, "json");

function renderTable(items) {
    $('#tbl').remove();
    let tbl = $("<table id=\"tbl\">\n" +
        "    <tr>\n" +
        "        <th>Тип</th>\n" +
        "        <th>Процессор</th>\n" +
        "        <th>Цена</th>\n" +
        "        <th>В наличии</th>\n" +
        "        <th>Рейтинг</th>\n" +
        "    </tr>" +
        "</table>");
    items.forEach(el => {
        let tr = $("<tr></tr>")
        let td = $("<td></td>")
        let a = $("<a></a>")
        a.attr('href', "/products/?id=" + el.id)
        a.text(el.arduino_board_type);
        td.append(a);
        tr.append(td);

        td = $("<td></td>")
        td.text(el.processor_type);
        tr.append(td);

        td = $("<td></td>")
        td.text(el.price);
        tr.append(td);

        td = $("<td></td>")
        switch (el.cnt) {
            case "many":
                td.text('Много');
                td.addClass("many")
                break;
            case 'few':
                td.text('Мало');
                td.addClass('few');
                break;
            case 'notAv':
                td.text('Нет в наличии');
                td.addClass('notAv');
                break;
        }
        tr.append(td);

        td = $("<td></td>")
        td.text(el.rate);
        tr.append(td);

        tbl.append(tr);
    })
    $('body').append(tbl);
}

function orderBy() {
    const order = document.getElementById('order');
    const avs = document.getElementsByName('av');
    let av = 0
    avs.forEach(i => {
        if (i.checked) {
            av = parseInt(i.value)
        }
    });
    $.get('/home', {'order': order.value, 'av': parseInt(av)}).done(function (data) {
        renderTable(data)
    });
}

function formSend() {
    const order = document.getElementById('order');
    const avs = document.getElementsByName('av');
    const min = document.getElementById('price-min');
    const max = document.getElementById('price-max');
    const search = document.getElementById('search');
    console.log(search.value);
    let av = 0
    avs.forEach(i => {
        if (i.checked) {
            av = parseInt(i.value)
        }
    });
    $.get('/home', {
        'order': order.value,
        'av': parseInt(av),
        'min': parseInt(min.value),
        'max': parseInt(max.value),
        'search': search.value
    }).done(function (data) {
        renderTable(data)
    })
}





