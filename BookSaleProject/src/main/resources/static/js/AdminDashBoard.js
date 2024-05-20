function handleInput() {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        var keyword = document.getElementById("keyword").value.trim();
        var encodedKeyword = encodeURIComponent(keyword);
        if (encodedKeyword.length >= 1) {
            var url = "/admin/recomendation?keyword=" + encodedKeyword;
            var xhr = new XMLHttpRequest();
            xhr.open("GET", url, true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var suggestions = JSON.parse(xhr.responseText);
                    // nhảy vào hàm hoặc in dữ liệu suggestion vào table
                }
            };
            xhr.send();
        }
        // Kiểm tra xem keyword có rỗng không sau khi nhận kết quả từ server
        if (keyword === "") {
            var suggestionsDiv = document.getElementById("suggestionBox");
            suggestionsDiv.style.display = "none"; // Ẩn các gợi ý khi không có ký tự nào trong ô input
        }
    }, 500)
}

function displaySearchResults(results) {
    // Xử lý và hiển thị kết quả tìm kiếm trong bảng HTML, ví dụ:
    var tableBody = document.getElementById("search-results-body");
    tableBody.innerHTML = ""; // Xóa bất kỳ dữ liệu cũ nào trong bảng
    results.forEach(book => {
        var row = tableBody.insertRow();
        var nameCell = row.insertCell(0);
        var authorCell = row.insertCell(1);
        // Thêm các ô dữ liệu khác cần thiết ở đây
        nameCell.textContent = book.name;
        authorCell.textContent = book.author;
        // Thêm các ô dữ liệu khác cần thiết ở đây
    });
}