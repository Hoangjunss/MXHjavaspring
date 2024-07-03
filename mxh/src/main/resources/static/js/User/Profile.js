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
            const button = form.querySelector('.relationship-btn');
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
                            updateButton(button, data.newStatus);
                        } else {
                            alert('Failed to update relationship status');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            });
        function updateButton(button, newStatus) {
            switch (newStatus) {
                case 0:
                case 4:
                    button.textContent = 'Add Friend';
                    button.setAttribute('data-status', '0');
                    break;
                case 1:
                    button.textContent = 'Accept';
                    button.setAttribute('data-status', '1');
                    break;
                case 2:
                    button.textContent = 'UnFriend';
                    button.setAttribute('data-status', '2');
                    break;
            }
        }
    });
});
