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
            .then(response =>  {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
            .then(result => {
                if (result.success) {
                    // Cập nhật giao diện người dùng nếu cần
                } else {
                    console.error("Failed to save reaction.");
                }
            })
            .catch(error => {
                console.error('Error fetching chat messages:', error.message);
                // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
            });
        });
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
    
            // Gửi AJAX request đến server
            fetch('/commenter', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ postId, content })
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
                }
            })
            .catch(error => {
                console.error('Error fetching chat messages:', error.message);
                // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
            });
        });
    });       
});

function redirectToMessagePage() {
    window.location.href = '/messagesmobile';
}
