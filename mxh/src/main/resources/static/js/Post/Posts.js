$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    if (userId) {
        fetchPostUser(userId);
    } else {
        fetchPosts();
    }

    $('#uploadPostUser').submit(function (event) {
        event.preventDefault();

        var formData = new FormData();
        var status = $('#StatusId').val();
        var content = $('#contentPost').val();
    var image = $('#imagePost')[0].files[0];
    var contentt = content.trimStart().trimEnd();
    // Kiểm tra nếu nội dung chỉ chứa khoảng trắng
    if (contentt === '' && !image) {
        alert('Please share your thoughts');
        return;
    }
        formData.append('StatusId', status);
        formData.append('content', content);

        if (image) {
            formData.append('image', image);
        }

        console.log(image);

        $.ajax({
            url: '/api/uploadpostuser',
            method: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.success) {
                    location.reload();
                } else {
                    alert("Error: " + (data.message || "Unknown error occurred"));
                }
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    });
});

function fetchPostUser(userId) {
    $.ajax({
        url: `/api/post?id=${userId}`,
        method: 'GET',
        success: function (data) {
            console.log(data);
            updatePostPage(data);
        },
        error: function (error) {
            console.error('Error fetching status post:', error);
        }
    });
}

function fetchPosts() {
    $.ajax({
        url: '/api/post',
        method: 'GET',
        success: function (data) {
            console.log(data);
            displayPosts(data);
        },
        error: function (error) {
            console.error('Error fetching posts:', error);
        }
    });
}

function updatePostPage(data) {
    const divContainerPost = $('#displaycontent');

    if (data.posts == null) {
        divContainerPost.append(`
            <h2 class="complex-effect-text">
                There are currently no posts. Let's share wonderful moments together
            </h2>
        `);
    } else {
        data.posts.forEach(post => {
            createPostContent(post);
        });
    }
}

function displayPosts(data) {
    const postDisplay = $('#displaycontent');
    postDisplay.empty(); // Clear previous content

    data.posts.forEach(post => {
        createPostContent(post);
    });
}

function createPostContent(post) {
    const postDisplay = $('#displaycontent');
    const imgUser = post.user.image!=null ?  post.user.image.urlImage : '/images/users/DefaultAvtUser.png';
    const imgPost = post.image != null ? post.image.urlImage : null;
    const countComment = post.comments ? post.comments.length : 0;
    const timeAgo = formatTimeAgo(post.updateAt);
    const displayPost = `
        <ul class="list-unstyled" id="postuserupload"> 
            <div class="post border-bottom p-3 bg-white w-shadow">
                <div class="media text-muted pt-3">
                    <img src="${imgUser}" alt="Online user" class="mr-3 post-user-image">
                    <form th:action="@{/hidepost}" method="post">
                        <input type="hidden" name="id" value="${post.id}">
                        <button type="submit">Ẩn</button>
                    </form>
                    <div class="media-body pb-3 mb-0 small lh-125">
                        <div class="d-flex justify-content-between align-items-center w-100">
                            <a href="/profile?id=${post.user.id}" class="text-gray-dark post-user-name">${post.user.firstName} ${post.user.lastName}</a>
                        </div>
                        <span class="d-block">${timeAgo} <i class='bx bx-globe ml-3'></i></span>
                    </div>
                </div>
                <div id="displaycontent">
                    <div class="mt-3 ">
                        <p>${post.content}</p>
                        ${imgPost ? `<div class="d-block mt-3">
                            <img id="postImage" class="post-content" src="${imgPost}">
                        </div>` : ''}
                    </div>
                </div>
                <div class="mb-3">
                    <div class="argon-reaction">
                        <span class="like-btn">
                            <a href="#" class="post-card-buttons" id="reactions"><i class='bx bxs-like mr-2'></i></a>
                            <ul class="reactions-box dropdown-shadow">
                                <li class="reaction reaction-like" data-reaction="Like"><button class="reaction-button" data-reaction-id="1" data-post-id="${post.id}"></button></li>
                                <li class="reaction reaction-love" data-reaction="Love"><button class="reaction-button" data-reaction-id="2" data-post-id="${post.id}"></button></li>
                                <li class="reaction reaction-haha" data-reaction="HaHa"><button class="reaction-button" data-reaction-id="3" data-post-id="${post.id}"></button></li>
                                <li class="reaction reaction-wow" data-reaction="Wow"><button class="reaction-button" data-reaction-id="4" data-post-id="${post.id}"></button></li>
                                <li class="reaction reaction-sad" data-reaction="Sad"><button class="reaction-button" data-reaction-id="5" data-post-id="${post.id}"></button></li>
                                <li class="reaction reaction-angry" data-reaction="Angry"><button class="reaction-button" data-reaction-id="6" data-post-id="${post.id}"></button></li>
                            </ul>
                        </span>
                    </div>
                    <button class="post-card-buttons show-comments-btn" onclick="showComment(${post.id})" data-id="${post.id}"><i class='bx bx-message-rounded mr-2'></i>${countComment}</button>
                    <div class="dropdown dropup share-dropup">
                        <a href="#" class="post-card-buttons" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class='bx bx-share-alt mr-2'></i> Share
                        </a>
                    </div>
                </div>
                <div class="hide-comments" id="commentslist-${post.id}" data-id="${post.id}">
                    <!-- Comments will be loaded here -->
                </div>
            </div>
        </ul>
    `;

    postDisplay.append(displayPost);

    const postImage = $('#postImage');
    if (imgPost && imgPost.trim() !== '') {
        postImage.attr('src', imgPost).show();
    }
}

function previewImage(event) {
    const imagePreview = document.getElementById('imagePreview');
    const removeImage = document.getElementById('remove-image-span');
    
    const reader = new FileReader();
    reader.onload = function() {
        imagePreview.src = reader.result;
        imagePreview.style.display = 'block';
        removeImage.style.display = 'block';
    };
    reader.readAsDataURL(event.target.files[0]);
}


function removeImage() {
    alert("CLICK");
    const imagePreview = $('#imagePreview');
    const imagePost = $('#imagePost');
    const removeImage = $('#remove-image-span');
    
    imagePreview.attr('src', 'images/icons/theme/post-image.png');
    imagePreview.hide();
    imagePost.val('');  // Reset the file input
    removeImage.hide();
}

