// Данные из бэкэнда
var g_data;

$(document).ready(function() {

    // Заглушка
    $(".price___183db").remove();

    initControls();
});

function initControls() {
    // Деньги
    $( "#price-slider" ).slider({
        range: true,
        min: 0,
        max: 1000,
        values: [ 25, 1000 ],
        slide: function( event, ui ) {
            $( "#amount" ).val( "₽" + ui.values[ 0 ]*100 + " - ₽" + ui.values[ 1 ]*100 );
        }
    });
    $( "#amount" ).val( "₽" + $( "#price-slider" ).slider( "values", 0 )*100 +
        " - ₽" + $( "#price-slider" ).slider( "values", 1 )*100 );
    // Время
    $( "#time-slider" ).slider({
        range: true,
        min: 0,
        max: 143,
        values: [ 0, 143 ],
        slide: function( event, ui ) {
            const timeStart = fractionToTime(ui.values[ 0 ]);
            const timeEnd = fractionToTime(ui.values[ 1 ]);
            $( "#amount-time" ).val( timeStart + " - " + timeEnd );
        }
    });
    $( "#amount-time" ).val(fractionToTime($( "#time-slider" ).slider( "values", 0 )) +
        " - " + fractionToTime($( "#time-slider" ).slider( "values", 1 )) );
    // Время на борту
    $( "#time-onboard-slider" ).slider({
        range: true,
        min: 0,
        max: 143,
        values: [ 0, 18 ],
        slide: function( event, ui ) {
            const timeStart = fractionToTime(ui.values[ 0 ]);
            const timeEnd = fractionToTime(ui.values[ 1 ]);
            $( "#amount-onboard-time" ).val( timeStart + " - " + timeEnd );
        }
    });
    $( "#amount-onboard-time" ).val(fractionToTime($( "#time-onboard-slider" ).slider( "values", 0 )) +
        " - " + fractionToTime($( "#time-onboard-slider" ).slider( "values", 1 )) );
    // Мультимодальные параметры
    $( ".modal-control-group" ).controlgroup({
        "direction": "vertical"
    });

    // Вкладки меню сверху
    $(".menu-tab").click(function() {
        // Удаляем активный статус
       $(".menu-tab-active").removeClass("menu-tab-active");
       // Добавляем данной вкладке активный статус
        $(this).addClass("menu-tab-active");
        // Перемещаем полоску под него multimodal-chkboxes
        const tabId = $(".menu-tab").index($(this));
        const css_left = (tabId*25) + "%";
        $(".line___1F-Hx").css("left", css_left);
        if (tabId === 3) {
            $("#multimodal-chkboxes").css("display", "block")
        }
        else {
            $("#multimodal-chkboxes").css("display", "none")
        }
    });

    // Сортировка
    $("#sort_by").on('change', function () {
        $(".cell___16tDr").remove();
        const items = sortDataOn(g_data, this.value).map(it => renderItem(it));
        items.forEach(it => $(".left").append(it));

        // Клики по добавленным перелетам
        $(".cell___16tDr").click(onCellClick);
    });

    // Фильтры WIP


}

/**
 * сортирует данные по переданному принципу
 */
function sortDataOn(data, condition) {
    switch (condition) {
        case "price_asc":
            return data.sort(function (a, b) {
                return a.totalPrice - b.totalPrice;
            });
        case "price_desc":
            return data.sort(function (a, b) {
                return b.totalPrice - a.totalPrice;
            });
        case "duration_asc":
            return data.sort(function (a, b) {
                return a.totalDuration - b.totalDuration;
            });
        case "duration_desc":
            return data.sort(function (a, b) {
                return b.totalDuration - a.totalDuration;
            });
    }
    return null; // не случится никогда
}

/**
 * Фильтрует данные по состоянию объектов фильтров в DOM
 */
function filterDataOnDOM() {

}

function fractionToTime(fraction) {
    var hours = Math.floor(fraction / 6).toString();
    if (hours.length < 2)
        hours = "0" + hours;
    var minutes = (fraction % 6).toString() + "0";
    return hours + ":" + minutes;
}

/**
 * разместить на экране полученные с сервера данные
 * @param data то, что мы получаем ответом из бэкэнда
 * Array of itin_data
 */
