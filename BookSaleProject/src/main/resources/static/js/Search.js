function showSearchBar() {
    var searchBar = document.getElementById("search-bar");
    var displayButton = document.getElementById("dropbtn");
    var all = document.getElementById("1");
    if (searchBar.style.display === "none") {
        searchBar.style.display = "block";
    }
}
function cancelBar() {
    var searchBar = document.getElementById("search-bar");
    var inputKeyword = document.getElementById("keyword");
    var suggestionBox = document.getElementById("suggestionBox");
    if (searchBar.style.display === "block") {
        searchBar.style.display = "none";
        inputKeyword.value = "";
        suggestionBox.innerHTML = "";
    }
}

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

let timeout = null;

function handleInput() {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        var keyword = document.getElementById("keyword").value.trim();
        var encodedKeyword = encodeURIComponent(keyword);
        if (encodedKeyword.length >= 1) {
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
        // Kiểm tra xem keyword có rỗng không sau khi nhận kết quả từ server
        if (keyword === "") {
            var suggestionsDiv = document.getElementById("suggestionBox");
            suggestionsDiv.style.display = "none"; // Ẩn các gợi ý khi không có ký tự nào trong ô input
        }
    }, 500)
}


function displaySuggestions(suggestions) {
    var suggestionsDiv = document.getElementById("suggestionBox");
    suggestionsDiv.innerHTML = ""; // Xóa nội dung cũ
    var maxSuggestions = Math.min(6, suggestions.length); // Số lượng tối đa 6 phần tử để hiển thị
    for (var i = 0; i < maxSuggestions; i++) {
        var suggestion = document.createElement("div");
        suggestion.style.paddingTop = "10px";
        suggestion.style.paddingBottom = "10px";

        // Tạo phần tử <img> cho ảnh sách
        var bookImage = document.createElement("img");
        bookImage.src = "images/" + suggestions[i].img;
        suggestion.appendChild(bookImage); // Thêm ảnh vào phần tử suggestion

        // Tạo phần tử <a> cho tên sách
        var bookLink = document.createElement("a");
        bookLink.textContent = suggestions[i].name; // Thay "name" bằng trường thông tin bạn muốn hiển thị
        bookLink.href = "/getBookById/" + suggestions[i].id; // Đặt href cho thẻ <a>
        suggestion.appendChild(bookLink); // Thêm tên sách vào phần tử suggestion

        // Bắt sự kiện click cho suggestion div
        suggestion.addEventListener("click", function () {
            // Redirect đến href của thẻ <a> khi div được nhấp vào
            window.location.href = this.querySelector('a').href;
        });

        suggestionsDiv.appendChild(suggestion);
    }
    suggestionsDiv.style.display = maxSuggestions > 0 ? "block" : "none"; // Hiển thị suggestionsDiv nếu có gợi ý, ẩn nếu không có
}



