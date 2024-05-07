function removeAnimation(element) {
    element.removeAttribute('data-mdb-toggle');
}

function addAnimation(element) {
    element.setAttribute('data-mdb-toggle', 'animation');
}

// Khi mở modal
$('button[data-bs-toggle="modal"]').on('click', function () {
    // Thêm class để tắt hover vào thẻ card
    $(this).closest('.card').addClass('no-hover');
});

// Khi đóng modal
$('.modal').on('hidden.bs.modal', function () {
    // Loại bỏ class để khôi phục lại hover
    $('.card.no-hover').removeClass('no-hover');
});



