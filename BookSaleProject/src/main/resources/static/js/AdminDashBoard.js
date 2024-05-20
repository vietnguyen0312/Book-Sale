var timeout;
var originalBookList = []; // This will hold the original book list

// Function to fetch and store the original book list
function fetchOriginalBookList() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/admin/recomendation?keyword=", true); // Assuming this endpoint returns all books when keyword is empty
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            originalBookList = JSON.parse(xhr.responseText);
        }
    };
    xhr.send();
}

function handleInput() {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        var keyword = document.getElementById("search-input").value.trim();
        var encodedKeyword = encodeURIComponent(keyword);
        if (encodedKeyword.length >= 1) {
            var url = "/admin/recomendation?keyword=" + encodedKeyword;
            var xhr = new XMLHttpRequest();
            xhr.open("GET", url, true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var result = JSON.parse(xhr.responseText);
                    displaySearchResults(result);
                }
            };
            xhr.send();
        } else {
            resetTable();
        }
    }, 500);
}

function displaySearchResults(result) {
    var tableBody = document.getElementById("book-table-body");
    tableBody.innerHTML = ""; // Clear previous table rows

    result.forEach((book, index) => {
        var row = document.createElement("tr");

        var indexCell = document.createElement("td");
        indexCell.textContent = index + 1; // Index starts from 1
        row.appendChild(indexCell);

        var nameCell = document.createElement("td");
        nameCell.textContent = book.name;
        row.appendChild(nameCell);

        var bookTypeCell = document.createElement("td");
        bookTypeCell.textContent = book.bookType.name;
        row.appendChild(bookTypeCell);

        var authorCell = document.createElement("td");
        authorCell.textContent = book.author;
        row.appendChild(authorCell);

        var priceCell = document.createElement("td");
        priceCell.textContent = book.price + " đ";
        row.appendChild(priceCell);

        var slCell = document.createElement("td");
        slCell.textContent = "" + book.sl;
        row.appendChild(slCell);

        var actionCell = document.createElement("td");
        var editButton = document.createElement("button");
        editButton.className = "action-btn edit-btn";
        editButton.style.color = "black";
        editButton.textContent = "Sửa";
        editButton.onmouseover = function () {
            this.style.color = 'white';
            this.style.textDecoration = 'underline';
        };
        editButton.onmouseout = function () {
            this.style.color = 'black';
            this.style.textDecoration = 'none';
        };
        actionCell.appendChild(editButton);
        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

function resetTable() {
    displaySearchResults(originalBookList);
}

// Fetch the original book list when the page loads
document.addEventListener("DOMContentLoaded", fetchOriginalBookList);

// Call handleInput function when input changes
document.getElementById("search-input").addEventListener("input", handleInput);
