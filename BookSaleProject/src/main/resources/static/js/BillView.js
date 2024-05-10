// Đợi cho trang được tải hoàn thành
document.addEventListener("DOMContentLoaded", function () {
    // Lấy danh sách các hóa đơn
    // Lấy danh sách các hóa đơn có id bắt đầu bằng "bill" theo sau là một chuỗi số
    var bills = document.querySelectorAll('[id^="bills"]');

    // Duyệt qua mỗi hóa đơn
    bills.forEach(function (bill) {
        // Lấy ID của hóa đơn
        var billId = bill.id.replace('bills', '');

        // Lấy danh sách các giá trị giá sách của hóa đơn
        var prices = bill.querySelectorAll('[id^="' + billId + 'price"]');

        // Tính tổng giá trị giá sách của hóa đơn
        var totalAmount = 0;
        prices.forEach(function (price) {
            totalAmount += parseFloat(price.textContent.replace(' đ', ''));
        });

        // Cập nhật tổng giá trị cho hóa đơn
        var totalElement = bill.querySelector('[id="total' + billId + '"]');
        totalElement.textContent = "Total Amount: " + totalAmount.toLocaleString() + " đ";
    });
});

//Đánh giá sản phẩm
function ratingBook(idbook) {
    const pop = document.querySelector("#popchat");
    var score = null;
    var stars = document.querySelectorAll('.stars input[type="radio"]');
    var comment = document.getElementById('comment').value;
    
    // Lặp qua từng input sao
    stars.forEach(function(star) {
        // Kiểm tra xem input sao này có được chọn không
        if (star.checked) {
            // Lấy giá trị của input sao đã chọn
            score = parseInt(star.id.split('-')[1]);
        }
    });

    // Nếu không có điểm được chọn, bạn có thể xử lý theo ý của bạn, ví dụ: gán mặc định là 0.
    if (score === null) {
        score = 0;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/book/rating?idBook=" + idbook + "&comment=" + comment + "&score=" + score, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Phản hồi thành công
            console.log(xhr.responseText);
            pop.classList.add("active");
            setTimeout(() => {
                pop.classList.remove("active");
            }, 2000);
        }
    };
    xhr.send();
}
