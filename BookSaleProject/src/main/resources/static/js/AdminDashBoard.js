const ctx = document.getElementById('myChart');

new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            borderWidth: 1
        }]
    },
    options: {
        plugins: {
            title: {
                display: true,
                text: 'Biểu đồ doanh thu của sách theo thể loại',
                position: 'bottom',
                font: {
                    size: 30
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});