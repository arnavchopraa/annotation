// Call the function to load the SVGs when the page loads
let jwt = localStorage.getItem('token')
document.addEventListener('DOMContentLoaded', function () {
    loadSVGs();
    displayAdminContent();
});


/**
    * Function to display the admin content based on the user's role
*/
function displayAdminContent() {
    if(window.location.href === 'http://localhost:63342/' +
        'developing-an-annotation-tool-to-train-an-llm-to-provide-automatic-feedback-on-students-theses/' +
        'Annotation_Tool/src/main/java/org/example/frontend/Login/Login.html')
            return ;
    var role;
    fetch("http://localhost:8080/users/me", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        }
    }).then(response => {
        // Check if the response is successful (status code 200)

        if (response.ok) {
            // Parse the JSON response
            return response.json();
        } else {
            // If the response is not successful, throw an error

            throw new Error('Failed to fetch user');
        }
    })
        .then(obj =>{
            // I don't see how we display admin functionality, this is if it doesn't happen:
            display(obj.role)
        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.log(error)
        });
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

function display(userRole) {
    const adminContent = document.getElementsByClassName("admin");
    if(userRole === "admin") {
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