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
        alert("kazakhstan ugrasaj nambambierovke")
    })
}