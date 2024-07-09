
function check_edit_value(){
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var email = document.getElementById("email").value;
    var errorValueFirstName = false;
    var errorValueLastName = false;
    var errorValueEmail = false;

    // Kiểm tra first name
    if (firstName === "") {
        document.getElementById("firstNameError").innerHTML = "Please enter a First name";
        errorValueFirstName = true;
    } else if (!isValidName(firstName)) {
        document.getElementById("firstNameError").innerHTML = "Only accepts letters and spaces";
        errorValueFirstName = true;
    }else{
        document.getElementById("firstNameError").innerHTML = "";
        errorValueFirstName = false;
    }

    // Kiểm tra last name
    if (lastName === "") {
        document.getElementById("lastNameError").innerHTML = "Please enter a Last name";
        errorValueLastName = true;
    } else if (!isValidName(lastName)) {
        document.getElementById("lastNameError").innerHTML = "Only accepts letters and spaces";
        errorValueLastName = true;
    }else{
        document.getElementById("lastNameError").innerHTML = "";
        errorValueLastName = false;
    }

    // Kiểm tra email
    if (email === "") {
        document.getElementById("emailError").innerHTML = "Please enter an email";
        errorValueEmail = true;
    } else if (!check_email_format(email)) {
        document.getElementById("emailError").innerHTML = "Email is not a valid email";
        errorValueEmail = true;
    }else{
        document.getElementById("emailError").innerHTML = "";
        errorValueEmail = false;
    }

    // Nếu có bất kỳ lỗi nào, trả về false để ngăn chặn form submit
    if (errorValueFirstName || errorValueLastName || errorValueEmail) {
        return false;
    }
    return true;
}

function check_rsgister_value(){
    var firstname = document.getElementById("inputFirstName").value;
    var lastname = document.getElementById("inputLastName").value;
    var email = document.getElementById("inputEmail4").value;
    var password = document.getElementById("inputPassword4").value;
    var re_password = document.getElementById("inputRe-enterPassword").value;
    errorValueFirstName = false;
    errorValueLastName = false;
    errorValueEmail = false;
    errorValuePassword = false;
    errorValueRePassword = false;
    if(firstname === ""){
        setError(document.getElementById("inputFirstName"), "Please enter your first name");
        errorValueFirstName = true;
    }else if(!isValidName(firstname)){
        setError(document.getElementById("inputFirstName"), "Only accepts letters and spaces");
        errorValueFirstName = true;
    }
    else{
        setSuccess(document.getElementById("inputFirstName"));
        errorValueFirstName = false;
    }
    if(lastname === ""){
        setError(document.getElementById("inputLastName"), "Please enter your last name");
        errorValueLastName = true;
    }else if(!isValidName(lastname)){
        setError(document.getElementById("inputLastName"), "Only accepts letters and spaces");
        errorValueLastName = true;
    }
    else{
        setSuccess(document.getElementById("inputLastName"));
        errorValueLastName = false;
    }
    if(email === ""){
        setError(document.getElementById("inputEmail4"), "Please enter your email");
        errorValueEmail = true;
    }else if(!check_email_format(email)){
        setError(document.getElementById("inputEmail4"), "Email is not a valid email");
        errorValueEmail = true;
    }
    else if(check_email_exist(email)){
        setError(document.getElementById("inputEmail4"), "Email already exists");
        errorValueEmail = true;
    }
    else{
        setSuccess(document.getElementById("inputEmail4"));
        errorValueEmail = false;
    }
    if(password === ""){
        setError(document.getElementById("inputPassword4"), "Please enter your password");
        errorValuePassword = true;
    }else if(password.length<8){
        setError(document.getElementById("inputPassword4"), "Password must be over 8 characters");
        errorValuePassword = true;
    }else{
        setSuccess(document.getElementById("inputPassword4"));
        errorValuePassword = false;
    }
    if(re_password === ""){
        setError(document.getElementById("inputRe-enterPassword"), "Please enter Re-Password");
        errorValueRePassword = true;
    }else if(password != re_password){
        setError(document.getElementById("inputRe-enterPassword"), "Password not match");
        errorValueRePassword = true;
    }else{
        setSuccess(document.getElementById("inputRe-enterPassword"));
        errorValueRePassword = false;
    }

    if(errorValueFirstName || errorValueLastName || errorValueEmail || errorValuePassword || errorValueRePassword){
        return false;
    }else{
        return true;
    }

    
}
function check_login_value(){
    var email = document.getElementById("inputEmail4").value;
    var password = document.getElementById("inputPassword4").value;
    errorValueEmail = false;
    errorValuePassword = false;
    if(email === ""){
        setError(document.getElementById("inputEmail4"), "Please enter your email");
        errorValueEmail = true;
    }
    else{
        setSuccess(document.getElementById("inputEmail4"));
        errorValueEmail = false;
    }
    if(password === ""){
        setError(document.getElementById("inputPassword4"), "Please enter your password");
        errorValuePassword = true;
    }else{
        setSuccess(document.getElementById("inputPassword4"));
        errorValuePassword = false;
    }
    if(errorValueEmail || errorValuePassword ){
        return false;
    }else{
        return true;
    }
    
}


function check_email_exist(email) {
    var check = false;
    $.ajax({
        type: "GET",
        url: "/checkEmail",
        data: {
            email: email
        },
        async: false,
        success: function (data) {
            if (data === "true") {
                check = true;
            }
        }
    });
    return check;
}

const setError = (element, message) =>{
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');
    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success');
}
const setSuccess = element => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
};

function isValidName(name) {
    var regex = /^[A-Za-z\sàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễđìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳỹỷỵÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄĐÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲỸỶỴ']+$/; // Chỉ chấp nhận các ký tự chữ cái và khoảng trắng
    return regex.test(name);
}

function check_email_format(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}
