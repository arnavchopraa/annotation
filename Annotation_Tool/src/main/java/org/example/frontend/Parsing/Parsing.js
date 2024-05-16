const fileInput = document.getElementById('fileInput');

const pdfText = document.getElementById('pdfText');
const annotationsText = document.getElementById('annotationsText');
const errorMessage = document.getElementById('error')

/**
    * Detect when a file has been inputted and call the process method.
**/
fileInput.addEventListener('change', function(e) {
    var file = e.target.files[0];
    if (!file)
        return;

    processFile(file)
});

/**
    * Method using fetch API to communicate with backend.
    * It parses the file and updates the text and annotations containers.

    @param file: The file to be processed
**/
function processFile(file) {
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
        fileInput.name = data.fileName;
    })
    .catch(error => {
        errorMessage.innerText = "An error occurred: " + error.message;
    });
}


const pdfConvert = document.getElementById('convertToPDFButton');
const txtConvert = document.getElementById('convertToTxtButton');

/**
    * Detect when the convert to .txt button is clicked.
    * Initiate the download process for the file containing the text and annotations.
**/
txtConvert.addEventListener('click', function() {
    const text = pdfText.innerText;
    const annotations = annotationsText.innerText;

    var pdfContent = "Text: \n" + text + "\n\n" + "Annotations: \n" + annotations;

    var blob = new Blob([pdfContent], { type: 'text/plain' , endings: 'transparent'});
    var anchor = document.createElement('a');

    anchor.download = fileInput.name;
    anchor.href = (window.webkitURL || window.URL).createObjectURL(blob);
    anchor.dataset.downloadurl = ['text/plain', anchor.download, anchor.href].join(':');
    anchor.click();
    anchor.remove();
});

/**
    * Detect when the convert to .pdf button is clicked.
    * Initiate the download process for the generated PDF file.
**/
pdfConvert.addEventListener('click', function () {
    const text = pdfText.innerText;
    const annotations = annotationsText.innerText;

    exportPDF(text, annotations);
});

/**
     * Method using fetch API to communicate with backend.
     * It sends the text and annotations to the backend to generate a PDF file.

     * @param text: The text to be included in the PDF
     * @param annotations: The annotations to be included in the PDF
 **/
function exportPDF(text, annotations) {
    const formData = new FormData();
    formData.append("text", text);
    formData.append("annotations", annotations);

    var endpoint = "http://localhost:8080/frontend/export";

    fetch(endpoint, {
        method: "POST",
        body: formData
    })
    .then(response => {
        if(response.ok) {
            return response.blob();
        } else {
            throw new Error('Failed to fetch');
        }
    })
    .then(response => {
        var url = window.URL.createObjectURL(response);
        var anchor = document.createElement('a');

        anchor.href = url;
        anchor.download = fileInput.name + ".pdf";

        document.body.appendChild(anchor);
        anchor.click();

        window.URL.revokeObjectURL(url);
    })
    .catch(error => {
        errorMessage.innerText = "An error occurred: " + error.message;
    });
}