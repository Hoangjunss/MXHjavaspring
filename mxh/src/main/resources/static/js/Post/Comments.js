function showComment(id){
    fetch('/api/comment?id='+id,{
        method:'GET'
    })  .then(response =>  {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    }) 
    .then(data => {
        displayComment(data, id)
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
    });
}
    function displayComment(data, postId) {
        const divContainerComment = $(`#commentslist-${postId}`);
        if (divContainerComment.hasClass('show-comments')) {
            divContainerComment.removeClass('show-comments').addClass('hide-comments');
            return; // Không thực hiện các hành động khác nếu đã ẩn thẻ
        }
        else {
            divContainerComment.removeClass('hide-comments').addClass('show-comments');
        }

        divContainerComment.empty();
    
        let commentsFrom = `
            <div class="row bootstrap snippets">
                <div class="col-md-12">
                    <div class="comment-wrapper">
                        <div class="panel panel-info">
                            <div class="panel-body">
                                <ul class="media-list comments-list" id="comments-list" style="list-style:none;">
                                    <li class="media comment-form">
                                        <a href="#" class="pull-left">
                                            <img th:src="@{/images/users/user-4.jpg}" alt="" class="img-circle">
                                        </a>
                                        <div class="media-body" data-post-id="${postId}">
                                            <form id="commentForm-${postId}" class="commentForm">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="input-group">
                                                            <input type="hidden" id="postId-${postId}" name="postId" value="${postId}">
                                                            <input type="text" id="content-${postId}" name="content" class="form-control comment-input" placeholder="Write a comment...">
                                                            <div class="input-group-btn">
                                                                <button type="button" class="btn btn-primary" onclick="submitComment(${postId})">Comment</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </li>
        `;
    
        let displayComment = '';
        if (data.comments != null) {
            data.comments.forEach(comment => {
                let avtUserCmt = '/images/users/DefaultAvtUser.png';
                if(comment.userSend.image){
                    avtUserCmt = comment.userSend.image.urlImage;
                }
                const timeAgo = formatTimeAgo(comment.createAt);
                displayComment += `
                    <li class="media">
                        <a href="/profile?id=${comment.userSend.id}" class="pull-left">
                            <img src="${avtUserCmt}" style="width: 30px;" alt="" class="img-circle">
                        </a>
                        <div class="media-body">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark"><a href="#" class="fs-8">${comment.userSend.firstName} ${comment.userSend.lastName}</a></strong>
                                <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                            </div>
                            <span class="d-block comment-created-time">${timeAgo}</span>
                            <p class="fs-8 pt-2">${comment.content}</p>
                        </div>
                    </li>
                `;
            });
    
            displayComment += `
                <li class="media">
                    <div class="media-body">
                        <div class="comment-see-more text-center">
                            <button type="button" class="btn btn-link fs-8">See More</button>
                        </div>
                    </div>
                </li>
                </ul>
                </div>
                </div>
                </div>
                </div>
                </div>
            `;
        }
    
        divContainerComment.append(commentsFrom);
        divContainerComment.append(displayComment);

        // divContainerComment.removeClass('hide-comments').addClass('show-comments');
    }
    
    function submitComment(postId) {
        const content = $(`#content-${postId}`).val();
        if (!content.trim()) {
            alert("Comment content cannot be empty!");
            return;
        }
    
        const data = {
            postId: postId,
            content: content
        };
    
        $.ajax({
            url: '/api/commenter',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(data) {
                if (data.success) {
                    addCommentToDOM(data.comment, postId);
                    $(`#content-${postId}`).val('');
                } else {
                    alert("Failed to post comment. Please try again.");
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }
    function addCommentToDOM(comment, postId) {
        let avtUserCmt = '/images/users/DefaultAvtUser.png';
        if (comment.userSend.image) {
            avtUserCmt = comment.userSend.image.urlImage;
        }
        const commentHTML = `
            <li class="media">
                <a href="/profile?id=${comment.userSend.id}" class="pull-left">
                    <img src="${avtUserCmt}" alt="" class="img-circle">
                </a>
                <div class="media-body">
                    <div class="d-flex justify-content-between align-items-center w-100">
                        <strong class="text-gray-dark"><a href="#" class="fs-8">${comment.userSend.firstName} ${comment.userSend.lastName}</a></strong>
                        <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                    </div>
                    <span class="d-block comment-created-time">Now</span>
                    <p class="fs-8 pt-2">${comment.content}</p>
                </div>
            </li>
        `;
    
        const divContainerComment = $(`#commentslist-${postId}`);
        const commentsList = divContainerComment.find('.comments-list');
        const commentForm = commentsList.find('.comment-form');
    
        // Thêm bình luận mới ngay sau thẻ form 
        commentForm.after(commentHTML);
        // divContainerComment.css('maxHeight', `${divContainerComment[0].scrollHeight}px`);
    
        const commentButton = $(`.show-comments-btn[data-id='${postId}']`);
        let countComment = parseInt(commentButton.text().trim().split(" ")[0]);
        countComment += 1;
        commentButton.html(`<i class='bx bx-message-rounded mr-2'></i>${countComment}`);
    
        // Xóa nội dung trong input sau khi bình luận
        $(`#content-${postId}`).val('');
    }
    