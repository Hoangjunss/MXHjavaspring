document.addEventListener("DOMContentLoaded", function () {
    /* Notification */
    // Lấy userId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    let isUserLogged;
    // Kiểm tra xem userId có tồn tại không
    if (userId) {
        fetchUser(userId);
        fetchRelationship(userId);
    }

    function fetchUser(userId) {
        fetch(`/api/getuser?id=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateProfile(data);
            })
            .catch(error => console.error('Error fetching user:', error));
    }

    function fetchRelationship(userId) {
        fetch(`/api/getrelationship?userId=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateRelationship(data);
                fetchStatusPost();
                fetchUserAbouts(userId);
            })
            .catch(error => console.error('Error fetching relationship:', error));
    }

    function fetchUserAbouts(userId) {
        fetch(`/api/getabouts?userId=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateUserAbouts(data);
            })
            .catch(error => console.error('Error fetching user abouts:', error));
    }

    function fetchStatusPost() {
        fetch(`/status`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateStatusPost(data);
            })
            .catch(error => console.error('Error fetching status post:', error));
    }

    /* END API */

    function previewImagePost(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const imagePreview = document.getElementById('imagePreview');
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        }
    }
    function previewImage(event) {
        var reader = new FileReader();
        reader.onload = function () {
            const previewBox = document.getElementById('preview_box');
            let showImage = document.createElement('img');
            showImage.src = reader.result;
            showImage.classList.add('preview');
            let deleteButton = document.createElement('span');

            let icon = document.createElement('i');
            icon.className = "fa-solid fa-xmark";
            deleteButton.appendChild(icon);
            let imageBox = document.createElement('div');
            imageBox.classList.add('imageBox');
            imageBox.appendChild(showImage);
            imageBox.appendChild(deleteButton);
            previewBox.appendChild(imageBox);

            document.getElementById('imageInput').style.display = 'none';
            const ImageBox = document.getElementById('image_box_item');

            removeMessage(ImageBox);

            deleteButton.addEventListener('click', () => {
                imageBox.remove();
                document.getElementById('imageInput').value = '';
                document.getElementById('imageInput').style.display = 'block';
            });

        };
        reader.readAsDataURL(event.target.files[0]);
    }


});

function displayEditProfileDetails() {
    const overlayAdd = document.getElementById('edit_profile');
    // Fetch dữ liệu từ endpoint
    fetch('/api/getabouts')
        .then(response => response.json())
        .then(data => {
            // Xử lý dữ liệu nhận được
            const abouts = data.abouts;
            const userAbouts = data.userAbouts;

            // Tạo HTML cho form dựa trên dữ liệu nhận được
            const formEditProfile = document.getElementById('form_edit_profile');
            formEditProfile.innerHTML = ''; // Xóa bỏ các phần tử trong form trước khi thêm mới
            let inputField = ''; // Sử dụng let thay vì const

            abouts.forEach((about, index) => {
                const userAbout = userAbouts.find(ua => ua.about.id === about.id);
                const description = userAbout ? userAbout.description : '';

                inputField = `
        <div class="name_box">
            <input type="hidden" name="userAboutDTOs[${index}].aboutId" value="${about.id}">
            <label>${about.name}</label>
            <input type="text" placeholder="${about.name}" name="userAboutDTOs[${index}].description" value="${description}">
        </div>
    `;
                formEditProfile.insertAdjacentHTML('beforeend', inputField);
            });

            const submitButton = `<button type="submit" id="submitBtn">Save</button>`;
            formEditProfile.insertAdjacentHTML('beforeend', submitButton);

            const overlayAdd = document.getElementById('edit_profile');
            overlayAdd.style.display = "flex";
        })
        .catch(error => console.error('Error fetching abouts:', error));
}

// Hàm đóng modal
function closeEditProfileDetails() {
    const overlayAdd = document.getElementById('edit_profile');
    overlayAdd.style.display = "none";
}

function updateProfile(data) {
    const avartaruser = $('#imageProfile');
    if(data.user.image){
        avartaruser.attr('src', data.user.image.urlImage);
    }else{
        avartaruser.attr('src', '/images/users/DefaultAvtUser.png');
    }
    const fullNameElement = document.querySelector('.profile-fullname');
    fullNameElement.textContent = `${data.user.firstName} ${data.user.lastName}`;
}

function updateRelationship(data) {
    isUserLogged = data.isOwnUser;
    const divsetfriend = document.getElementById('setfriend');
    const fromSetFriend = document.getElementById('setfrienduser');
    if (data.isOwnUser) {
        fromSetFriend.remove();
        divsetfriend.innerHTML += `
            <button type="button" class="btn btn-follow mr-3" id="editProfile"><i class='bx bx-plus'></i> Edit Profile</button>
        `;
    } else if (!data.isOwnUser) {
        if (data.relationship == null) {
            fromSetFriend.innerHTML += `
                <input type="hidden" name="id" value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
            `;
        } else if (data.relationship != null && data.relationship.status.id == 4) {
            fromSetFriend.innerHTML += ` 
                <input type="hidden" name="id" value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
                                           
            `;
        } else if (data.relationship != null && data.relationship.status.id != 4) {
            if (data.relationship.status.id == 1 && data.relationship.userOne.id == data.user.id) {
                fromSetFriend.innerHTML += `
                <input type="hidden" name="id" value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="2"><i class='bx bx-plus'></i>Accept</button>
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Reject</button>
                                           
            `;
            } else if (data.relationship.status.id == 1 && data.relationship.userOne.id != data.user.id) {
                fromSetFriend.innerHTML += `
                    <input type="hidden" name="id" value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn"><i class='bx bx-plus'></i>Send to</button>
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Recall</button>                
                `;
            } else if (data.relationship.status.id == 2) {
                fromSetFriend.innerHTML += `
                    <input type="hidden" name="id" value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>UnFriend</button>
                                           
                `;
            }
        }
    }
}

function updateUserAbouts(data) {
    const aboutUserUI = document.getElementById('aboutuser');
    if (data.userAbouts != null) {
        data.userAbouts.forEach(userAbout => {
            data.abouts.forEach(about => {
                if (userAbout.about.id == about.id) {
                    aboutUserUI.innerHTML += `
                    <div class="intro-item d-flex justify-content-between align-items-center">
                                        <p class="intro-title text-muted"><i class='bx bx-briefcase text-primary'></i> 
                                                <strong>${about.name}: ${userAbout.description}</strong> 
                                            </p>
                                    </div>
                    `;
                }
            })
        })
        if (isUserLogged) {
            aboutUserUI.innerHTML += `<div class="intro-item d-flex justify-content-between align-items-center">
                                <button href="#" onclick="displayEditProfileDetails()" class="btn btn-quick-link join-group-btn border w-100" id="editProfileDetails">Edit Profile Details</button>
                            </div>
            `;
        }
    }
}

function updateStatusPost(data) {
    if (isUserLogged) {
        const statusSelect = document.getElementById('StatusId');
        data.status.forEach(stt => {
            const option = document.createElement('option');
            option.value = stt.id;
            option.textContent = stt.name;
            statusSelect.appendChild(option);
        })
    } else if (!isUserLogged) {
        document.getElementById('uploadpost').remove();
    }
}

function openmessage(){
    if (window.innerWidth <= 768) { // Kích thước màn hình mobile (có thể điều chỉnh theo ý bạn)
        window.location.href = '/messagesmobil';
    } else {
        window.location.href = '/messages';
    }
}

// Hàm để xử lý sự kiện click của các button
function handleRelationshipButtonClick(event) {
    const button = event.target;
    const form = button.closest('.relationship-form');
    const userId = form.querySelector('input[name="id"]').value;
    const status = button.getAttribute('data-status');
    alert(status + " " + userId); // Dùng cho mục đích gỡ lỗi
    updateRelationshipStatus(userId, status, form);
}

// Hàm để cập nhật trạng thái mối quan hệ
function updateRelationshipStatus(userId, status, form) {
    fetch('/relationship', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userId: userId,
            status: status
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            updateButtons(form, data.newStatus);
        } else {
            alert('Cập nhật trạng thái mối quan hệ thất bại');
        }
    })
    .catch(error => console.error('Lỗi:', error));
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