function setUpSearchResults(data) {
    // Дебаг
    console.dir(data);

    // Подготавливаем дату для работы
    data.forEach(d => {
        d.totalPrice = d.itin.reduce(priceReduce, 0);

        const o_dt_minutes = d.itin[0].departureTime[3]*60 + d.itin[0].departureTime[4];
        const o_at_minutes = d.itin[d.itin.length - 1].arrivalTime[3]*60 + d.itin[d.itin.length - 1].arrivalTime[4];
        d.totalDuration = o_at_minutes - o_dt_minutes;

    });
    // Сохраняем дату
    g_data = data;
    // Рендерим элементы для всех айтемов и дефолтно сортируем
    const renderedItems = sortDataOn(data, $("#sort_by").val()).map(it => renderItem(it));
    console.dir(renderedItems); // Пока что так
    renderedItems.forEach(it => $(".left").append(it)); // И так

    // Назначить клики
    // Клики по перелетам
    $(".cell___16tDr").click(onCellClick);
}

function onCellClick() {
    const idx = $(".cell___16tDr").index($(this));
    console.dir(idx);
    // make coords array
    coords = [];
    coords.push([g_data[idx].src.longitude, g_data[idx].src.latitude]);
    for (var i = 0; i < g_data[idx].itin.length; i++) {
        const itinerary = g_data[idx].itin[i];
        const dest = JSON.parse(JSON.stringify(itinerary)).destination;

        const long = dest["longitude"];
        const lat = dest["latitude"];
        coords.push([long, lat]);
    }

    console.dir(coords);
    updateMap(coords);
}

/**
 * ну это чтобы редюситть тотал прайс
 */
function priceReduce(total, value, index, array) {
    return total + value.cost;
}

