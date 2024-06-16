function displaySavedPopUp(textContent) {
    Swal.fire({
        title: 'Saved!',
        text: textContent,
        icon: 'success',
        iconColor: '#588157',
        color: '#a6a6a6',
        customClass: {
            popup: 'popup-container',
            title: 'popup-title',
            confirmButton: 'popup-confirm-button'
        },
        showConfirmButton: true,
        confirmButtonText: 'OK',
        confirmButtonAriaLabel: 'OK button'
    });
}

function displayErrorPasswordPopUp (textContent) {
    Swal.fire({
        title: 'ERROR!',
        text: textContent,
        icon: 'error',
        iconColor: '#bd3233',
        color: '#a6a6a6',
        customClass: {
            popup: 'popup-container',
            title: 'popup-title',
            confirmButton: 'popup-confirm-button'
        },
        buttonsStyling: false,
        showConfirmButton: true,
        confirmButtonText: 'OK',
        confirmButtonAriaLabel: 'OK button'
    });
}