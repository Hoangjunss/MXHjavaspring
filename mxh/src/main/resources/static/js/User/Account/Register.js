// JavaScript/jQuery
$(document).ready(function() {
    $('#formRegister').on('submit', function(event){
        event.preventDefault();
        var isError = false;

        // Validate each field
        isError |= validateField('#inputFirstName', isValidName, 'Please enter your first name', 'The first name is not in the correct format', '.errorFirstName');
        isError |= validateField('#inputLastName', isValidName, 'Please enter your last name', 'The last name is not in the correct format', '.errorLastName');
        isError |= validateField('#inputEmail4', isValidEmail, 'Please enter your email', 'The email is not valid', '.errorEmail');
        isError |= validatePassword('#inputPassword4', '#inputRe-enterPassword', '.errorPassword', '.errorRe-Password');

        if (!isError) {
            let formData = {
                lastName: document.getElementById("inputLastName").value,
                firstName: document.getElementById("inputFirstName").value,
                password: document.getElementById("inputPassword4").value,
                email: document.getElementById("inputEmail4").value,
            };
            fetch("/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 409) {
                        throw new Error("Email already exists");
                    } else {
                        throw new Error("An unexpected error occurred");
                    }
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    window.location.href = "/Confirm";
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                alert(error.message);
            });
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

    function validatePassword(passwordSelector, confirmPasswordSelector, passwordErrorSelector, confirmPasswordErrorSelector) {
        var password = $(passwordSelector).val().trim();
        var confirmPassword = $(confirmPasswordSelector).val().trim();
        var isError = false;

        if (password === "") {
            $(passwordErrorSelector).text('Please enter your password').show();
            isError = true;
        } else if (password.length < 8) {
            $(passwordErrorSelector).text('Password must be at least 8 characters long').show();
            isError = true;
        } else {
            $(passwordErrorSelector).hide();
        }

        if (confirmPassword === "") {
            $(confirmPasswordErrorSelector).text('Please confirm your password').show();
            isError = true;
        } else if (confirmPassword !== password) {
            $(confirmPasswordErrorSelector).text('Passwords do not match').show();
            isError = true;
        } else {
            $(confirmPasswordErrorSelector).hide();
        }

        return isError;
    }
});
