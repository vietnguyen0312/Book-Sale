var acc = document.getElementsByClassName("accordion");
        var i;

        for (i = 0; i < acc.length; i++) {
            acc[i].addEventListener("click", function () {
                this.classList.toggle("active");
                var panel = this.nextElementSibling;
                if (panel.classList.contains('open')) {
                    // Đóng panel
                    panel.style.maxHeight = panel.scrollHeight + "px";
                    panel.classList.remove('open');
                    // Chờ cho hiệu ứng đóng hoàn thành trước khi đặt max-height thành null
                    panel.addEventListener('transitionend', function () {
                        panel.style.maxHeight = null;
                    });
                } else {
                    // Đóng tất cả các panel trước khi mở panel này
                    var allPanels = document.querySelectorAll('.panel');
                    allPanels.forEach(function (element) {
                        element.style.maxHeight = null;
                        element.classList.remove('open');
                    });
                    // Mở panel
                    panel.style.maxHeight = panel.scrollHeight + "px";
                    panel.classList.add('open');
                }
            });
        }