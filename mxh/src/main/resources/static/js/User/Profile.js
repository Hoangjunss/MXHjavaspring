document.addEventListener("DOMContentLoaded", function () {

    document.getElementById('startChatButton').addEventListener('click', function () {
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
    }
});
