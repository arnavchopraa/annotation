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
            if(data.status == 200) {
                console.log("OK!");
                return {success: true, message: data.text()}
            }
            else {
                return data.text()
            }
        })
    .then(data => {
            if(!data.success) {
                errorMessage.innerHTML = data
                errorMessage.style.color = "red"
                return {error: true}
            } else {
                return data.message
            }
        })
    .then(data => {
            if(!data.error) {
                pdfText.innerHTML = data
                errorMessage.innerHTML = ""
            }
    })
}