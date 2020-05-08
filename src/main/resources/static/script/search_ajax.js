
$(document).ready(function() {
    $(".left").append(renderItem());
    initControls();
});

function initControls() {
    // Деньги
    $( "#price-slider" ).slider({
        range: true,
        min: 0,
        max: 500,
        values: [ 25, 500 ],
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
}

function initMap() {
    var myMap = new ymaps.Map("map_float", {
        // Координаты центра карты.
        // Порядок по умолчанию: «широта, долгота».
        // Чтобы не определять координаты центра карты вручную,
        // воспользуйтесь инструментом Определение координат.
        center: [55.76, 37.64],
        // Уровень масштабирования. Допустимые значения:
        // от 0 (весь мир) до 19.
        zoom: 7
    });
}

function fractionToTime(fraction) {
    var hours = Math.floor(fraction / 6).toString();
    if (hours.length < 2)
        hours = "0" + hours;
    var minutes = (fraction % 6).toString() + "0";
    return hours + ":" + minutes;
}

// Вернет отрендеренный блок который можно приплюсовать к списку
function renderItem() {
    var container = jQuery('<div/>', {'class': 'cell___16tDr'});
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
    const imgUrl = "https://images.kiwi.com/airlines/64x64/" + "S7" + ".png";
    var airlineLogoImg = jQuery('<img/>', {
        'src': imgUrl,
        'class': 'sc-AxjAm lmzjeI',
        'style': 'margin-right: 10px;'
    });
    airlineLogoImg.height("48px");

    // Упаковка дивов
    airlineLogoImg.appendTo(logoContainer);
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
    actualTime.text("00:00");

    var duration = jQuery('<span/>', {'class': 'duration___1lrZd'});
    duration.append(jQuery('<span/>', {'class': 'separator___HwxsU'}));
    var durationString = "3" + "ч" + "10" + "м";
    var actualDuration = jQuery('<span/>');
    actualDuration.text(durationString);
    duration.append(actualDuration);
    duration.append(jQuery('<span/>', {'class': 'separator___HwxsU'}));

    var arrivalColumn = jQuery('<span/>', {'class': 'arrivalColumn___2iJcq'});
    var arrTime = jQuery('<span/>');
    var a_tripTime = jQuery('<span/>', {'class': 'tripTime___1UC_S'});
    var a_time = jQuery('<span/>', {'class': 'time___1NfGs'});
    var a_actualTime = jQuery('<span/>');
    a_actualTime.text("23:59");

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
    srcStation.text("Станция отпр.");
    var destStation = jQuery('<span/>', {'class': 'station___2Ru5P'});
    destStation.text("Станция назн.");

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
    stopsActualText.text("N пересадок");

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
    priceActualText.text("1 111 Р");

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
