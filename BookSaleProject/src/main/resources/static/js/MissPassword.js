var code;

function checkEmail(email) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/user/missPassword?email=" + email, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                code = xhr.responseText;
                // Ẩn div hiện tại
                document.getElementById("emailForm").style.display = "none";
                // Hiện div để nhập mã xác nhận
                document.getElementById("verifyForm").style.display = "block";
            } else {
                // Phản hồi lỗi
                alert(xhr.responseText); // Hiển thị thông báo từ máy chủ

                var emailInput = document.getElementById("email");
                emailInput.value = ""; // Xóa giá trị hiện tại
                emailInput.focus();
            }
        }
    };
    xhr.send();
}


const inputs = document.querySelectorAll(".otp-field > input");
const button = document.querySelector("#submitBtn");

window.addEventListener("load", () => inputs[0].focus());
button.setAttribute("disabled", "disabled");

inputs[0].addEventListener("paste", function (event) {
    event.preventDefault();

    const pastedValue = (event.clipboardData || window.clipboardData).getData(
        "text"
    );
    const otpLength = inputs.length;

    for (let i = 0; i < otpLength; i++) {
        if (i < pastedValue.length) {
            inputs[i].value = pastedValue[i];
            inputs[i].removeAttribute("disabled");
            inputs[i].focus;
        } else {
            inputs[i].value = ""; // Clear any remaining inputs
            inputs[i].focus;
        }
    }
});

inputs.forEach((input, index1) => {
    input.addEventListener("keyup", (e) => {
        const currentInput = input;
        const nextInput = input.nextElementSibling;
        const prevInput = input.previousElementSibling;

        if (currentInput.value.length > 1) {
            currentInput.value = "";
            return;
        }

        if (
            nextInput &&
            nextInput.hasAttribute("disabled") &&
            currentInput.value !== ""
        ) {
            nextInput.removeAttribute("disabled");
            nextInput.focus();
        }

        if (e.key === "Backspace") {
            inputs.forEach((input, index2) => {
                if (index1 <= index2 && prevInput) {
                    input.setAttribute("disabled", true);
                    input.value = "";
                    prevInput.focus();
                }
            });
        }

        button.classList.remove("active");
        button.setAttribute("disabled", "disabled");

        var codeNumber = parseInt(code);

        const inputsNo = inputs.length;
        if (!inputs[inputsNo - 1].disabled && inputs[inputsNo - 1].value !== "") {
            var codeInput = '';
            inputs.forEach(function (input) {
                codeInput += input.value;
            });
            if (codeNumber == parseInt(codeInput)) {
                button.classList.add("active");
                button.removeAttribute("disabled");
                return;
            }
        }
    });
});

function showRefreshPass() {
    document.getElementById("verifyForm").style.display = "none";
    // Hiện div để nhập mã xác nhận
    document.getElementById("refreshPassForm").style.display = "block";
}

function checkPassword() {
    var password = document.getElementById("password").value;
    var passwordAgain = document.getElementById("passwordAgain").value;

    if (password !== passwordAgain) {
        alert("Mật khẩu nhập lại không khớp.");
        return false; // Ngăn chặn form được gửi đi
    }
    return true; // Cho phép form được gửi đi nếu hai mật khẩu khớp nhau
}