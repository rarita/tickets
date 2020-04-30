// on document ready

$(document).ready(function() {
    let cache = {};

    $(".aqf_aqh_29PUo").autocomplete({
        minLength: 2,
        source: function (request, response) {
            const term = request.term;

            if ( term in cache ) {
                response( cache[ term ] );
                return;
            }

            $.getJSON( "http://localhost:3333/autocomplete", request, function( data, status, xhr ) {
                cache[ term ] = data;
                response( data );
            });

        },
        select: function (event, ui) {
            console.log("Selected " + ui.id + " - " + ui.value);
        }
    });

    // Это теперь инпут
    $(".ar4_ast_2Xr6l").datepicker({
        minDate: 0,
        dateFormat: "d MM, yy",
        monthNames : ['Январь','Февраль','Март','Апрель','Май','Июнь','Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
        dayNamesMin : ['Вс','Пн','Вт','Ср','Чт','Пт','Сб']
    });

    $("#spinner_a").spinner({
        max: 5,
        min: 1
    }).val(1);

    $("#spinner_c").spinner({
        max: 5,
        min: 0
    }).val(0);

    $(".ui-spinner").css({
        'float' : 'right'
    });



});

/* When the user clicks on the button,
        toggle between hiding and showing the dropdown content */
function showDropdown() {
    $('.ddown').css({
        'display': 'block'
    });
}

$(document).click(function(e) {
        //if you click on anything except the modal itself or the "open modal" link, close the modal
        if (!$(e.target).closest(".ddown,.aj6_apo_10ZKj").length) {
            $('.ddown').css({
                'display': 'none'
            });
        }
});