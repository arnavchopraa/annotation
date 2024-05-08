const fileInput = document.getElementById('fileInput');

const pdfText = document.getElementById('pdfText');
const annotationsText = document.getElementById('annotationsText');
const errorMessage = document.getElementById('error')

fileInput.addEventListener('change', function(e) {
    var file = e.target.files[0];
    if (!file)
        return;

    process(file)
    // render the PDF file => change after text extraction is finalized
    // pdfObject.src = URL.createObjectURL(file);
});

/**
    Method using fetch API to communicate with backend
**/
function process(file) {
    const formData = new FormData();
    formData.append("file", file);

    var endpoint = "http://localhost:8080/frontend";
    fetch(endpoint, {
        method: "POST",
        body: formData
    })
    .then(response => {
            if(response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch');
            }
        })
    .then(data => {
        if(data.text) {
            pdfText.innerHTML = data.text.replace(/\n/g, '<br>'); // Replace newline characters with <br> tags
        } else {
            pdfText.innerHTML = ""; // Clear the container if no text is received
        }

        if(data.annotations) {
            annotationsText.innerHTML = data.annotations.replace(/\n/g, '<br>'); // Replace newline characters with <br> tags
        } else {
            annotationsText.innerHTML = ""; // Clear the container if no annotations are received
        }

        errorMessage.innerHTML = "";
    })
    .catch(error => {
        errorMessage.innerHTML = "An error occurred: " + error.message;
        errorMessage.style.color = "red";
    });
}