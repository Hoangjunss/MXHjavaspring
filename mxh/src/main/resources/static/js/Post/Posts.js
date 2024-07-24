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
        var content = $('#contentPost').val();
    var image = $('#imagePost')[0].files[0];
    var contentt = content.trimStart().trimEnd();
    // Kiểm tra nếu nội dung chỉ chứa khoảng trắng
    if (contentt === '' && !image) {
        alert('Please share your thoughts');
        return;
    }
        formData.append('content', content);

        if (image) {
            formData.append('image', image);
        }


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
    console.log('fetchPostUser');
    $.ajax({
        url: `/api/post?id=${userId}`,
        method: 'GET',
        success: function (data) {
            updatePostPage(data);
        },
        error: function (error) {
            console.error('Error fetching status post:', error);
        }
    });
}

function fetchPosts() {
    console.log('fetchPosts');
    $.ajax({
        url: '/api/post',
        method: 'GET',
        success: function (data) {
            displayPosts(data);
        },
        error: function (error) {
            console.error('Error fetching posts:', error);
        }
    });
}

function updatePostPage(data) {
    console.log('updatePostPage');
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
    console.log('displayPosts');
    const postDisplay = $('#displaycontent');
    postDisplay.empty(); // Clear previous content

    data.posts.forEach(post => {
        createPostContent(post);
    });
}

function createPostContent(post) {
    console.log('createPostContent');
    const postDisplay = $('#displaycontent');
    const imgUser = post.user.image!=null ?  post.user.image.urlImage : '/images/users/DefaultAvtUser.png';
    const imgPost = post.image != null ? post.image.urlImage : null;
    let imagePostUrl;
    if(post.image!=null){
        imagePostUrl = post.image.urlImage
        console.log(imagePostUrl);
    }
    const countComment = post.comments ? post.comments.length : 0;
    const timeAgo = formatTimeAgo(post.updateAt);
    const displayPost = `
        <ul class="list-unstyled" id="postuserupload"> 
            <div class="post border-bottom p-3 bg-white w-shadow">
                <div class="media text-muted pt-3">
                    <img src="${imgUser}" alt="Online user" class="mr-3 post-user-image">
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
                            <img id="postImageUrl" class="post-content" src="${post.image.urlImage}">
                        </div>` : ''}
                    </div>
                </div>
                <div class="mb-3">
                    <div class="argon-reaction">
                        <span class="like-btn">
                            <a href="#" class="post-card-buttons" id="reactions"><i data-post-id="${post.id}" class='bx bxs-like mr-2'></i></a>
                            <ul class="reactions-box dropdown-shadow">
                                <li onclick="interaction(this)" data-reaction-id="1" data-post-id="${post.id}" class="reaction reaction-like" data-reaction="Like"></li>
                                <li onclick="interaction(this)" data-reaction-id="2" data-post-id="${post.id}" class="reaction reaction-love" data-reaction="Love"></li>
                                <li onclick="interaction(this)" data-reaction-id="3" data-post-id="${post.id}" class="reaction reaction-haha" data-reaction="HaHa"></li>
                                <li onclick="interaction(this)" data-reaction-id="4" data-post-id="${post.id}" class="reaction reaction-sad" data-reaction="Sad"></li>
                                <li onclick="interaction(this)" data-reaction-id="5" data-post-id="${post.id}" class="reaction reaction-angry" data-reaction="Angry"></li>
                            </ul>
                        </span>
                    </div>
                    <button class="post-card-buttons show-comments-btn" onclick="showComment(${post.id})" data-id="${post.id}"><i class='bx bx-message-rounded mr-2'></i>${countComment}</button>
                </div>
                <div class="hide-comments" id="commentslist-${post.id}" data-id="${post.id}">
                    <!-- Comments will be loaded here -->
                </div>
            </div>
        </ul>
    `;

    postDisplay.append(displayPost);
    fetchCountInteraction(post.id);
    if(post.interactions!=null){
        handleInteractions(post);
    }

    const postImage = $('#postImage');
    if (imgPost && imgPost.trim() !== '') {
        postImage.attr('src', imgPost).show();
    }
}

function fetchInteracion(idInteraction, idPost){
    console.log('fetchInteracion');
    $.ajax({
        url: `/api/interaction?id=${idInteraction}`,
        method: 'GET',
        success: function (data) {
            updateReactionUI(data.interac.reactionId, idPost);
        },
        error: function (error) {
            console.error('Error fetching interactions:', error);
        }
    });
}

async function fetchCountInteraction(postId) {
    console.log('fetchCountInteraction');

            try {
                fetch('/api/countinteraction?id='+postId,{
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
                    $(`i[data-post-id="${postId}"]`).empty().append(`<span>${data.countInteract}</span>`);
                })
                .catch(error => {
                    console.error('Error fetching chat messages:', error.message);
                    // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
                });
            } catch (error) {
                console.error('Đã xảy ra lỗi khi lấy ID người dùng:', error.message);
                // Xử lý lỗi nếu cần
            }
}

function previewImage(event) {
    console.log('previewImage');

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

async function handleInteractions(post) {
    console.log('handleInteractions');
    if (post.interactions.id == null) {
        for (const interact of post.interactions) {
            try {
                const userid = await fetchCurrentUser();
                if(interact.user.id ==userid){
                    fetchInteracion(interact.id, post.id);
                }
            } catch (error) {
                console.error('Đã xảy ra lỗi khi lấy ID người dùng:', error.message);
                // Xử lý lỗi nếu cần
            }
        }
    }
}

function fetchCurrentUser() {
    console.log('fetchCurrentUser');
    return fetch('/api/usercurrent', {
        method: 'GET'
    }).then(response => {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    }).then(data => {
        return data.id;
    }).catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        return null; // Trả về null hoặc một giá trị mặc định khác trong trường hợp lỗi
    });
}

function interaction(event){
    console.log('interaction');
    const reactionId = event.getAttribute("data-reaction-id");
    const postId = event.getAttribute("data-post-id");
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

            fetchCountInteraction(postId);
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
    console.log('updateReactionUI');
    
const postElement = document.querySelector(`i[data-post-id="${postId}"]`);

if (postElement) {
    // Xóa tất cả các lớp CSS hiện tại
    postElement.className = 'bx mr-2'; // Chỉ giữ lại lớp cơ bản 'bx'
    switch(reactionId.toString()) {
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
