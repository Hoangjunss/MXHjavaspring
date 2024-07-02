document.addEventListener("DOMContentLoaded", function() {
    const reactionButtons = document.querySelectorAll(".reaction-button");

    reactionButtons.forEach(button => {
        button.addEventListener("click", function(event) {
            event.preventDefault();

            const reactionId = this.getAttribute("data-reaction-id");
            const postId = this.getAttribute("data-post-id");

            // Tạo dữ liệu gửi đi
            const data = {
                reactionId: reactionId,
                postId: postId
            };

            // Gửi yêu cầu AJAX
            fetch('/interact', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    console.log("Reaction saved successfully!");
                    // Cập nhật giao diện người dùng nếu cần
                } else {
                    console.error("Failed to save reaction.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
        });
    });
    const notificationItem = document.querySelector('.nav-item.s-nav.dropdown.notification');
    const dropdownMenu = notificationItem.querySelector('.dropdown-menu');

    notificationItem.addEventListener('click', (e) => {
        e.preventDefault(); // Prevent default link behavior
        markNotificationsAsRead();
        dropdownMenu.classList.toggle('show');
    });

    // Close the dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!notificationItem.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }
    });
    const showCommentsButtons = document.querySelectorAll('.show-comments');

    showCommentsButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();

            const postId = this.getAttribute('data-id'); // Lấy giá trị data-id từ button
            const commentsSection = document.querySelector('.hide-comments[data-id="' + postId + '"]');

            if (commentsSection.style.display === 'none' || commentsSection.style.display === '') {
                commentsSection.style.display = 'block';
                commentsSection.style.maxHeight = commentsSection.scrollHeight + 'px';
            } else {
                commentsSection.style.maxHeight = 0;
                setTimeout(() => commentsSection.style.display = 'none', 300);
            }
        });
    });
    document.querySelectorAll('.commentForm').forEach(form => {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
    
            const postId = this.querySelector('input[name="postId"]').value;
            const content = this.querySelector('input[name="content"]').value;
            alert(content + " " + postId);
    
            // Gửi AJAX request đến server
            fetch('/commenter', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ postId, content })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Tạo phần tử HTML mới cho comment
                    const newComment = document.createElement('li');
                    newComment.classList.add('media');
                    newComment.innerHTML = `
                        <a href="#" class="pull-left">
                            <img src="/images/users/user-2.jpg" alt="" class="img-circle">
                        </a>
                        <div class="media-body">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark"><a href="#" class="fs-8">${data.comment.userSend.firstName} ${data.comment.userSend.lastName}</a></strong>
                                <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                            </div>
                            <span class="d-block comment-created-time">30min ago</span>
                            <p class="fs-8 pt-2">${data.comment.content}</p>
                            <div class="commentLR">
                                <button type="button" class="btn btn-link fs-8">Like</button>
                                <button type="button" class="btn btn-link fs-8">Reply</button>
                            </div>
                        </div>
                    `;
                    // Thêm comment mới vào danh sách các comment
                    const commentsList = form.closest('.media').nextElementSibling;
                    commentsList.insertBefore(newComment, commentsList.firstChild);
    
                    // Reset form
                    form.reset();
                } else {
                    // Xử lý khi có lỗi
                    alert('Đã xảy ra lỗi khi thêm bình luận. SEVER');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Đã xảy ra lỗi khi thêm bình luận. CLIENT');
            });
        });
    });       
});

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
