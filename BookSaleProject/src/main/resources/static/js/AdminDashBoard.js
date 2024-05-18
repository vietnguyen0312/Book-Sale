function openTab(event, tabId) {
    // Hide all tab content
    var contents = document.getElementsByClassName('tab-content');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }

    // Remove active state from all tab links
    var options = document.getElementsByClassName('nav-option');
    for (var i = 0; i < options.length; i++) {
        options[i].classList.remove('active');
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabId).classList.add('active');
    event.currentTarget.classList.add('active');
}