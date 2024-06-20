alert('Load data js');

$(document).ready(function() {
    // Lấy danh sách bài viết khi trang được tải
    $.ajax({
        url: '/getPosts', // URL đến endpoint lấy danh sách bài viết
        type: 'GET',
        success: function(response) {
            response.forEach(function(post) {
                var postHtml = `
                <!-- Content -->
                <div class="mt-3 ">
                    <p>${post.content}</p>
                <!--End Content -->
                <!-- Image -->
                    <div class="d-block mt-3">
                        <img src="${post.imageUrl}" class="post-content" alt="post image">
                    </div>
                `;
                $('#displaycontent').append(postHtml); // Thêm bài viết vào danh sách
            });
        },
        error: function(error) {
            console.error(error);
        }
    });

    // Gửi bài viết mới
$('#post-form').on('submit', function(event) {
    event.preventDefault(); // Ngăn chặn form submit mặc định

    var userId = $('#user-id').val();
    var postContent = $('#post-content').val();
    var postImage = $('#post-image')[0].files[0];

    var formData = new FormData();
    formData.append('userId', userId);
    formData.append('postContent', postContent);
    formData.append('postImage', postImage);

    $.ajax({
        url: '/uploadpost', // URL đến endpoint xử lý upload post
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            // Xử lý phản hồi thành công từ server
            var postHtml = `
                <!-- Content -->
                <div class="mt-3 ">
                    <p>${post.content}</p>
                <!--End Content -->
                <!-- Image -->
                    <div class="d-block mt-3">
                        <img src="${post.imageUrl}" class="post-content" alt="post image">
                    </div>
            `;
            $('#displaycontent').prepend(postHtml); // Thêm bài viết mới vào đầu danh sách
        },
        error: function(error) {
            console.error(error);
        }
    });
});
});

