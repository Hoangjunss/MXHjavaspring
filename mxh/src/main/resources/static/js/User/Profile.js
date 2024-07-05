document.addEventListener("DOMContentLoaded", function () {

    /* document.getElementById('startChatButton').addEventListener('click', function () {
        if (window.innerWidth <= 768) { // Kích thước màn hình mobile (có thể điều chỉnh theo ý bạn)
            window.location.href = '/messagermobile';
        } else {
            window.location.href = '/messager';
        }
    });
    const overlayAdd = document.getElementById('edit_profile');
    const editProfileDetails = document.getElementById('editProfileDetails')
    const closeButton = overlayAdd.querySelector('.close');
    if(editProfileDetails!=null){
        editProfileDetails.addEventListener('click', () => {
        overlayAdd.style.display = "flex";
    })

    closeButton.addEventListener('click', () => {

        overlayAdd.style.display = "none";
    })
    }

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
    const relationshipForms = document.querySelectorAll('.relationship-form');
    relationshipForms.forEach(form => {
        const buttons = form.querySelectorAll('.relationship-btn');
        buttons.forEach(button => {
            button.addEventListener('click', function () {
                const userId = form.querySelector('input[name="id"]').value;
                const status = button.getAttribute('data-status');
                alert(status + " " + userId);
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
                        alert('Failed to update relationship status');
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        });
    });

    function updateButtons(form, newStatus) {
        const buttons = form.querySelectorAll('.relationship-btn');
        buttons.forEach(button => {
            switch (newStatus) {
                case 1:
                    if (button.getAttribute('data-status') == '1') {
                        button.textContent = 'Send to';
                        const recallButton = document.createElement('button');
                        recallButton.type = 'button';
                        recallButton.classList.add('btn', 'btn-follow', 'mr-3', 'relationship-btn');
                        recallButton.setAttribute('data-status', '4');
                        recallButton.innerHTML = "<i class='bx bx-plus'></i>Recall";
                        recallButton.addEventListener('click', function() {
                            updateButtons(form, 4);
                        });
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
    } */
    /* GET API */
    const notificationItem = document.querySelector('.nav-item.s-nav.dropdown.notification');
    const dropdownMenu = notificationItem.querySelector('.dropdown-menu');
    const dropContent = dropdownMenu.querySelector('.drop-content');

    notificationItem.addEventListener('click', (e) => {
        alert("CLICK")
        e.preventDefault(); // Prevent default link behavior
        markNotificationsAsRead();
        dropdownMenu.classList.toggle('show');
        if (dropdownMenu.classList.contains('show')) {
            fetchNotificationsList();
        }
    });

    // Close the dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!notificationItem.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }
    });

    function fetchNotificationsList() {
        fetch('/api/notifications', { method: 'GET' })
            .then(response => response.json())
            .then(data => {
                dropContent.innerHTML = '';
                data.notifications.forEach(notification => {
                    const notificationElement = document.createElement('li');
                    notificationElement.innerHTML = `
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <div class="notify-img">
                                <img src="#" alt="notification user image">
                            </div>
                        </div>
                        <div class="col-md-10 col-sm-10 col-xs-10">
                            <a href="${notification.url}" class="notification-user">${notification.user.firstName}</a>
                            <span class="notification-type">${notification.message}</span>
                            <a href="#" class="notify-right-icon">
                                <i class='bx bx-radio-circle-marked'></i>
                            </a>
                            <p class="time">
                                <span class="badge badge-pill badge-primary"><i class='bx bxs-group'></i></span>
                                30minutes ago
                            </p>
                        </div>
                    `;
                    dropContent.appendChild(notificationElement);

                });
            })
            .catch(error => console.error('Error:', error));
    }

    function markNotificationsAsRead() {
        fetch('/notificationsischecked', {
            method: 'POST'
        })
            .then(response => {
                if (response.ok) {
                    // Remove the unread messages count badge
                    const unreadMessagesBadge = document.querySelectorAll('.unread-messages');
                    unreadMessagesBadge.forEach(badge => badge.remove());
                } else {
                    console.error('Failed to mark notifications as read.');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    /* Notification */
    // Lấy userId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    let isUserLogged;

    // Kiểm tra xem userId có tồn tại không
    if (userId) {
        fetchNotificationsIsCheck(userId);
        fetchUser(userId);
        fetchRelationship(userId);
        fetchUserAbouts(userId);
    }

    // Hàm để lấy thông tin thông báo từ server
    function fetchNotificationsIsCheck(userId) {
        fetch(`/countNotificationsIsCheck?userId=${userId}`)
            .then(response => response.json())
            .then(data => {
                updateNotificationsIsCheck(data);
            })
            .catch(error => console.error('Error fetching notifications:', error));
    }
    
    function fetchUser(userId){
        fetch(`/api/getuser?id=${userId}`)
           .then(response => response.json())
           .then(data => {
                console.log(data);
                updateProfile(data);
            })
           .catch(error => console.error('Error fetching user:', error));
    }

    function fetchRelationship(userId){
        fetch(`/api/getrelationship?userId=${userId}`)
           .then(response => response.json())
           .then(data => {
                console.log(data);
                updateRelationship(data);
                fetchStatusPost();
            })
           .catch(error => console.error('Error fetching relationship:', error));
    }

    function fetchUserAbouts(userId){
        fetch(`/api/getabouts?userId=${userId}`)
           .then(response => response.json())
           .then(data => {
                console.log(data);
                updateUserAbouts(data);
            })
           .catch(error => console.error('Error fetching user abouts:', error));
    }
    
    function fetchStatusPost(){
        fetch(`/status`)
           .then(response => response.json())
           .then(data => {
                console.log(data);
                updateStatusPost(data);
            })
           .catch(error => console.error('Error fetching status post:', error));
    }

    // Hàm để cập nhật thông tin thông báo đã đ��c
    function updateNotificationsIsCheck(data) {
        const unreadCount = data.unreadCount;
        const quantityNotification = document.getElementById('quantityNotification');
        if (unreadCount > 0) {
            quantityNotification.textContent = unreadCount;
            quantityNotification.style.display = 'inline';
        } else {
            quantityNotification.style.display = 'none';
        }
    }

    function updateProfile(data){
        const fullNameElement = document.querySelector('.profile-fullname');
        fullNameElement.textContent = `${data.user.firstName} ${data.user.lastName}`;
    }

    function updateRelationship(data){
        isUserLogged = data.isOwnUser;
        alert(isUserLogged);
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
                    <input type="hidden" name="id" th:value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
                `;
            } else if (data.relationship != null && data.relationship.status.id == 4) {
                fromSetFriend.innerHTML += ` 
                    <input type="hidden" name="id" th:value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
                                               
                `;
            } else if (data.relationship != null && data.relationship.status.id != 4) {
                if (data.relationship.status.id == 1 && data.relationship.userOne.id == user.id) {
                    fromSetFriend.innerHTML += `
                    <input type="hidden" name="id" th:value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="2"><i class='bx bx-plus'></i>Accept</button>
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Reject</button>
                                               
                `;
                } else if (data.relationship.status.id == 1 && data.relationship.userOne.id != user.id) {
                    fromSetFriend.innerHTML += `
                        <input type="hidden" name="id" th:value="${data.user.id}">
                        <button type="button" class="btn btn-follow mr-3 relationship-btn"><i class='bx bx-plus'></i>Send to</button>
                        <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Recall</button>
                                               
                    `;
                } else if (data.relationship.status.id == 2) {
                    fromSetFriend.innerHTML += `
                        <input type="hidden" name="id" th:value="${data.user.id}">
                        <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>UnFriend</button>
                                               
                    `;
                }
            }
        }
    }  

    function updateUserAbouts(data){
        const aboutUserUI = document.getElementById('aboutuser');
        if (data.userAbouts != null) {
            data.userAbouts.forEach(userAbout => {
                data.abouts.forEach(about =>{
                    if(userAbout.about.id == about.id){
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
            if(isUserLogged){
                aboutUserUI.innerHTML += `<div class="intro-item d-flex justify-content-between align-items-center">
                                    <button href="#" class="btn btn-quick-link join-group-btn border w-100" id="editProfileDetails">Edit Profile Details</button>
                                </div>
                `;
            }
        }
    }
        
    function updateStatusPost(data){
        alert(isUserLogged +" STATUSPOST")
        if (isUserLogged) {
            const statusSelect = document.getElementById('StatusId');
                data.status.forEach(stt =>{
                    const option = document.createElement('option');
                    option.value = stt.id;
                    option.textContent = stt.name;
                    statusSelect.appendChild(option);
                })
        }else{
            document.getElementById('uploadpost').remove();
        }
    }
    /* END API */
});