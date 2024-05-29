const fileInput = document.getElementById('fileInput');
const pdfObject = document.getElementById('pdfObject');

/**
    * Detect when a file has been inputted and render the PDF file.
**/
fileInput.addEventListener('change', function(e) {
    var file = e.target.files[0];
    if (!file)
        return;

    // render the PDF file
    pdfObject.src = URL.createObjectURL(file);
});

/**
    * Fetch the codes from the database when the page loads.
**/
document.addEventListener('DOMContentLoaded', function() {
    fetchCodes();
});

/**
    * Add the codes from the backend to the container and display them as buttons.
**/
function fetchCodes() {
    var endpoint = "http://localhost:8080/frontend/codes";
    fetch(endpoint)
    .then(response => {
        if(response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to fetch');
        }
    })
    .then(codes => {
        const codesContainer = document.getElementById('codes');

        codes.forEach(code => {
            const codeButton = document.createElement('input');
            codeButton.type = 'submit';
            codeButton.className = 'code';
            codeButton.value = code;
            codesContainer.appendChild(codeButton);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}