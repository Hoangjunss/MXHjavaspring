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
});