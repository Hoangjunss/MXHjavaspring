
function check_edit_value(){
    alert('Check value');
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var email = document.getElementById("email").value;
    console.log(email);
    var error = false;

    // Kiểm tra first name
    if (firstName === "") {
        document.getElementById("firstNameError").innerHTML = "Please enter a First name";
        error = true;
    } else if (!isValidName(firstName)) {
        document.getElementById("firstNameError").innerHTML = "Only accepts letters and spaces";
        error = true;
    }else{
        document.getElementById("firstNameError").innerHTML = "";
        error = false;
    }

    // Kiểm tra last name
    if (lastName === "") {
        document.getElementById("lastNameError").innerHTML = "Please enter a Last name";
        error = true;
    } else if (!isValidName(lastName)) {
        document.getElementById("lastNameError").innerHTML = "Only accepts letters and spaces";
        error = true;
    }else{
        document.getElementById("lastNameError").innerHTML = "";
        error = false;
    }

    // Kiểm tra email
    if (email === "") {
        document.getElementById("emailError").innerHTML = "Please enter an email";
        console.log("email is empty");
        error = true;
    } else if (!check_email_format(email)) {
        console.log("email is not a valid");
        document.getElementById("emailError").innerHTML = "Email is not a valid email";
        error = true;
    }else{
        document.getElementById("emailError").innerHTML = "";
        error = false;
    }

    // Nếu có bất kỳ lỗi nào, trả về false để ngăn chặn form submit
    if (error) {
        return false;
    }
    return true;
}

function isValidName(name) {
    var regex = /^[A-Za-z\sàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễđìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳỹỷỵÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄĐÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲỸỶỴ']+$/; // Chỉ chấp nhận các ký tự chữ cái và khoảng trắng
    return regex.test(name);
}

function check_email_format(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}
