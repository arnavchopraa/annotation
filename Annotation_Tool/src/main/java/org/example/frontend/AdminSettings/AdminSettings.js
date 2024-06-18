const token = localStorage.getItem('token');

/**
    Animate the screenshots when they come into view.
*/
document.addEventListener('DOMContentLoaded', function() {
    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('in-view');
            } else {
                entry.target.classList.remove('in-view');
            }
        });
    }, { threshold: 0.5 });

    const images = document.querySelectorAll('.step-image');
    images.forEach(image => {
        observer.observe(image);
    });
});

/**
    Show the file name when a file is selected.
*/
document.addEventListener('DOMContentLoaded', () => {
    const zipInput = document.getElementById('zipInput');
    const zipName = document.getElementById('zipName');
    const formData = new FormData();
    var zipUploaded = false;
    var csvUploaded = false;
    var xlsxUploaded = false;

    zipInput.addEventListener('change', () => {
        if (zipInput.files.length > 0) {
            displaySavedPopUp("Your files have been uploaded successfully!");
            console.log("zip uploaded!\n");

            zipName.textContent = zipInput.files[0].name;
            formData.append("zipFile", zipInput.files[0]);
            zipUploaded = true;
            if(zipUploaded == true && csvUploaded == true && xlsxUploaded == true)
                communicate(formData);
        } else {
            zipName.textContent = 'No file chosen';
            zipUploaded = false;
        }
    });

    const csvInput = document.getElementById('csvInput');
    const csvName = document.getElementById('csvName');

    csvInput.addEventListener('change', () => {
        if (csvInput.files.length > 0) {
            displaySavedPopUp("Your file has been uploaded successfully!");
            console.log("csv uploaded!\n");

            csvName.textContent = csvInput.files[0].name;
            formData.append("csvFile", csvInput.files[0]);
            csvUploaded = true;
            if(zipUploaded == true && csvUploaded == true && xlsxUploaded == true)
                communicate(formData);
        } else {
            csvUploaded = false;
            csvName.textContent = 'No file chosen';
        }
    });

    const xlsxInput = document.getElementById('xlsxInput');
    const xlsxName = document.getElementById('xlsxName');

    xlsxInput.addEventListener('change', () => {
        if (xlsxInput.files.length > 0) {
            displaySavedPopUp("Your file has been uploaded successfully!");
            console.log("excel uploaded!\n");

            xlsxName.textContent = xlsxInput.files[0].name;
            formData.append("xlsxFile", xlsxInput.files[0]);
            xlsxUploaded = true;
            if(zipUploaded == true && csvUploaded == true && xlsxUploaded == true)
                communicate(formData);
        } else {
            xlsxUploaded = false;
            xlsxName.textContent = 'No file chosen';
        }
    });
});

function communicate(formData) {
    console.log("send?");

    var endpoint = "http://localhost:8080/admin/files";
    fetch(endpoint, {
        method: "POST",
        headers: {
            'Authorization': `Bearer ${token}`
        },
        body: formData
    })
    .then(response => {
        if(response.ok) {
            return response.json();
        } else {
            throw new Error("Failed to fetch response");
        }
    })
    .catch(error => {
        displayErrorPopUp("An error occurred while uploading the files. Please try again later.", false);
    })
}

document.getElementById('downloadTXT').addEventListener('click', function() {
    var endpoint = "http://localhost:8080/admin/bulkdownload"

    fetch(endpoint, {
        method: 'GET',
        headers: {
            "Content-Type": "application/zip"
        }
    })
    .then(response => {
        if(response.ok) {
            displaySavedPopUp("All submissions have been downloaded successfully!")
            return response.blob()
        }
        else
            throw new Error("Couldn't fetch file - bulk download")
    })
    .then(bytes => {
        var blob = new Blob([bytes], {type: 'application/zip'})
        var a = document.createElement('a')

        a.download = 'download.zip'
        a.style = 'display: none'
        let url = window.URL.createObjectURL(blob)
        a.href = url
        a.click()
        a.remove()
        window.URL.revokeObjectURL(url)
    })
    .catch(error => console.error(error))
})

document.getElementById('deleteALL').addEventListener('click', function() {
    Swal.fire({
        title: 'Are you sure?',
        text: 'Are you sure you want to delete ALL submissions from the database? Note that this process is irreversible.',
        icon: 'warning',
        iconColor: '#bd3233',
        color: '#a6a6a6',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete them!',
        cancelButtonText: 'No, cancel!',
        customClass: {
            popup: 'popup-container',
            title: 'popup-title',
            confirmButton: 'popup-confirm-delete-button',
            cancelButton: 'popup-cancel-button'
        },
        buttonsStyling: false,
    }).then((result) => {
        if (result.isConfirmed) {
            var endpoint = "http://localhost:8080/admin/deleteall"

            fetch(endpoint, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    displaySavedPopUp("All submissions have been deleted successfully!")
                } else {
                    throw new Error('Deleting failed')
                }
            })
            .catch(e => console.error('Error:', e));
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            displayCancelPopUp("Your submissions are safe!")
        }
    })
    .catch (error => console.error(error));
})