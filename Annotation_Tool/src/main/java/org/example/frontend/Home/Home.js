const fileInput = document.getElementById('fileInput');
const pdfObject = document.getElementById('pdfObject');

fileInput.addEventListener('change', function(e) {
    var file = e.target.files[0];
    if (!file)
        return;

    // render the PDF file
    pdfObject.src = URL.createObjectURL(file);
});
