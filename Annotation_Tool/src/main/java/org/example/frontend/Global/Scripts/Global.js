const role = localStorage.getItem('role');

window.onload = displayAdminContent();

function displayAdminContent() {
    const adminContent = document.getElementsByClassName("admin");
    if(role === "admin") {
        // Loop through each element with the class "admin"
        for (var i = 0; i < adminContent.length; i++) {
            // Set the display style to 'flex' to show the element
            adminContent[i].style.display = 'flex';
        }
    } else {
        for (var i = 0; i < adminContent.length; i++) {
            // Set the display style to 'none' to hide the element
            adminContent[i].style.display = 'none';
        }
    }
}