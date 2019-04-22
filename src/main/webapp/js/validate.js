$(document).ready(function () {
    $(".numeric").keypress(function(e) {
        return (/^\d+$/.test(String.fromCharCode(e.which)))
    });
});
