const role = localStorage.getItem('role')
let getName = localStorage.getItem('file')
var allCodes
const subBtn = document.querySelector('#submitFile')
const arr = document.querySelectorAll('.admin')
let newFile;
const prevButton = document.getElementById('prevFile')
const nextButton = document.getElementById('nextFile')
let token = localStorage.getItem('token')
let locked = false
let noSave = false

prevButton.addEventListener('click', function() {

    if(locked === true) {
        if(confirm("You have unsaved changes. Are you sure you want to leave?")) {
            prev()
            locked = false
        }
    } else {
        prev()
    }
})

function prev() {
    let length
    if(localStorage.getItem('whichList') === 'center')
        length = localStorage.getItem('sublength')
    else
        length = localStorage.getItem('rightlength')
    let index = localStorage.getItem("curidx")
    if(index != 0) {
        index = Number(index) - 1
        localStorage.setItem('curidx', index)
        let prevDoc
        if(localStorage.getItem('whichList') === 'center')
            prevDoc = localStorage.getItem('submission'+index)
        else
            prevDoc = localStorage.getItem('rightsub'+index)
        localStorage.setItem('file', prevDoc)
        fetchSub(prevDoc)
    }
}

nextButton.addEventListener('click', function() {

    if(locked === true) {
        if(confirm("You have unsaved changes. Are you sure you want to leave?")) {
            next()
            locked = false
        }
    }
    else {
        next()
    }
})

function next() {
    let length
    if(localStorage.getItem('whichList') === 'center')
        length = localStorage.getItem('sublength')
    else
        length = localStorage.getItem('rightlength')
    let index = localStorage.getItem('curidx')
    if(index != length - 1) {
        index = Number(index) + 1
        localStorage.setItem('curidx', index)
        let nextDoc
        if(localStorage.getItem('whichList') === 'center')
            nextDoc = localStorage.getItem('submission'+index)
        else
            nextDoc = localStorage.getItem('rightsub'+index)
        localStorage.setItem('file', nextDoc)
        fetchSub(nextDoc)
    }
}

function fetchSub(name) {
    var endpoint = `http://localhost:8080/submissions/${name}`
    fetch(endpoint, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok)
            return response.json()
        else
            throw new Error("Failed to retrieve current submission")
    })
    .then(submission => {
        adobePreview(submission)
    })
    .catch(error => console.error(error))
}

/**
    * Fetch the codes from the database when the page loads.
**/
document.addEventListener('DOMContentLoaded', function() {
    fetchCodes();
    loadPassedFile();
});

subBtn.addEventListener('click', () => {
    if(newFile === undefined) {

    }
    newFile.submitted = true;
    newFile.lastSubmitted = new Date().toUTCString()
    console.log(newFile)
    fetch(`http://localhost:8080/submissions/${getName}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(newFile)
    }).then(
        function (response) {
            if(response.status == 200) {
                displaySavedPopUp("Your file has been successfully submitted!")

                console.log('file has been submitted')
            }
        }
    ).catch(e => {
        console.log(e)
    })
})

/**
    * Add the codes from the backend to the container and display them as buttons.
**/
function fetchCodes() {
    var endpoint = "http://localhost:8080/annotations/";
    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
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
        allCodes = codes
        var even = false;

        const codesContainer = document.getElementById('codes');

        codes.forEach(code => {
            const codeButton = document.createElement('div');
            codeButton.className = 'code';
            codeButton.textContent = code.id;

            const codeDescription = document.createElement('div');
            codeDescription.className = 'code-description';
            codeDescription.textContent = code.codeContent;

            if (even == true) {
                codeDescription.classList.add('even');
            } else {
                codeDescription.classList.remove('even');
            }
            even = !even;

            codeButton.appendChild(codeDescription);

            codesContainer.appendChild(codeButton);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}

function loadPassedFile() {
    // getting the file from database
    if(getName === null) {
        displayErrorPopUp("You have not accessed any file.", true) // redirected to the last accessed page automatically
    }
    let sessionFile
    fetch( `http://localhost:8080/submissions/${getName}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok) {
            //sessionFile = response.json()
            return response.json()
        } else {
            throw new Error('Failed to fetch');
        }
    }).then(sub => {
        newFile = sub;
        newFile.submitted = false
        adobePreview(sub)
    })
    .catch(error => console.error('Error fetching codes: ', error));
}

