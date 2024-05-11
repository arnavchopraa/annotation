const fileInput = document.getElementById('fileInput');
// const pdfObject = document.getElementById('pdfObject');
const pdfText = document.getElementById('pdfText');
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
    .then(data => {
            console.log(data)
            if(data.status == 200) { // If succeeds, pass flag to mark success
                return {success: true, message: data.text()}
            }
            else { // If fails, pass error text
                return data.text()
            }
        })
    .then(data => {
            if(!data.success) { // If fails, display error text through a label and pass flag
                errorMessage.innerHTML = data
                errorMessage.style.color = "red"
                return {error: true}
            } else { // If succeeds, pass the message
                return data.message
            }
        })
    .then(data => {
            if(!data.error) { // If succeeds, hide the error message and display text
                pdfText.innerHTML = data
                errorMessage.innerHTML = ""
            }
    })
}