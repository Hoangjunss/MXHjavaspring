// JavaScript/jQuery
$(document).ready(function() {
    $('#formEditAccount').on('submit', function(event){
        event.preventDefault();
        var isError = false;

        // Validate each field
        isError |= validateField('#firstName', isValidName, 'Please enter your first name', 'The first name is not in the correct format', '.errorFirstName');
        isError |= validateField('#lastName', isValidName, 'Please enter your last name', 'The last name is not in the correct format', '.errorLastName');
        isError |= validateField('#email', isValidEmail, 'Please enter your email', 'The email is not valid', '.errorEmail');

        if (!isError) {
            this.submit();
        }
    });

    function validateField(selector, validationFn, emptyMsg, invalidMsg, errorSelector) {
        var value = $(selector).val().trim();
        if (value === "") {
            $(errorSelector).text(emptyMsg).show();
            return true;
        } else if (!validationFn(value)) {
            $(errorSelector).text(invalidMsg).show();
            return true;
        } else {
            $(errorSelector).hide();
            return false;
        }
    }
    
    function isValidName(name) {
        var regex = /^[A-Za-z\sàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễđìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳỹỷỵÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄĐÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲỸỶỴ]+$/;
        return regex.test(name);
    }

    function isValidEmail(email) {
        var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return regex.test(email.toLowerCase());
    }
});