function adobePreview(passedFile) {
    const fileName = passedFile.fileName
    const submissionEmail = passedFile.id

    // this transforms the base64 encoding to PDF file
    let binaryString = atob(passedFile.fileSubmission);
    let len = binaryString.length;
    let bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    let blob = new Blob([bytes], { type: 'application/pdf' })
    var adobeDCView = new AdobeDC.View({clientId: "543b9355cff44d19821857d8b0ddfb96", divId: "pdfContainer"});
    adobeDCView.previewFile({
            content:{location: {url: URL.createObjectURL(blob)}},
            metaData:{fileName: fileName, id: fileName}
        },
        {
            enableAnnotationAPIs: true,
            includePDFAnnotations: true
        });

    //options for the saving of the document
    const saveOptions = {
        autoSaveFrequency: 0,
        enableFocusPolling: false,
        showSaveButton: true
    }

    //registering the save functionality
    adobeDCView.registerCallback(
        AdobeDC.View.Enum.CallbackType.SAVE_API,
        function(metaData, content, options) {
            locked = false

            let uint8Array = new Uint8Array(content)
            let blob = new Blob([uint8Array], { type: 'application/pdf' })

            let binaryString = '';
            for (let i = 0; i < uint8Array.length; i++) {
                binaryString += String.fromCharCode(uint8Array[i]);
            }
            let base64string = btoa(binaryString)

            newFile.fileSubmission = base64string
            newFile.lastEdited = new Date().toUTCString()

            var endpoint = `http://localhost:8080/submissions/${submissionEmail}`
            fetch(endpoint, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(newFile)
            }).then(
                function (response) {
                    if(response.status == 200) {
                        console.log('GOOD JOB PAUL')
                    }
                }
            ).catch(e => {
                console.log(e)
            })

            return new Promise((resolve, reject) => {
                resolve({
                    code: AdobeDC.View.Enum.ApiResponseCode.SUCCESS,
                    data: {
                        metaData: Object.assign(metaData, {fileName: fileName})
                    }
                });
            });
        },
        saveOptions);


    //promise used to listen to changes
    const previewFilePromise = adobeDCView.previewFile({
            content:{location: {url: URL.createObjectURL(blob)}},
            metaData:{fileName: fileName, id: fileName}
        },
        {
            enableAnnotationAPIs: true,
            includePDFAnnotations: true
        });

    //changes that we listen to
    const eventOptions = {
        listenOn: [
            "ANNOTATION_ADDED", "ANNOTATION_UPDATED", "ANNOTATION_DELETED"
        ]
    }

    //listening to changes in the pdf document
    previewFilePromise
        .then((adobeViewer) => {
            adobeViewer.getAnnotationManager()
                .then(annotationManager => {
                    //getting all of the annotations
                    annotationManager.getAnnotations()
                        .then(result => {
                            console.log("All annotations: ", result);
                        })
                        .catch(e => {
                            console.log(e);
                        });

                    annotationManager.registerEventListener(
                        function (event) {
                            console.log(event.type, event.data)
                            if (event.type === 'ANNOTATION_ADDED') {
                                console.log("Annotation added\nAll annotations: ", annotationManager.getAnnotations())
                                if(locked != true)
                                    verifyLock()
                                replaceCodes(annotationManager, event.data)
                            } else if (event.type === 'ANNOTATION_UPDATED') {
                                if(locked != true)
                                    verifyLock()
                                console.log("Annotation updated\nAll annotations: ", annotationManager.getAnnotations())
                                replaceCodes(annotationManager, event.data)
                            } else if (event.type === 'ANNOTATION_DELETED') {
                                if(locked != true)
                                    verifyLock()
                                console.log("Annotation deleted\nAll annotations: ", annotationManager.getAnnotations())
                            }
                        },
                        eventOptions
                    );

                })
                .catch(e => {
                    console.log(e);
                });
        })
        .catch(e => {
            console.log(e);
        });
}

/*
    Method that adds the description to codes added / deleted in anottations
*/
function replaceCodes(annotationManager, data) {
    let text = data.bodyValue.trim()

    allCodes.forEach((code) => {
        if(code.id === text) {
            data.bodyValue = data.bodyValue.trim() + " - " + code.codeContent
            annotationManager.updateAnnotation(data)
                .then (()=> console.log("Success"))
            .catch(error => console.log("Error when updating annotations: ", error));
        }
    });
}

function verifyLock() {
    const endpoint = `http://localhost:8080/submissions/${getName}/getLock`

    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
    }
    }).then(response => {
        if(response.ok) {
            return response.json()
        }
        else {
            throw new Error("Failed to retrieve current submission lock")
        }
    }).then(lock => {
        if(lock === true) {
            alert("The file is locked by someone else! Your changes were NOT saved!")
            noSave = true
            window.location.href = "../Dashboard/Dashboard.html"
        }
        else {
            lockFile()
        }
    })
}

function lockFile() {
    locked = true

    const endpoint = `http://localhost:8080/submissions/${getName}/lock`

    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        if(response.ok) {
            return response.json()
        }
        else {
            throw new Error("Failed to lock current submission")
        }
    }).then(sub => {
        console.log("successfully locked file")
    })
}

window.onunload = function(e) {
    if(noSave === true) {       //if the user tried to change a file that is already locked, we should not unlock it
        return undefined
    }

    locked = false

    unlockFile()

    return undefined
}

function unlockFile() {

    locked = false

    const endpoint = `http://localhost:8080/submissions/${getName}/unlock`

    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        if(response.ok) {
            return response.json()
        }
        else {
            throw new Error("Failed to unlock current submission. Please contact an admin if this persists!")
        }
    }).then(sub => {
        console.log("successfully unlocked file")
    })

    //we need to make sure code is synchronous in order to be sure that the endpoint gets executed
    sleep(500)
}

//helper sleep function used to ensure synchronous execution
function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}

/*
    Method that exports the annotated file as a PDF.
*/
document.getElementById('exportButton').addEventListener('click', function() {
    var endpoint = `http://localhost:8080/submissions/export/${newFile.id}`
    fetch(endpoint, {
        method: 'GET',
        headers: {
            "Content-Type": "application/pdf",
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok) {
            displaySavedPopUp("Your submission has been downloaded successfully!");
            return response.blob()
        }
        else
            throw new Error("Couldn't fetch downloads")
    })
    .then(bytes => {
        var blob = new Blob([bytes], {type: 'application/pdf'})
        var a = document.createElement('a')

        a.download = `${newFile.id} - ${newFile.fileName}`
        a.style = 'display: none'
        let url = window.URL.createObjectURL(blob)
        a.href = url
        a.click()
        a.remove()
        window.URL.revokeObjectURL(url)
    })
    .catch(error => console.error(error))
});
