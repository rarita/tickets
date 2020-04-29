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
})
