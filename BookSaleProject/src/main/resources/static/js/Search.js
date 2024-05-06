function handleSearch(event) {
    if (event.key === 'Enter') {
        var keyword = document.getElementById("keyword").value.trim();
        var sanitizedKeyword = keyword.replace(/[^\w\s]/gi, '');
        var encodedKeyword = encodeURIComponent(sanitizedKeyword);
        if (encodedKeyword === '') {
            alert('Bạn chưa nhập từ khóa. (Không tính các ký tự đặc biệt vào độ dài từ khóa)');
        } else {
            var url = "/book/search?keyword=" + encodedKeyword;

            window.location.href = url;
        }
    }
}

function handleInput() {
    var keyword = document.getElementById("keyword").value;
    var encodedKeyword = encodeURIComponent(keyword);
    if (encodedKeyword.length >= 1){
        var url = "/book/recomendation?keyword=" + encodedKeyword;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var suggestions = JSON.parse(xhr.responseText);
                displaySuggestions(suggestions);
            }
        };
        xhr.send();
    }

    // Kiểm tra xem keyword có rỗng không
    if (keyword.trim() === "") {
        var suggestionsDiv = document.getElementById("suggestionBox");
        suggestionsDiv.style.display = "none"; // Ẩn các gợi ý khi không có ký tự nào trong ô input
    }
}


function displaySuggestions(suggestions) {
    var suggestionsDiv = document.getElementById("suggestionBox");
    suggestionsDiv.innerHTML = ""; // Xóa nội dung cũ
    var maxSuggestions = Math.min(6, suggestions.length); // Số lượng tối đa 6 phần tử để hiển thị
    for (var i = 0; i < maxSuggestions; i++) {
        var suggestion = document.createElement("div");
        suggestion.style.paddingTop = "20px";
        suggestion.style.paddingBottom = "20px";
        // Tạo phần tử <img> cho ảnh sách
        var bookImage = document.createElement("img");
        bookImage.src = "'../static/images/" + suggestions[i].img + "'";
        bookImage.style.width = "80px"; // Thiết lập chiều rộng của ảnh
        bookImage.style.height = "40px";
        bookImage.style.display = "inline-flex";
        suggestion.appendChild(bookImage); // Thêm ảnh vào phần tử suggestion

        // Tạo phần tử <a> cho tên sách
        var bookLink = document.createElement("a");
        bookLink.textContent = suggestions[i].name; // Thay "name" bằng trường thông tin bạn muốn hiển thị
        bookLink.href = "/getBookById/" + suggestions[i].id; // Đặt href cho thẻ <a>
        suggestion.appendChild(bookLink); // Thêm tên sách vào phần tử suggestion

        suggestionsDiv.appendChild(suggestion);
    }
    suggestionsDiv.style.display = maxSuggestions > 0 ? "block" : "none"; // Hiển thị suggestionsDiv nếu có gợi ý, ẩn nếu không có
}


