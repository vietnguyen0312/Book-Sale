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
    window.location.href = "/bill/viewPayment?selectedIds=" + selectedIdsString;
}

