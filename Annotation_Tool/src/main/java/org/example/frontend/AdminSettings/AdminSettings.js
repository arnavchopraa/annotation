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

    zipInput.addEventListener('change', () => {
        if (zipInput.files.length > 0) {
            zipName.textContent = zipInput.files[0].name;
        } else {
            zipName.textContent = 'No file chosen';
        }
    });

    const csvInput = document.getElementById('csvInput');
    const csvName = document.getElementById('csvName');

    csvInput.addEventListener('change', () => {
        if (csvInput.files.length > 0) {
            csvName.textContent = csvInput.files[0].name;
        } else {
            csvName.textContent = 'No file chosen';
        }
    });

    const xlsxInput = document.getElementById('xlsxInput');
    const xlsxName = document.getElementById('xlsxName');

    xlsxInput.addEventListener('change', () => {
        if (xlsxInput.files.length > 0) {
            xlsxName.textContent = xlsxInput.files[0].name;
        } else {
            xlsxName.textContent = 'No file chosen';
        }
    });
});