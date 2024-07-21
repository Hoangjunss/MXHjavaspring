function interaction(event){
        const reactionId = event.getAttribute("data-reaction-id");
        const postId = event.getAttribute("data-post-id");
        alert('hello');
        const data = {
            reactionId: reactionId,
            postId: postId
        };
        fetch('/api/interact', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response =>  {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(result => {
            if (result.success) {
                updateReactionUI(reactionId, postId);
            } else {
                console.error("Failed to save reaction.");
            }
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            alert("Có lỗi xảy ra khi lưu phản ứng. Vui lòng thử lại.");
        });
    };

function updateReactionUI(reactionId, postId) {
    const postElement = document.querySelector(`i[data-post-id="${postId}"]`);
    
    if (postElement) {
        // Xóa tất cả các lớp CSS hiện tại
        postElement.className = 'bx mr-2'; // Chỉ giữ lại lớp cơ bản 'bx'

        switch(reactionId) {
            case '1':
                postElement.classList.add('bxs-like');
                break;
            case '2':
                postElement.classList.add('bxs-heart');
                break;
            case '3':
                postElement.classList.add('bxs-laugh');
                break;
            case '4':
                postElement.classList.add('bxs-sad');
                break;
            case '5':
                postElement.classList.add('bxs-angry');
                break;
        }
    }
}

function redirectToMessagePage() {
    window.location.href = '/messagesmobile';
}
