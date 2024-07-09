// JavaScript/jQuery
$(document).ready(function() {
    $('#formLogin').on('submit', function(event){
        event.preventDefault();
        var isError = false;

        // Validate each field
        isError |= validateField('#inputEmail4', 'Please enter your email','.errorEmail');
        isError |= validatePassword('#inputPassword4','.errorPassword');
        /* hello */
        if (!isError) {
            this.submit();
        }
    });

    function validateField(selector, emptyMsg, errorSelector) {
        var value = $(selector).val().trim();
        if (value === "") {
            $(errorSelector).text(emptyMsg).show();
            return true;
        } else {
            $(errorSelector).hide();
            return false;
        }
    }

    function validatePassword(passwordSelector, passwordErrorSelector) {
        var password = $(passwordSelector).val().trim();
        var isError = false;

        if (password === "") {
            $(passwordErrorSelector).text('Please enter your password').show();
            isError = true;
        } else {
            $(passwordErrorSelector).hide();
        }
        return isError;
    }
});
