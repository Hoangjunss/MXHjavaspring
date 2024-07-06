document.addEventListener("DOMContentLoaded", function () {
    fetch('/post',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displayPosts(data)
    })

    fetch('/status',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
    })
});
function displayPosts(data){
       const postDisplay=$('#displaycontent');
       postDisplay.empty();
       data.posts.forEach(post=>{
        const imgUser = '#';
                const imgPost =  post.image!=null && post.image.urlImage != null ? post.image.urlImage : '#';
                const countComment = post.comments ? post.comments.length : 0;
         const postElement = $(`<ul class="list-unstyled" id="postuserupload"> 
                                                <div class="post border-bottom p-3 bg-white w-shadow"> <!-- id ul cua post -->
                                                    <div class="media text-muted pt-3">
                                                        <!-- Avartar user -->
                                                        <img src="${imgUser}" alt="Online user" class="mr-3 post-user-image">
                                                        <form th:action="@{/hidepost}" method="post">
                                                            <input type="hidden" name="id" value="${post.id}">
                                                            <button type="submit">Ẩn</button>
                                                        </form>
                                                        <div class="media-body pb-3 mb-0 small lh-125">
                                                            <div class="d-flex justify-content-between align-items-center w-100">
                                                                <!--Hien thi Name user -->
                                                                <a href="${post.user.id}" class="text-gray-dark post-user-name">${post.user.firstName} ${post.user.lastName}</a>
                                                            </div>
                                                            <!-- Display time update -->
                                                            <span class="d-block">3 hours ago <i class='bx bx-globe ml-3'></i></span>
                                                        </div>
                                                    </div>
                                                    <div id="displaycontent">
                                                            <!-- Content -->
                                                            <div class="mt-3 ">
                                                            <p>${post.content}</p>
                                                        <!--End Content -->
                                                        <!-- Image cua post -->
                                                        <div class="d-block mt-3">
                                                            <img src="${imgPost}" class="post-content" alt="post image">
                                                        </div>
                                                        <!-- End Image -->
                                                        </div>
                                                    </div>
                                                    <!-- End time -->
                                                    <div class="mb-3">
                                                        <!-- Reactions -->
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
                                                        
                                                        <!-- End Reactions -->
                                                        <button class="post-card-buttons show-comments" onclick="showComment(${post.id})" data-id="${post.id}"><i class='bx bx-message-rounded mr-2'></i>${countComment}</button>
                                                        <!-- Share -->
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
                                                        <!-- End Share -->
                                                    </div>
                                                </div>
                                            </ul>
            `)
            postDisplay.append(postElement);
       });
}
function showComment(id){
    alert(id+" POSTID")
    fetch('/comment?id='+id,{
        method:'GET'
    })  .then(response => response.json()) 
    .then(data => {
        alert(data.comments.length + " comments");
        displayComment(data)
    })
}   

function displayComment(data){
   const commentlist=$('.media');
   data.comments.forEach(element=>{
       const display=$(`
         <a href="#" class="pull-left">
                                                                    <img th:src="@{/images/users/user-2.jpg}" alt="" class="img-circle">
                                                                </a>
                                                                <div class="media-body">
                                                                    <div class="d-flex justify-content-between align-items-center w-100">
                                                                        <strong class="text-gray-dark"><a href="#" class="fs-8" text="${element.userSend.firstName} +' '+${element.userSend.lastName}"></a></strong>
                                                                        <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                                                                    </div>
                                                                    <span class="d-block comment-created-time">30 min ago</span>
                                                                    <p class="fs-8 pt-2" text="${element.content}">
                                                                    </p>
                                                                    <div class="commentLR">
                                                                        <button type="button" class="btn btn-link fs-8">Like</button>
                                                                        <button type="button" class="btn btn-link fs-8">Reply</button>
                                                                    </div>
                                                                </div>
        `)
        commentlist.append(display);
   })
}
function showNotification(){
    fetch('/notifications',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        displayNotification(data)
    })
}
function displayNotification(data){
    const notification=$('.dropContent');
    data.forEach(element=>{
        const display=$(`
            <li >
                                            <div class="col-md-2 col-sm-2 col-xs-2">
                                                <div class="notify-img">
                                                    <img th:src="@{/images/users/user-10.png}" alt="notification user image">
                                                </div>
                                            </div>
                                            <div class="col-md-10 col-sm-10 col-xs-10">
                                                <a th:href="@{${element.url}}" class="notification-user"">${element.user.firstName}</a> 
                                                <span class="notification-type">${element.message}</span>
                                                <a href="#" class="notify-right-icon">
                                                    <i class='bx bx-radio-circle-marked'></i>
                                                </a>
                                                <p class="time">
                                                    <span class="badge badge-pill badge-primary"><i class='bx bxs-group'></i></span> 
                                                    <!-- You can calculate and display time here -->
                                                </p>
                                            </div>
                                        </li>
            `)
    })
}
function displaystatus(data){
    const status=$('#StatusId');
    data.forEach(element=>{
        const display=$(`
            <option  value="${element.id}">${element.name}</option>
            `)
    })
}