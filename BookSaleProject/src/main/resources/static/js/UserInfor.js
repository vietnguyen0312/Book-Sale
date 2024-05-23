$(document).ready(function () {
    $('.account-settings-links a').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});

function changePassword() {

    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const newPasswordAgain = document.getElementById("newPasswordAgain").value;

    if (!currentPassword || !newPassword || !newPasswordAgain) {
        alert("Vui lòng điền vào tất cả các trường.");
        return;
    }

    if (newPassword !== newPasswordAgain) {
        alert("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        return;
    }
    const pop = document.querySelector("#popchat");
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/user/changePassword?newPassword=" + newPassword + "$currentPassword=" + currentPassword, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Phản hồi thành công
                console.log(xhr.responseText);
                pop.classList.add("active");
                setTimeout(() => {
                    pop.classList.remove("active");
                }, 2000);
            } else {
                // Phản hồi lỗi
                alert(xhr.responseText); // Hiển thị thông báo từ máy chủ
            }
        }
    };
    xhr.send();
}

