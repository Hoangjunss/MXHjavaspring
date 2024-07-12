function showComment(id){
    fetch('/comment?id='+id,{
        method:'GET'
    })  .then(response => response.json()) 
    .then(data => {
        displayComment(data, id)
    })
}   

function displayComment(data, postId) {
    const divContainerComment = document.getElementById(`commentslist-${postId}`);
    if (divContainerComment.classList.contains('show-comments')) {
        divContainerComment.classList.remove('show-comments');
        divContainerComment.classList.add('hide-comments');
        divContainerComment.style.maxHeight = 0;
        return; // Không thực hiện các hành động khác nếu đã ẩn thẻ
    }

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
            let avtUserCmt='/images/users/DefaultAvtUser.png';
            if(comment.userSend.image){
                avtUserCmt = comment.userSend.image.urlImage;
            }
            displayComment += `
                <li class="media">
                    <a href="/profile?id=${comment.userSend.id}" class="pull-left">
                        <img src="${avtUserCmt}" alt="" class="img-circle">
                    </a>
                    <div class="media-body">
                        <div class="d-flex justify-content-between align-items-center w-100">
                            <strong class="text-gray-dark"><a href="#" class="fs-8">${comment.userSend.firstName} ${comment.userSend.lastName}</a></strong>
                            <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                        </div>
                        <span class="d-block comment-created-time">30 min ago</span>
                        <p class="fs-8 pt-2">${comment.content}</p>
                        <div class="commentLR">
                            <button type="button" class="btn btn-link fs-8">Like</button>
                            <button type="button" class="btn btn-link fs-8">Reply</button>
                        </div>
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

    divContainerComment.innerHTML = commentsFrom + displayComment;
    divContainerComment.classList.remove('hide-comments');
    divContainerComment.classList.add('show-comments');
    divContainerComment.style.maxHeight = `${divContainerComment.scrollHeight}px`;
}

function submitComment(postId) {
    const content = document.getElementById(`content-${postId}`).value;
    if (!content.trim()) {
        alert("Comment content cannot be empty!");
        return;
    }

    const data = {
        postId: postId,
        content: content
    };

    fetch('/commenter', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            addCommentToDOM(data.comment, postId);
            document.getElementById(`content-${postId}`).value = '';
        } else {
            alert("Failed to post comment. Please try again.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function addCommentToDOM(comment, postId) {
    let avtUserCmt='/images/users/DefaultAvtUser.png';
            if(comment.userSend.image){
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
                <span class="d-block comment-created-time">${new Date(comment.createdTime).toLocaleTimeString()}</span>
                <p class="fs-8 pt-2">${comment.content}</p>
                <div class="commentLR">
                    <button type="button" class="btn btn-link fs-8">Like</button>
                    <button type="button" class="btn btn-link fs-8">Reply</button>
                </div>
            </div>
        </li>
    `;

    const divContainerComment = document.getElementById(`commentslist-${postId}`);
    const commentsList = divContainerComment.querySelector('.comments-list');
    const commentForm = commentsList.querySelector('.comment-form');
    // Thêm bình luận mới ngay sau thẻ form
    commentForm.insertAdjacentHTML('afterend', commentHTML);
    divContainerComment.style.maxHeight = `${divContainerComment.scrollHeight}px`;

    const commentButton = document.querySelector(`.show-comments-btn[data-id='${postId}']`);
    let countComment = parseInt(commentButton.innerText.trim().split(" ")[0]);
    countComment += 1;
    commentButton.innerHTML = `<i class='bx bx-message-rounded mr-2'></i>${countComment}`;

    // Xóa nội dung trong input sau khi bình luận
    document.getElementById(`content-${postId}`).value = '';
}
