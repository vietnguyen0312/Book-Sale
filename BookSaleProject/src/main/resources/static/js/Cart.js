function toggleCheckboxes(source) {
    checkboxes = document.getElementsByName('checkBoxNoLabel');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        checkboxes[i].checked = source.checked;
    }
    checkBoxFunction();
}

var selectedIds = [];

function checkBoxFunction() {
    var checkboxes = document.getElementsByName('checkBoxNoLabel');
    var paymentButton = document.querySelector('.btn-primary');
    var orderTotal = 0;
    var atLeastOneChecked = false;
    selectedIds = [];

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            atLeastOneChecked = true;
            selectedIds.push(checkboxes[i].id);
            var price = parseFloat(checkboxes[i].parentNode.parentNode.querySelector('.price').innerText);
            orderTotal += price;
        }
    }

    var orderTotalElement = document.querySelector('.order-total');
    if (orderTotalElement) {
        orderTotalElement.innerText = orderTotal + 'đ';
    }

    if (atLeastOneChecked) {
        paymentButton.removeAttribute('disabled'); // Kích hoạt nút "Thanh toán"
    } else {
        paymentButton.setAttribute('disabled', 'disabled'); // Vô hiệu hóa nút "Thanh toán"
    }

    window.onload = toggleCheckboxes;
}

// Hàm để xử lý sự kiện khi nút thanh toán được nhấn
function handlePayment() {
    // Chuyển mảng selectedIds sang chuỗi để gửi đến servlet
    var selectedIdsString = selectedIds.join(",");

    // Gửi các ID đã chọn đến servlet bằng cách chuyển hướng URL
    window.location.href = "/bill/payment?selectedIds=" + selectedIdsString;
}

var input;
var currentValue = {};

window.onload = function () {
    input = document.querySelectorAll('input[name^="quantity"]');
    input.forEach(function (inputField) {
        currentValue[inputField.name] = parseInt(inputField.value);
    });
    // Thực hiện các hành động khác sau khi trang đã được tải
};

function adjustQuantity(id, delta) {
    var input = document.querySelector('input[name="quantity' + id + '"]');
    var currentValue = parseInt(input.value);
    var newValue = currentValue + delta;
    // Đảm bảo giá trị không nhỏ hơn giá trị tối thiểu
    if (newValue > parseInt(input.min) || newValue < parseInt(input.max)) {
        input.value = newValue;
        updateCart(id, newValue);
    } else {
        alert("Số lượng không hợp lệ");
    }
    // Cập nhật giá trị mới vào input
}

function updateCart(id, quantity) {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        var remainingQuantityText = document.querySelector('p[name="SL' + id + '"]').textContent;
        var matchResult = remainingQuantityText.match(/\d+/);
        var remainingQuantity = matchResult ? parseInt(matchResult[0]) : 0;

        var inputField = document.querySelector('input[name="quantity' + id + '"]');
        var currentValueId = inputField.name;

        if (quantity <= 0 || quantity > remainingQuantity) {
            alert("Số lượng không hợp lệ");
            inputField.value = currentValue[currentValueId];
            return;
        }

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/cart/update?id=" + id + "&quantity=" + quantity, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // Phản hồi thành công
                console.log(xhr.responseText);

                currentValue[currentValueId] = quantity;

                var priceElement = document.getElementById('price' + id);
                var pricePerItem = parseFloat(priceElement.value);

                // Tính tổng giá trị
                var totalPrice = pricePerItem * quantity;
                var totalPrice = (pricePerItem * quantity).toFixed(0);

                // Cập nhật tổng giá trị vào phần tử HTML
                var totalPriceElement = document.getElementById('totalPrice' + id);
                totalPriceElement.textContent = totalPrice.toLocaleString() + ' đ'; // Format số tiền

                checkBoxFunction();
            }
        };
        xhr.send();
    }, 500)
}

function deleteRow(id, button) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/cart/delete?id=" + id, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Phản hồi thành công
            console.log(xhr.responseText);
            var row = button.parentNode.parentNode;
            row.remove();
        }
    };
    xhr.send();
}
