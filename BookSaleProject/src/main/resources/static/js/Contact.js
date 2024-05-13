function sendFeedback() {
    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var comment = document.getElementById("comment").value;
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/client/feedback?name=" + name + "&email=" + email + "&comment=" + comment, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Phản hồi thành công
            console.log(xhr.responseText);
            // Thay đổi nội dung của thẻ p thành cảm ơn
            document.getElementById("popchat").innerHTML = "Cảm ơn bạn đã gửi liên hệ!";
            // Sửa đổi phông chữ
            document.getElementById("popchat").style.fontSize = "xx-large";
            document.getElementById("popchat").style.color = "#ffd700";
            // Đặt thời gian để chuyển lại như cũ sau 5 giây
            setTimeout(function () {
                document.getElementById("popchat").innerHTML = "Nếu bạn có thắc mắc gì, có thể gửi yêu cầu cho chúng tôi, và chúng tôi sẽ liên lạc lại với bạn sớm nhất có thể.";
                document.getElementById("popchat").style.fontSize = "medium";
                document.getElementById("popchat").style.color = "#808080";
            }, 5000);
        } else {
            console.log(xhr.responseText);
        }
    };
    xhr.send();
}
