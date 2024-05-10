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
function ratingBook(idbook, comment, score) {
    const pop = document.querySelector("#popchat");
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/book/rating?idBook=" + idbook + "&comment=" + comment + "$score=" + score, true);
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
