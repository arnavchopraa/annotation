// Call the function to load the SVGs when the page loads
document.addEventListener('DOMContentLoaded', function () {
    loadSVGs();
    displayAdminContent();
});


/**
    * Function to display the admin content based on the user's role
*/
function displayAdminContent() {
    const role = localStorage.getItem('role');
    console.log(`The role is ${role}`)

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

/*
    * Function to load and insert SVG content into the hidden div
*/
async function loadSVGs() {
    try {
        // Fetch the content of the SVG.html file
        const response = await fetch('../Global/HTML/SVG.html');
        const svgContent = await response.text();

        // Insert the SVG content into the div with class 'hidden'
        document.querySelector('.hidden').innerHTML = svgContent;
    } catch (error) {
        console.error('Error loading SVG content:', error);
    }
}

/*
    * Method to create an SVG
*/
function createSvgIcon(svgId) {
    const svgElement = document.getElementById(svgId).cloneNode(true);
    svgElement.removeAttribute('id'); // Remove the id to prevent duplicates in the DOM
    return svgElement;
}