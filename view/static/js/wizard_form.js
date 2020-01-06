$(document).ready(function () {

    let current_fs, next_fs, previous_fs; //fieldsets
    let opacity;

    $(".step").click(function () {
            let current_id = $(this).attr('id');
            $(this).addClass("active");
            $("#progressbar li").each(function (index, element) {
                if ($(element).attr('id') !== current_id) {
                    $(element).removeClass("active")
                }
            });
            $('fieldset').each(function (index, element) {
                if (!$(element).hasClass(current_id)) {
                    $(element).animate({opacity: 0}, {
                        step: function (now) {
// for making fielset appear animation
                            opacity = 1 - now;

                            $(element).css({
                                'display': 'none',
                                'position': 'relative'
                            });
                            $('fieldset[style*="display: none"]').css({'opacity': opacity});
                        },
                        duration: 600
                    });
                }
            });

            $(`fieldset.${current_id}`).css({
                'display': '',
                'position': '',
                'opacity': ''
            })
            $(`fieldset.${current_id}`).show();
        }
    );

    $(".next").click(function () {

        current_fs = $(this).parent();
        next_fs = $(this).parent().next();

//Add Class Active
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

//show the next fieldset
        next_fs.show();
//hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
// for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                next_fs.css({'opacity': opacity});
            },
            duration: 600
        });
    });

    $(".previous").click(function () {

        current_fs = $(this).parent();
        previous_fs = $(this).parent().prev();

//Remove class active
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

//show the previous fieldset
        previous_fs.show();

//hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
// for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                previous_fs.css({'opacity': opacity});
            },
            duration: 600
        });
    });

    $('.radio-group .radio').click(function () {
        $(this).parent().find('.radio').removeClass('selected');
        $(this).addClass('selected');
    });

    $(".submit").click(function () {
        return false;
    })

});