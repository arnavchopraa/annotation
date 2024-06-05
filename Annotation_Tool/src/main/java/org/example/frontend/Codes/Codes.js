document.addEventListener('DOMContentLoaded', function() {
    fetchCodes();
});

/**
    * Add the codes from the backend to the container and display them as buttons.
**/
function fetchCodes() {
    var endpoint = "http://localhost:8080/frontend/codes";
    fetch(endpoint)
    .then(response => {
        if(response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to fetch');
        }
    })
    .then(codes => {
        const codesContainer = document.getElementById('codes-wrapper');

        /*
        codes-wrapper {
            code-wrapper {
                code-container {
                    code-button
                    code-controls {
                        edit-button
                        delete-button
                    }
                }
                code-description
            }
        }*/

        codes.forEach(code => {
            const codeContainer = document.createElement('div');
            codeContainer.className = 'code-container';

            const codeButton = document.createElement('input');
            codeButton.type = 'submit';
            codeButton.className = 'code-button';
            codeButton.value = code.id;

            const codeDescription = document.createElement('div');
            codeDescription.className = 'code-description';
            codeDescription.innerText = code.codeContent;

            const text = code.codeContent;
            const firstPeriodIndex = text.indexOf('.');

            if (firstPeriodIndex !== -1) {
                const boldText = text.substring(0, firstPeriodIndex + 1);
                const remainingText = text.substring(firstPeriodIndex + 1);

                codeDescription.innerHTML = `<span style="font-weight: 700; color: #474747;">${boldText}</span>${remainingText}`;
            } else {
                codeDescription.innerText = text; // If no period is found, display the text as is
            }

            const codeControls = document.createElement('div');
            codeControls.className = 'code-controls';

            const deleteButton = document.createElement('input');
            deleteButton.type = 'submit';
            deleteButton.className = 'btn';
            deleteButton.value = 'Delete';

            const editButton = document.createElement('input');
            editButton.type = 'submit';
            editButton.className = 'btn';
            editButton.value = 'Edit';

            codeControls.appendChild(editButton);
            codeControls.appendChild(deleteButton);

            codeContainer.appendChild(codeButton);
            codeContainer.appendChild(codeControls);

            const codeWrapper = document.createElement('div');
            codeWrapper.className = 'code-wrapper';
            codeWrapper.appendChild(codeContainer);
            codeWrapper.appendChild(codeDescription);

            codesContainer.appendChild(codeWrapper);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}