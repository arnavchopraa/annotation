const token = localStorage.getItem('token');

 document.addEventListener('DOMContentLoaded', function() {
    fetchCodes();
});

/**
    * Add the codes from the backend to the container and display them as buttons.
**/
function fetchCodes() {
    var endpoint = "http://localhost:8080/annotations/";
    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to fetch');
        }
    })
    .then(codes => {
        const codesContainer = document.getElementById('codes-wrapper');

        codes.forEach(code => {
            /* Should contain the code ID and the 2 icons for edit and delete */
            const codeContainer = document.createElement('div');
            codeContainer.className = 'code-container';

            /* Code ID */
            const codeButton = document.createElement('input');
            codeButton.type = 'submit';
            codeButton.className = 'code-button';
            codeButton.value = code.id;

            /* Edit and Delete icons */
            const codeControls = document.createElement('div');
            codeControls.className = 'code-controls';

            const editWrapper = createIcons('edit-icon', code.id);
            const deleteWrapper = createIcons('delete-icon', code.id);

            codeControls.appendChild(editWrapper);
            codeControls.appendChild(deleteWrapper);

            codeContainer.appendChild(codeButton);
            codeContainer.appendChild(codeControls);

            /* Code Content */
            const codeDescription = document.createElement('div');
            codeDescription.className = 'code-description';
            codeDescription.innerHTML = boldTitle(code.codeContent);

            /* Add the code and the description to the wrapper */
            const codeWrapper = document.createElement('div');
            codeWrapper.className = 'code-wrapper';
            codeWrapper.appendChild(codeContainer);
            codeWrapper.appendChild(codeDescription);

            /* Add the code wrapper to the container */
            codesContainer.appendChild(codeWrapper);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}

/*
    * Method to find the code title and style it.
*/
function boldTitle (text) {
    const firstPeriodIndex = text.indexOf('.');
    if (firstPeriodIndex !== -1) {
        const boldText = text.substring(0, firstPeriodIndex + 1);
        const remainingText = text.substring(firstPeriodIndex + 1);
        return `<span style="font-weight: 700; color: #474747;">${boldText}</span>${remainingText}`;
    } else {
        return text; // If no period is found, return the text as is
    }
}

/*
    * Method to create the edit and delete icons
*/
function createIcons(svgId, codeId) {
    const wrapper = document.createElement('div');
    wrapper.className = 'icon-wrapper';

    const button = document.createElement('a');
    button.className = 'icon';
    button.setAttribute('code', codeId)
    button.appendChild(createSvgIcon(svgId));

    if (svgId === 'delete-icon') {
        button.addEventListener('click', function() {
            deleteCode(codeId);
        });
    } else if (svgId === 'edit-icon') {
        button.setAttribute('formVisible', false)
        button.addEventListener('click', function() {
            showEditForm(codeId, button);
        });
    }

    wrapper.appendChild(button);

    return wrapper;
}

/*
    Method that removes a code from the database ( and page )
*/
function deleteCode(codeId) {
    var endpoint = `http://localhost:8080/annotations/${codeId}`;
        fetch(endpoint, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.ok) {
                // removing code directly from DOM
                const codeWrapper = document.querySelector(`.code-button[value='${codeId}']`).closest('.code-wrapper');
                codeWrapper.remove();
            } else {
                throw new Error('Failed to delete');
            }
        })
        .catch(error => console.error('Error deleting code: ', error));
}

/*
    Event listener for adding a code
*/
document.getElementById("add-code-button").addEventListener('click', function() {
    addCode(document.getElementById("code-id").value, document.getElementById("code-text").value)
});

/*
    Method to add a code to the database
*/
function addCode(id, text) {
    var endpoint = "http://localhost:8080/annotations/";

    var newCode = {
        id: id,
        codeContent: text
    };

    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
            body: JSON.stringify(newCode)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 400) {
            displayErrorPopUp("Feedback shortcut code already exists.", false);
            timeout(2000);
            console.log('Code already exists');
        } else {
            throw new Error('Failed to add code');
        }
    })
    .then(addedCode => {
        console.log('Code added successfully:', addedCode);

        location.reload();

        //we could either rewrite all the codes or directly refresh the page
        //i've chosen to keep the refresh for now
    })
}

/*
    Method that shows the form where the user can edit a code
*/
function showEditForm(codeId, button) {
    if(button.getAttribute("formVisible") === 'false') {    //checking if we do not have another form open for this code
        button.setAttribute('formVisible', true)
        const codeWrapper = document.querySelector(`.code-button[value='${codeId}']`).closest('.code-wrapper');
        const codeDescription = codeWrapper.querySelector('.code-description');

        const editForm = document.createElement('div');
        editForm.className = 'edit-form';

        const textArea = document.createElement('textarea');
        textArea.value = codeDescription.textContent;
        textArea.className = 'edit-text'
        editForm.appendChild(textArea);

        const saveButton = document.createElement('button');
        saveButton.textContent = 'Save';
        saveButton.className = 'save-button icon'
        saveButton.addEventListener('click', function() {
            editCode(codeId, textArea.value);
            codeDescription.innerHTML = boldTitle(textArea.value); //update visualization
            codeWrapper.removeChild(editForm); //remove the form after saving
            button.setAttribute('formVisible', false) //make it possible to edit again
        });
        editForm.appendChild(saveButton);

        codeWrapper.appendChild(editForm);
    }
}

/*
    Method that edits the code's description in the database
*/
function editCode(id, text) {
    var endpoint = `http://localhost:8080/annotations/${id}`;

    var updatedCode = {
        id: id,
        codeContent: text
    };

    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(updatedCode)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to update code');
        }
    })
    .then(updatedCode => {
        console.log('Code updated successfully:', updatedCode);
    })
    .catch(error => console.error('Error updating code:', error));
}
