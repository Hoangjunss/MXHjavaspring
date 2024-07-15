$(document).ready(function () {
    
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    let isUserLogged;

    if (userId) {
        fetchUser(userId);
        fetchRelationship(userId);
    }

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

    /* Profile Details Edit */
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
        }).fail(function(error) {
            console.error('Error fetching abouts:', error);
        });

    }


    function closeEditProfileDetails() {
        $('#edit_profile').hide();
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

        if (data.isOwnUser) {
            formSetFriend.remove();
            divSetFriend.append(`
                <button type="button" class="btn btn-follow mr-3" id="editProfile"><i class='bx bx-plus'></i> Edit Profile</button>
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
                            buttonHtml += `<button type="button" class="btn btn-follow mr-3 relationship-btn"><i class='bx bx-plus'></i>Send to</button>
                                           <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Recall</button>`;
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

                if (userAbout.about.id == about.id) {
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
        $.post('/relationship', {
            userId: userId,
            status: status
        }, function(data) {
            if (data.success) {
                updateButtons(form, data.newStatus);
            } else {
                alert('Cập nhật trạng thái mối quan hệ thất bại');
            }
        }).fail(function(error) {
            console.error('Lỗi:', error);
        });
    }

// Hàm để cập nhật nút button
function updateButtons(form, newStatus) {
    const buttons = form.querySelectorAll('.relationship-btn');
    buttons.forEach(button => {
        button.removeEventListener('click', handleRelationshipButtonClick); // Xóa các sự kiện click cũ
        button.addEventListener('click', handleRelationshipButtonClick); // Thêm lại sự kiện click mới
        switch (newStatus) {
            case 1:
                if (button.getAttribute('data-status') == '1') {
                    button.textContent = 'Send to';
                    const recallButton = document.createElement('button');
                    recallButton.type = 'button';
                    recallButton.classList.add('btn', 'btn-follow', 'mr-3', 'relationship-btn');
                    recallButton.setAttribute('data-status', '4');
                    recallButton.innerHTML = "<i class='bx bx-plus'></i>Recall";
                    recallButton.addEventListener('click', handleRelationshipButtonClick); // Gắn sự kiện click cho button mới
                    form.appendChild(recallButton);
                } else {
                    button.remove();
                }
                break;
            case 2:
                if (button.getAttribute('data-status') == '2') {
                    button.textContent = 'UnFriend';
                    button.setAttribute('data-status', '4');
                } else {
                    button.remove();
                }
                break;
            case 4:
                if (button.getAttribute('data-status') == '4') {
                    button.textContent = 'Add Friend';
                    button.setAttribute('data-status', '1');
                    const recallButton = form.querySelector('button[data-status="4"]');
                    if (recallButton) recallButton.remove();
                } else {
                    button.remove();
                }
                break;
        }
    });
}

document.addEventListener('click', function(event) {
    if (event.target.closest('.relationship-form') && event.target.classList.contains('relationship-btn')) {
        handleRelationshipButtonClick(event);
    }
});
});