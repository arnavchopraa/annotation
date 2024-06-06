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
        .codes-wrapper {
            .code-wrapper {
                .code-container {
                    .code-button
                    .code-controls {
                        .icon-wrapper {
                            .icon {
                                #edit-icon (svg)
                            }
                        }
                        .icon-wrapper {
                            .icon {
                                #delete-icon (svg)
                            }
                        }
                    }
                }
                code-description
            }
        }*/

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

            const editWrapper = createIcons('edit-icon');
            const deleteWrapper = createIcons('delete-icon');

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
    * Method to create an SVG
*/
function createSvgIcon(svgId) {
    const svgElement = document.getElementById(svgId).cloneNode(true);
    svgElement.removeAttribute('id'); // Remove the id to prevent duplicates in the DOM
    return svgElement;
}

/*
    * Method to create the edit and delete icons
*/
function createIcons(svgId) {
    const wrapper = document.createElement('div');
    wrapper.className = 'icon-wrapper';

    const button = document.createElement('a');
    button.className = 'icon';
    button.appendChild(createSvgIcon(svgId));

    if (svgId === 'delete-icon') {
        button.href = 'Codes.html';
    }

    wrapper.appendChild(button);

    return wrapper;
}
