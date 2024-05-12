const fileInput = document.getElementById('fileInput');

const pdfText = document.getElementById('pdfText');
const annotationsText = document.getElementById('annotationsText');
const errorMessage = document.getElementById('error')

const exportButton = document.getElementById('exportAsPDFButton');

/**
    Detect when a file has been inputted and call the process method
**/
fileInput.addEventListener('change', function(e) {
    var file = e.target.files[0];
    if (!file)
        return;

    process(file)
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
        fileInput.name = data.fileName;
    })
    .catch(error => {
        errorMessage.innerText = "An error occurred: " + error.message;
    });
}


/**
    Detect when the export button has been pressed and call the exportPDF method
**/
exportButton.addEventListener('click', function() {
    const text = pdfText.innerText;
    const annotations = annotationsText.innerText;

    var pdfContent = "Text: \n" + text + "\n\n" + "Annotations: \n" + annotations;

    exportPDF(text, annotations, pdfContent);
});

function exportPDF(text, annotations, pdfContent) {
    var blob = new Blob([pdfContent], { type: 'text/plain' }),
        anchor = document.createElement('a');

    anchor.download = "hello.txt";
    anchor.href = (window.webkitURL || window.URL).createObjectURL(blob);
    anchor.dataset.downloadurl = ['application/pdf', anchor.download, anchor.href].join(':');
    anchor.click();
    /*var blob = new Blob([pdfContent], {type: 'text/plain', endings: 'transparent'});
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob, fileInput.name);
    } else {
        var e = document.createEvent('MouseEvents'),
        a = document.createElement('a');
        a.download = "download.txt";
        a.href = window.URL.createObjectURL(blob);
        a.dataset.downloadurl = ['text/plain', a.download, a.href].join(':');
        e.initEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
        a.dispatchEvent(e);
    }*/
}

/**
 * Method using fetch API to communicate with backend
 * It sends the text and annotations to the backend to generate a PDF file
 * and initiates the download process for the generated PDF file
 * @param text: The text to be included in the PDF
 * @param annotations: The annotations to be included in the PDF
 **/
/*function exportPDF(text, annotations) {

    const formData = new FormData();
    formData.append("text", text);
    formData.append("annotations", annotations);

    var endpoint = "http://localhost:8080/frontend/export"; // Endpoint for exporting PDF
    fetch(endpoint, {
        method: "POST",
        body: formData
    })
    .then(response => {
        if(response.ok) {
            // Initiate download process
            response.blob().then(blob => {
                const url = window.URL.createObjectURL(new Blob([blob.text]));
                const a = document.createElement('a');
                a.href = url;
                a.download = 'exported.pdf'; // Set the filename for download
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
            });
        } else {
            throw new Error('Failed to export PDF');
        }
    })
    .catch(error => {
        errorMessage.innerText = "An error occurred: " + error.message;
    });
}*/


