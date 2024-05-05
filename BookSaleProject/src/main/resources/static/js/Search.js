function handleSearch(event) {
    if (event.key === 'Enter') {
        var keyword = document.getElementById("keyword").value; // Lấy giá trị từ ô input

        // Encode keyword để đảm bảo không chứa ký tự không hợp lệ trong URL
        var encodedKeyword = encodeURIComponent(keyword);

        // Tạo URL với tham số keyword đã được mã hóa
        var url = "/book/search?keyword=" + encodedKeyword;

        // Chuyển hướng người dùng đến trang tìm kiếm với tham số keyword
        window.location.href = url;
    }
} 
// Hàm để gửi yêu cầu đến máy chủ và nhận các gợi ý sách
function getBookSuggestions(keyword) {
    // Thực hiện yêu cầu đến máy chủ, có thể sử dụng AJAX hoặc Fetch API
    // Đây là một ví dụ đơn giản sử dụng Fetch API
    fetch(`/book/suggestions?keyword=${keyword}`)
        .then(response => response.json())
        .then(data => {
            // Hiển thị các gợi ý sách cho người dùng
            displayBookSuggestions(data);
        })
        .catch(error => {
            console.error('Error fetching book suggestions:', error);
        });
}

// Hàm để hiển thị các gợi ý sách cho người dùng
function displayBookSuggestions(suggestions) {
    // Xóa các gợi ý cũ trước khi hiển thị các gợi ý mới
    clearBookSuggestions();

    // Hiển thị các gợi ý sách mới
    const suggestionList = document.getElementById('bookSuggestions');
    suggestions.forEach(book => {
        const listItem = document.createElement('li');
        listItem.textContent = book.title; // Giả sử 'title' là thuộc tính chứa tên sách
        suggestionList.appendChild(listItem);
    });
}

// Hàm để xóa các gợi ý sách cũ
function clearBookSuggestions() {
    const suggestionList = document.getElementById('bookSuggestions');
    suggestionList.innerHTML = ''; // Xóa nội dung bên trong thẻ <ul>
}

// Hàm để xử lý sự kiện khi người dùng nhập vào ô tìm kiếm
function handleSearchInput(event) {
    const keyword = event.target.value;
    if (keyword.trim() !== '') {
        getBookSuggestions(keyword);
    } else {
        clearBookSuggestions();
    }
}

// Gán sự kiện cho ô tìm kiếm
const searchInput = document.getElementById('keyword');
searchInput.addEventListener('input', handleSearchInput);
