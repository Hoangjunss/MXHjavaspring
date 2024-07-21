$(document).ready(function () {
    
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    var isUserLogged;

    if (userId) {
        fetchUser(userId);
        fetchRelationship(userId);
    }

    /* Image Preview Functions */
    $('#imageInput').on('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imagePreview = $('#imagePreview');
                imagePreview.attr('src', e.target.result).show();
            };
            reader.readAsDataURL(file);
        }
    });

    $('#imagePostInput').on('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const previewBox = $('#preview_box');
                const showImage = $('<img>').attr('src', e.target.result).addClass('preview');
                const deleteButton = $('<span>').html('<i class="fa-solid fa-xmark"></i>');

                const imageBox = $('<div>').addClass('imageBox');
                imageBox.append(showImage).append(deleteButton);
                previewBox.append(imageBox);

                $('#imageInput').hide();
                $('#image_box_item').empty(); // Assuming removeMessage removes all children

                deleteButton.on('click', function() {
                    imageBox.remove();
                    $('#imageInput').val('').show();
                });
            };
            reader.readAsDataURL(file);
        }
    });

    document.getElementById('form_edit_account').addEventListener('submit', function(event) {
        event.preventDefault();
    
        const form = event.target;
        const firstName = form.querySelector('input[name="firstName"]').value.trim();
        const lastName = form.querySelector('input[name="lastName"]').value.trim();
        const imageInput = document.getElementById('imageInput');
        const imageFile = imageInput.files[0];

        // Kiểm tra nếu có bất kỳ trường nào bị bỏ trống
        if (!firstName || !lastName) {
            alert('Vui lòng điền đầy đủ thông tin.');
            return; // Dừng xử lý tiếp theo nếu có trường bị bỏ trống
        }
        
        const formData = new FormData();
        formData.append('firstName', firstName);
        formData.append('lastName', lastName);

        if (imageFile) {
            formData.append('imageFile', imageFile);
        }
    
        fetch('/api/editaccount', {
            method: 'POST',
            body: formData
        })
        .then(response =>  {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                alert('Edit account successful');
                // Tải lại trang hiện tại
                window.location.reload();
            } else if (data.message === "Email already exists") {
                document.getElementById('email-error').style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
    });

    $('#form_edit_profile').on('submit', function(event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của form

        // Lấy tất cả dữ liệu từ form
        const formData = $(this).serializeArray();
        const userAboutForm = {};
        formData.forEach(field => {
            const [_, index, fieldName] = field.name.match(/userAboutDTOs\[(\d+)\]\.(.*)/);
            if (!userAboutForm.userAboutDTOs) userAboutForm.userAboutDTOs = [];
            if (!userAboutForm.userAboutDTOs[index]) userAboutForm.userAboutDTOs[index] = {};
            userAboutForm.userAboutDTOs[index][fieldName] = field.value;
        });

        // Gửi yêu cầu POST lên server
        $.ajax({
            url: '/api/editprofiledetails',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userAboutForm),
            success: function(response) {
                // Nếu yêu cầu thành công, load lại trang web
                window.location.href = "/profile?id=" + response.userId;
            },
            error: function(xhr, status, error) {
                // Xử lý lỗi nếu có
                console.error('Error:', error);
            }
        });
    });

    $(document).on('click', '.relationship-btn', function(event) {
        event.preventDefault();
        handleRelationshipButtonClick(event);
    });
    
});

function fetchUser(userId) {
    $.get(`/api/getuser?id=${userId}`, function(data) {
        updateProfile(data);
    }).fail(function(error) {
        console.error('Error fetching user:', error);
    });
}

function fetchRelationship(userId) {
    $.get(`/api/getrelationship?userId=${userId}`, function(data) {
        updateRelationship(data);
        fetchUserAbouts(userId);
    }).fail(function(error) {
        console.error('Error fetching relationship:', error);
    });
}

function fetchUserAbouts(userId) {
    $.get(`/api/getabouts?userId=${userId}`, function(data) {
        updateUserAbouts(data);
    }).fail(function(error) {
        console.error('Error fetching user abouts:', error);
    });
}

function updateProfile(data) {
    const avatarUser = $('#imageProfile');
    avatarUser.attr('src', data.user.image ? data.user.image.urlImage : '/images/users/DefaultAvtUser.png');
    $('.profile-fullname').text(`${data.user.firstName} ${data.user.lastName}`);
}

function updateRelationship(data) {
    isUserLogged = data.isOwnUser;
    const divSetFriend = $('#setfriend');
    const formSetFriend = $('#setfrienduser');

    formSetFriend.empty(); // Clear existing buttons

    if (data.isOwnUser) {
        const userString = JSON.stringify(data.user).replace(/'/g, "&apos;");
        divSetFriend.append(`
            <button type="button" class="btn btn-follow mr-3" id="editProfile" onclick='displayEditProfile(${userString})'><i class='bx bx-plus'></i> Edit Profile</button>
        `);
    } else {
        $('.profile-img-caption').hide();
        $('.cover-overlay').hide();
        let buttonHtml = `<input type="hidden" name="id" value="${data.user.id}">`;

        if (!data.relationship || data.relationship.status.id == 1) {
            buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="2"><i class='bx bx-plus'></i>Add Friend</button>`;
        } else {
            switch (data.relationship.status.id) {
                case 2:
                    if (data.relationship.userOne.id == data.user.id) {
                        buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="3"><i class='bx bx-plus'></i>Accept</button>
                                       <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Reject</button>`;
                    } else {
                        buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Recall</button>`;
                    }
                    break;
                case 3:
                    buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>UnFriend</button>`;
                    break;
            }
        }

        formSetFriend.append(buttonHtml);
    }
}