// Вернет отрендеренный блок который можно приплюсовать к списку
function renderItem(itemData) {
    var container = jQuery('<div/>', {'class': 'cell___16tDr'}); // есть вариант selected___3K41_
    var c2 = jQuery('<div/>', {'class': 'Container_container_98308'});
    var link_c = jQuery('<a/>', {'class': 'link___2H3sm'}); // тут был href ещё

    var c3_section = jQuery('<div/>', {'class': 'slimContainerSection___30GVK'});
    var c4_section = jQuery('<div/>', {'class': 'container___msmDb'});

    // Элементы внутри контейнера

    var indicators = jQuery('<div/>', {'class': 'indicators___U9uwV'});
    // - Индикаторы саб-дивы
    var tripFeatures = jQuery('<div/>', {'class': 'tripFeatures___2rD-K'});
    var companies = jQuery('<div/>', {'class': 'companiesContainer___10R6U'});
    var logos = jQuery('<div/>', {'class': 'logos___L5Uuv'});
    var logoContainer = jQuery('<div/>', {'class': 'logoContainer___1pUKz'});

    // Картинки Авиакомпаний
    itemData.itin.forEach(it => {
        const carrierCode = it.carrierCode;
        const imgUrl = "https://images.kiwi.com/airlines/64x64/" + carrierCode + ".png";
        var airlineLogoImg = jQuery('<img/>', {
            'src': imgUrl,
            'class': 'sc-AxjAm lmzjeI',
            'style': 'margin-right: 10px;'
        });
        airlineLogoImg.height("48px");

        airlineLogoImg.appendTo(logoContainer);
    });


    // Упаковка дивов
    logoContainer.appendTo(logos);
    logos.appendTo(companies);
    companies.appendTo(tripFeatures);
    tripFeatures.appendTo(indicators);

    var timesAndStations = jQuery('<div/>', {'class': 'timesAndStationsContainer___2zGZA'});
    // Время и станции саб-дивы

    // - Время
    var times = jQuery('<span/>', {'class': 'times___3pG-p'});
    var departureColumn = jQuery('<span/>', {'class': 'departureColumn___2FH6w'});

    var depTime = jQuery('<span/>');
    var tripTime = jQuery('<span/>', {'class': 'tripTime___1UC_S'});
    var time = jQuery('<span/>', {'class': 'time___1NfGs'});
    var actualTime = jQuery('<span/>');

    // Заранее посчитаем затраченное время
    durationString = Math.floor(itemData.totalDuration / 60) + "ч" + ((itemData.totalDuration % 60 > 9) ?
        itemData.totalDuration % 60 : "0" + itemData.totalDuration % 60) + "м";

    const depTimeData = itemData.itin[0].departureTime;
    if (depTimeData[3].toString().length < 2)
        depTimeData[3] = "0" + depTimeData[3];
    if (depTimeData[4].toString().length < 2)
        depTimeData[4] = "0" + depTimeData[4];
    actualTime.text(depTimeData[3] + ":" + depTimeData[4]);

    var duration = jQuery('<span/>', {'class': 'duration___1lrZd'});
    duration.append(jQuery('<span/>', {'class': 'separator___HwxsU'}));
    var actualDuration = jQuery('<span/>');
    actualDuration.text(durationString);
    duration.append(actualDuration);
    duration.append(jQuery('<span/>', {'class': 'separator___HwxsU'}));

    var arrivalColumn = jQuery('<span/>', {'class': 'arrivalColumn___2iJcq'});
    var arrTime = jQuery('<span/>');
    var a_tripTime = jQuery('<span/>', {'class': 'tripTime___1UC_S'});
    var a_time = jQuery('<span/>', {'class': 'time___1NfGs'});
    var a_actualTime = jQuery('<span/>');
    const arrTimeData = itemData.itin[itemData.itin.length - 1].arrivalTime;
    if (arrTimeData[3].toString().length < 2)
        arrTimeData[3] = "0" + arrTimeData[3];
    if (arrTimeData[4].toString().length < 2)
        arrTimeData[4] = "0" + arrTimeData[4];
    a_actualTime.text(arrTimeData[3] + ":" + arrTimeData[4]);

    // Упаковка дивов времени
    actualTime.appendTo(time);
    time.appendTo(tripTime);
    tripTime.appendTo(depTime);
    depTime.appendTo(departureColumn);
    duration.appendTo(departureColumn);

    a_actualTime.appendTo(a_time);
    a_time.appendTo(a_tripTime);
    a_tripTime.appendTo(arrTime);
    arrTime.appendTo(arrivalColumn);


    departureColumn.appendTo(times);
    arrivalColumn.appendTo(times);

    // - Станции
    var stations = jQuery('<div/>', {'class': 'stations___21org'});
    var srcStation = jQuery('<span/>', {'class': 'station___2Ru5P'});
    const srcStationData = ((itemData.src.name_RU) ? itemData.src.name_RU : itemData.src.name_EN) +
    ", " + ((itemData.c_src.name_RU) ? itemData.c_src.name_RU : itemData.c_src.name_EN);
    srcStation.text(srcStationData);
    var destStation = jQuery('<span/>', {'class': 'station___2Ru5P'});
    const dstStationData = ((itemData.dst.name_RU) ? itemData.dst.name_RU : itemData.dst.name_EN) +
        ", " + ((itemData.c_dst.name_RU) ? itemData.c_dst.name_RU : itemData.c_dst.name_EN);
    destStation.text(dstStationData);

    // Упаковка дивов станций
    srcStation.appendTo(stations);
    destStation.appendTo(stations);

    // Упаковка времен, станций
    times.appendTo(timesAndStations);
    stations.appendTo(timesAndStations);

    // Остановки (плейсхолдер)
    var stops = jQuery('<div/>', {'class': 'stopsContainer___1upkn'});
    var numberOfChanges = jQuery('<button/>', {'class': 'button___3hiYb', 'type': 'button', 'data-e2e': 'numberOfChanges'});
    var stopsText = jQuery('<span/>', {'class': 'stops___1mrFf'});
    var stopsActualText = jQuery('<span/>');
    const switchesCount = itemData.itin.length - 1;
    stopsActualText.text( switchesCount + " пересадок");

    var iconSpan = jQuery('<span/>', {'class': 'icon___dH54l'});
    var iconSvg = jQuery('<svg/>', {'xmlns': 'http://www.w3.org/2000/svg', 'viewBox': '0 0 26 15'});
    var iconPath = jQuery('<path d="M13 10.2L3.4.6c-.8-.8-2-.8-2.8 0-.8.8-.8 2 0 2.8l11 11c.4.4.9.6 1.4.6.5 0 1-.2 1.4-.6l11-11c.8-.8.8-2 0-2.8-.8-.8-2-.8-2.8 0L13 10.2z" fill="red" fill-rule="evenodd"/>');

    // Упаковка остановок
    iconPath.appendTo(iconSvg);
    iconSvg.appendTo(iconSpan);

    stopsActualText.appendTo(stopsText);

    stopsText.appendTo(numberOfChanges);
    iconSpan.appendTo(numberOfChanges);

    numberOfChanges.appendTo(stops);

    // Цена перелета
    var tripPrice = jQuery('<div/>', {'class': 'tripPriceContainer___2yEXL'});
    var priceHolder = jQuery('<div/>', {'class': 'price___1dv6u'});
    var priceActualText = jQuery('<span/>');
    // Правильная прайс дата
    var priceData = 0;
    itemData.itin.forEach(it => priceData += it.cost);
    priceActualText.text(priceData + " Р");

    // Упаковка цен перелета
    priceActualText.appendTo(priceHolder);
    priceHolder.appendTo(tripPrice);


    // << Упаковка элементов контейнера в контейнер
    indicators.appendTo(c4_section);
    timesAndStations.appendTo(c4_section);
    stops.appendTo(c4_section);
    tripPrice.appendTo(c4_section);

    // <<< Упаковка контейнеров
    c4_section.appendTo(c3_section);
    c3_section.appendTo(link_c);
    link_c.appendTo(c2);
    c2.appendTo(container);

    return container;

}
