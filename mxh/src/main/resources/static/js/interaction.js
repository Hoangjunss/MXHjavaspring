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
