document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    if(userId){
        fetchPostUser(userId);
    }else{
        fetchPosts();
    }
});

function fetchPostUser(userId) {
    fetch(`/post?id=${userId}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            updatePostPage(data);
        })
        .catch(error => console.error('Error fetching status post:', error));
}

function fetchPosts(){
    fetch('/post',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displayPosts(data)
    })
}

function updatePostPage(data) {
    const divContainerPost = document.getElementById('containerpost');

    if (data.posts == null) {
        divContainerPost.innerHTML += `
            <h2 class="complex-effect-text">
                There are currently no posts. Let's share wonderful moments together
            </h2>
        `;
    } else {
        data.posts.forEach(post => {
            divContainerPost.innerHTML += createPostContent(post);
        });
    }
}

function displayPosts(data) {
    const postDisplay = $('#displaycontent');
    postDisplay.empty(); // Clear previous content

    data.posts.forEach(post => {
        const postElement = createPostContent(post);
        postDisplay.append(postElement);
    });
}

function createPostContent(post) {
    const imgUser = '#';
    const imgPost = post.image != null && post.image.urlImage != null ? post.image.urlImage : '#';
    const countComment = post.comments ? post.comments.length : 0;

    return `
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
                        <span class="d-block">3 hours ago <i class='bx bx-globe ml-3'></i></span>
                    </div>
                </div>
                <div id="displaycontent">
                    <div class="mt-3 ">
                        <p>${post.content}</p>
                        <div class="d-block mt-3">
                            <img src="${imgPost}" class="post-content">
                        </div>
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
                        <div class="dropdown-menu post-dropdown-menu">
                            <a href="#" class="dropdown-item">
                                <div class="row">
                                    <div class="col-md-2">
                                        <i class='bx bx-share-alt'></i>
                                    </div>
                                    <div class="col-md-10">
                                        <span>Share Now (Public)</span>
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="dropdown-item">
                                <div class="row">
                                    <div class="col-md-2">
                                        <i class='bx bx-share-alt'></i>
                                    </div>
                                    <div class="col-md-10">
                                        <span>Share...</span>
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="dropdown-item">
                                <div class="row">
                                    <div class="col-md-2">
                                        <i class='bx bx-message'></i>
                                    </div>
                                    <div class="col-md-10">
                                        <span>Send as Message</span>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="hide-comments" id="commentslist-${post.id}" data-id="${post.id}">
                    <!-- Comments will be loaded here -->
                </div>
            </div>
        </ul>
    `;
}