// on document ready

var itemWhere = null;
var itemFrom = null;

// Кэш для autocomplete
let cache = {};

$(document).ready(function() {

    assignAutocomplete($(".aqf_aqh_29PUo"));

    // Это теперь инпут
    assignDatepicker($(".ar4_ast_2Xr6l"));

    $("#spinner_a").spinner({
        max: 5,
        min: 1,
        stop: function () {
            $("#adultsCount").val($(this).val());
        }
    }).val(1);

    $("#spinner_c").spinner({
        max: 5,
        min: 0,
        stop: function () {
            $("#childrenCount").val($(this).val());
        }
    }).val(0);

    $(".ui-spinner").css({
        'float' : 'right'
    });

    $("#trip-type").change(function (e) {
        if ($(this).val() === 'compound')
            complexRouteGUI(true);
        else
            complexRouteGUI(false);
        if ($(this).val() === 'round')
            toggleHideSecondDatepicker(false)
        else if ($(this).val() === '1-way')
            toggleHideSecondDatepicker(true);
    });

    // Так как начинаем в режиме 1-way сразу скроем второй дэйтпикер
    toggleHideSecondDatepicker(true);

    // Назначить действие на кнопку "Найти"
    $($(".Button_button_6a0ab").get(0)).click(function () {

    });
});

function assignAutocomplete(target) {
    target.autocomplete({
        minLength: 2,
        source: function (request, response) {
            const term = request.term;

            if ( term in cache ) {
                response( cache[ term ] );
                return;
            }

            $.getJSON( "http://192.168.0.3:3333/autocomplete", request, function( data, status, xhr ) {
                cache[ term ] = data;
                response( data );
            });

        },
        select: function (event, ui) {
            if (event.target.placeholder.startsWith('откуда:')) {
                itemFrom = ui.item;
                $("#src").val(ui.item.value);
                $("#originCode").val(ui.item.id);
            }
            else {
                itemWhere = ui.item;
                $("#dst").val(ui.item.value);
                $("#destinationCode").val(ui.item.id);
            }

            console.dir(ui);
        }
    });
}

function assignDatepicker(target) {
    target.datepicker({
        minDate: 0,
        dateFormat: "yy-mm-dd",
        monthNames : ['Январь','Февраль','Март','Апрель','Май','Июнь','Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
        dayNamesMin : ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
        defaultDate: 0,
        onSelect: function (dateText) {
            if ($(this).parent().parent().attr("class") === "datepicker-1")
                $("#outboundDateFrom").val(dateText);
            else
                $("#outboundDateTo").val(dateText);

            $("#outboundDateTo").val(dateText);
        }
    });
    const today = new Date().toISOString().substr(0, 10);
    $(target).val(today);

    console.dir($(target).parent().parent().attr("class"));

    if ($(target).parent().parent().attr("class") === "datepicker-1")
        $("#outboundDateFrom").val(today);
    else
        $("#outboundDateTo").val(today);

    $("#outboundDateTo").val(today);

}

/* When the user clicks on the button,
        toggle between hiding and showing the dropdown content */
function showDropdown() {
    $('.ddown').css({
        'display': 'block'
    });
}

function expandMarks() {
    // Изменить высоту всех картинок-марок (тру быдлокод)
    $(".mark_img").css({
        'top': 530 + $(".m_form").length*50 + "px"
    });
}

function cloneForm() {

    // Клонируем и удаляем из DOM кнопку
    let btn_add = $("#btn_add");
    $(btn_add).detach();

    // Клонируем форму
    let form = $($(".m_form").get(0)).clone();

    // Скопируем в новую форму dst старой
    $(form).find("input").val("");
    $(form.find("input").get(0)).val(itemWhere == null ? "" : itemWhere.value);

    // Стили для кнопки
    let btn = $(form).find(".Button_button_6a0ab")
    $(btn).css({
        'font-size' : '16px',
        'color' : '#0b9e8a',
        'background-color' : '#f1f2f6'
    });

    // Текст для кнопки
    $((btn).find(".Button_contents_6a0ab")
        .find("span")).text("Удалить перелет");

    // Действие для кнопки
    $(btn).attr("type", "button");
    if ($(".m_form").length > 1) {

        $(btn).css({
            'font-size' : '16px',
            'color' : 'white',
            'background-color' : '#49a9aa'
        });

        $(btn).click(function () {
            $(this).parent().parent().remove();

            expandMarks();
        });

    }

    form.appendTo("#holder");
    btn_add.appendTo('#holder');
    assignAutocomplete($(".aqf_aqh_29PUo"));
    assignDatepicker($(form).find(".ar4_ast_2Xr6l"));

    expandMarks();
}

// state - булеан true - on, false - off
function toggleHideSecondDatepicker(state) {
    if (state) {
        // Убираем второй дейтпикер и раздвигаем первый
        $(".datepicker-2").css({'display': 'none'});
        $(".ar4_ast_2Xr6l").css({'width': '100%'});
    }
    else {
        $(".datepicker-2").css({'display': 'inline-block'});
        $(".ar4_ast_2Xr6l").css({'width': 'calc(100%-60px)'});
    }
}

// state - булеан true - on, false - off
function complexRouteGUI(state) {
    if (state) {
        $("#MainHeroUsps-Usps").css({'display': 'none'}); // скрыть сердечко и текст

        toggleHideSecondDatepicker(true);

        // Кнопка "Добавить ещё перелет"
        let btn_add = $(".Button_button_6a0ab").clone();
        $(btn_add).find("span").text("Добавить ещё перелет...");
        $(btn_add).css({
            'background-color': '#f1f2f6',
            'color': '#14b5a3'
        });
        $(btn_add).attr("id", "btn_add");
        $(btn_add).attr("type", "button");
        $(btn_add).click(cloneForm);
        $(btn_add).appendTo("#holder");

        cloneForm(true);

    }
    else {
        // Развернуть все обратно
        $("#MainHeroUsps-Usps").css({'display': 'block'});
        toggleHideSecondDatepicker(false);

        // Убрать лишние формы и контролы
        $(".m_form").slice(1).remove();
        $("#btn_add").remove();

        expandMarks();
    }
}

$(document).click(function(e) {
        //if you click on anything except the modal itself or the "open modal" link, close the modal
        if (!$(e.target).closest(".ddown,.aj6_apo_10ZKj").length) {
            $('.ddown').css({
                'display': 'none'
            });
        }
});