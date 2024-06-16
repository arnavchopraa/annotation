const role = localStorage.getItem('role')
let getName = localStorage.getItem('file')
var allCodes
const subBtn = document.querySelector('#submitFile')
const parseBtn = document.querySelector('#parseFile')
const arr = document.querySelectorAll('.admin')
let newFile;
const prevButton = document.getElementById('prevFile')
const nextButton = document.getElementById('nextFile')
let token = localStorage.getItem('token')

prevButton.addEventListener('click', function() {
    
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
})

nextButton.addEventListener('click', function() {
    
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
})

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
    if(role === 'student') {
        arr.forEach(elem => {
            elem.style.display = 'none'
        })
    }
    fetchCodes();
    loadPassedFile();
});

subBtn.addEventListener('click', () => {
    if(newFile === undefined) {

    }
    newFile.submitted = true;
    newFile.lastSubmitted = formatTimestamp(new Date())
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
            if(response.status == 200) console.log('ESTI CEL MAI TARE PAUL')
        }
    ).catch(e => {
        console.log(e)
    })
})

parseBtn.addEventListener('click', () => {
    window.location.href = "../Parsing/Parsing.html";
})



/**
    * Add the codes from the backend to the container and display them as buttons.
**/
function fetchCodes() {
    var endpoint = "http://localhost:8080/frontend/codes";
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

        const codesContainer = document.getElementById('codes');

        codes.forEach(code => {
            const codeButton = document.createElement('div');
            codeButton.className = 'code';

            codeButton.textContent = code.id;
            codeButton.title = code.codeContent

            codesContainer.appendChild(codeButton);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}

function loadPassedFile() {
    // getting the file from database
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
        }

    )
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
            let uint8Array = new Uint8Array(content)
            let blob = new Blob([uint8Array], { type: 'application/pdf' })

            let binaryString = '';
            for (let i = 0; i < uint8Array.length; i++) {
                binaryString += String.fromCharCode(uint8Array[i]);
            }
            let base64string = btoa(binaryString)

            newFile.fileSubmission = base64string
            newFile.lastEdited = formatTimestamp(new Date())

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
                    if(response.status == 200) console.log('GOOD JOB PAUL')
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
                                replaceCodes(annotationManager, event.data)
                            } else if (event.type === 'ANNOTATION_UPDATED') {
                                console.log("Annotation updated\nAll annotations: ", annotationManager.getAnnotations())
                                replaceCodes(annotationManager, event.data)
                            } else if (event.type === 'ANNOTATION_DELETED') {
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


/**
    * Format the timestamp to a HH-mm-ss yyyy-MM-dd format
    * @param timestamp - the timestamp to be formatted
*/
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const year = date.getFullYear();

    return `${hours}:${minutes}:${seconds} ${year}-${month}-${day}`;
}

