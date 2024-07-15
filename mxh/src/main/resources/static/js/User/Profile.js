$(document).ready(function () {
    
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    let isUserLogged;

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

    $(document).on('click', '.relationship-btn', function(event) {
        const button = $(event.target);
        const form = button.closest('.relationship-form');
        const userId = form.find('input[name="id"]').val();
        const status = button.data('status');
    
        alert(status + " " + userId); // Debugging alert
    
        $.ajax({
            url: '/relationship', // Địa chỉ URL của API cần gọi
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ userId: userId, status: status }),
            success: function(response) {
                updateButtons(form, response.newStatus);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                alert('An unexpected error occurred');
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
        console.log(data);
        updateProfile(data);
    }).fail(function(error) {
        console.error('Error fetching user:', error);
    });
}

function fetchRelationship(userId) {
    $.get(`/api/getrelationship?userId=${userId}`, function(data) {
        console.log(data);
        updateRelationship(data);
        fetchStatusPost();
        fetchUserAbouts(userId);
    }).fail(function(error) {
        console.error('Error fetching relationship:', error);
    });
}

function fetchUserAbouts(userId) {
    $.get(`/api/getabouts?userId=${userId}`, function(data) {
        console.log(data);
        updateUserAbouts(data);
    }).fail(function(error) {
        console.error('Error fetching user abouts:', error);
    });
}

function fetchStatusPost() {
    $.get(`/status`, function(data) {
        console.log(data);
        updateStatusPost(data);
    }).fail(function(error) {
        console.error('Error fetching status post:', error);
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
        let buttonHtml = `<input type="hidden" name="id" value="${data.user.id}">`;

        if (!data.relationship || data.relationship.status.id == 4) {
            buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>`;
        } else {
            switch (data.relationship.status.id) {
                case 1:
                    if (data.relationship.userOne.id == data.user.id) {
                        buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="2"><i class='bx bx-plus'></i>Accept</button>
                                       <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Reject</button>`;
                    } else {
                        buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Recall</button>`;
                    }
                    break;
                case 2:
                    buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>UnFriend</button>`;
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

function openMessage() {
    const url = window.innerWidth <= 768 ? '/messagesmobil' : '/messages';
    window.location.href = url;
}

function handleRelationshipButtonClick(event) {
    const button = $(event.target);
    const form = button.closest('.relationship-form');
    const userId = form.find('input[name="id"]').val();
    const status = button.data('status');
    alert(status + " " + userId); // Debugging alert
    updateRelationshipStatus(userId, status, form);
}

function updateRelationshipStatus(userId, status, form) {
    const formData = {
        userId: userId,
        status: status
    };

    $.ajax({
        url: '/relationship', // Update with the new URL
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(data) {
            if (data.success) {
                updateButtons(form, data.newStatus);
            } else {
                alert(data.message);
            }
        },
        error: function(error) {
            console.error('Error:', error);
            alert('An unexpected error occurred');
        }
    });
}

// Hàm để cập nhật nút button
function updateButtons(form, newStatus) {
    const buttons = form.find('.relationship-btn');
    buttons.off('click'); // Xóa các sự kiện click cũ
    buttons.each(function() {
        const button = $(this);
        switch (newStatus) {
            case 1:
                if (button.data('status') == '1') {
                    button.text('Send to');
                    const recallButton = $('<button/>', {
                        type: 'button',
                        class: 'btn btn-follow mr-3 relationship-btn',
                        'data-status': '4',
                        html: "<i class='bx bx-plus'></i>Recall"
                    }).appendTo(form);
                } else {
                    button.remove();
                }
                break;
            case 2:
                if (button.data('status') == '2') {
                    button.text('UnFriend').data('status', '4');
                } else {
                    button.remove();
                }
                break;
            case 4:
                if (button.data('status') == '4') {
                    button.text('Add Friend').data('status', '1');
                    form.find('button[data-status="4"]').remove();
                } else {
                    button.remove();
                }
                break;
        }
    }).on('click', handleRelationshipButtonClick); // Thêm lại sự kiện click mới
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
    }).fail(function(error) {
        console.error('Error fetching abouts:', error);
        document.body.style.overflow = "hidden";
    });
}

function displayEditProfile(user) {
    const editProfileHtml = `
        <div class="name_box">
            <label>First Name</label>
            <input type="text" name="firstName" value="${user.firstName}">
        </div>
        <div class="name_box">
            <label>Last Name</label>
            <input type="text" name="lastName" value="${user.lastName}">
        </div>
        <div class="name_box">
            <label>Email</label>
            <input type="text" name="email" value="${user.email}">
            <p id="email-error" style="color: red; display: none;">Email already exists</p>
        </div>
        <button type="submit" id="submitBtn">Save</button>
    `;
    $('#form_edit_account').html(editProfileHtml);
    $('#edit_profile_user').css('display', 'flex');
    $('#overlay_user').css('display', 'block');
    $('body').css('overflow', 'hidden');
}