function updateUserAbouts(data) {
    const aboutUserUI = $('#aboutuser').empty();
    data.userAbouts.forEach(userAbout => {
        data.abouts.forEach(about => {

            if (userAbout.about.id == about.id && userAbout.description!="") {
                aboutUserUI.append(`
                    <div class="intro-item d-flex justify-content-between align-items-center">
                        <p class="intro-title text-muted"><i class='bx bx-briefcase text-primary'></i> 
                            <strong>${about.name}: ${userAbout.description}</strong> 
                        </p>
                    </div>
                `);

            }
        });
    });

    if (isUserLogged) {
        aboutUserUI.append(`
            <div class="intro-item d-flex justify-content-between align-items-center">
                <button onclick="displayEditProfileDetails()" class="btn btn-quick-link join-group-btn border w-100" id="editProfileDetails">Edit Profile Details</button>
            </div>
        `);
    }
}

function updateStatusPost(data) {
    if (isUserLogged) {
        const statusSelect = $('#StatusId').empty();
        data.status.forEach(stt => {
            statusSelect.append(`<option value="${stt.id}">${stt.name}</option>`);
        });
    } else {
        $('#uploadpost').remove();
    }
}

function handleRelationshipButtonClick(event) {
    const button = $(event.target);
    const form = button.closest('.relationship-form');
    const userId = form.find('input[name="id"]').val();
    const status = button.data('status');
    updateRelationshipStatus(userId, status, form);
}

function updateRelationshipStatus(userId, status, form) {
    const formData = {
        userId: userId,
        status: status
    };
    $.ajax({
        url: '/api/relationship', // Update with the new URL
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(data) {
            if (data.success) {
                window.location.reload();
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}

function closeEditProfileDetails() {
    $('#edit_profile').hide();
    $('#overlay_details').css('display', 'none');
    document.body.style.overflow = "auto";
}

function closeEditProfile() {
    $('#edit_profile_user').css('display', 'none');
    $('#overlay_user').css('display', 'none');
    document.body.style.overflow = "auto";
}

function displayEditProfileDetails() {
    $.get('/api/getabouts', function(data) {
        const formEditProfile = $('#form_edit_profile').empty();
        data.abouts.forEach((about, index) => {
            const userAbout = data.userAbouts.find(ua => ua.about.id === about.id);

            const description = userAbout ? userAbout.description : '';
            formEditProfile.append(`
                <div class="name_box">
                    <input type="hidden" name="userAboutDTOs[${index}].aboutId" value="${about.id}">
                    <label>${about.name}</label>
                    <input type="text" placeholder="${about.name}" name="userAboutDTOs[${index}].description" value="${description}">
                </div>
            `);
        });

        formEditProfile.append('<button type="submit" id="submitBtn">Save</button>');
        $('#edit_profile').css('display', 'flex');
        $('#overlay_details').css('display', 'block');
        $('body').css('overflow', 'hidden');
    }).fail(function(error) {
        console.error('Error fetching abouts:', error);
        document.body.style.overflow = "auto";
    });
}

function displayEditProfile(user) {
    const userImage = user.image ? user.image.urlImage : '/images/users/DefaultAvtUser.png';
    const editProfileHtml = `
        <div class="image_box">
            <div class="item" id="image_box_item">
                <label for="">Avatar User</label>
                <input type="file" placeholder="Avatar" id="imageInput" onchange="previewImageAvartarUser(event)" name="image">
            </div>
            <div class="preview_box" id="preview_box">
                <img id="currentImage" src="${userImage}" alt="User Image" style="max-width: 200px; max-height: 200px;"/>
            </div>
        </div>
        <div class="name_box">
            <label>First Name</label>
            <input type="text" name="firstName" value="${user.firstName}">
        </div>
        <div class="name_box">
            <label>Last Name</label>
            <input type="text" name="lastName" value="${user.lastName}">
        </div>
        <button type="submit" id="submitBtn">Save</button>
    `;
    $('#form_edit_account').html(editProfileHtml);
    $('#edit_profile_user').css('display', 'flex');
    $('#overlay_user').css('display', 'block');
    $('body').css('overflow', 'hidden');
}

function previewImageAvartarUser(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const currentImage = $('#currentImage');
        currentImage.attr('src', reader.result);
        currentImage.addClass('preview');

        const deleteButton = $('<span>');
        const icon = $('<i>').addClass('fa-solid fa-xmark');
        deleteButton.append(icon);
    };
    reader.readAsDataURL(event.target.files[0]);
}

function openmessage() {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    if(isUserLogged){
        window.location.href = '/messagesmobile';
    }else{
        window.location.href = '/chatmobile?id='+userId;
    }
}