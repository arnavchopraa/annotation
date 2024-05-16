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
    It parses the file and updates the text and annotations containers
    @param file: The file to be processed
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
            pdfText.innerText = data.text;
        } else {
            pdfText.innerText = ""; // Clear the container if no text is received
        }

        if(data.annotations) {
            annotationsText.innerText = data.annotations;
        } else {
            annotationsText.innerText = "";
        }

        errorMessage.innerText = "";
    })
    .catch(error => {
        errorMessage.innerText = "An error occurred: " + error.message;
    });
}