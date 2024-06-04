const fileInput = document.getElementById('fileInput');
const pdfObject = document.getElementById('pdfObject');
let getName = localStorage.getItem('file')
/**
    * Detect when a file has been inputted and render the PDF file.
    * Also listen for changes in the file such as adding / editing / deleting annotations and saving the file
**/
fileInput.addEventListener('change', function(e) {
        var file = e.target.files[0];
        if (!file)
            return;

        //showcasing the file
		var adobeDCView = new AdobeDC.View({clientId: "543b9355cff44d19821857d8b0ddfb96", divId: "pdfContainer"});
		adobeDCView.previewFile({
			content:{location: {url: URL.createObjectURL(file)}},
			metaData:{fileName: file.name, id: file.name}
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
              console.log("Document was saved")
              return new Promise((resolve, reject) => {
                 resolve({
                    code: AdobeDC.View.Enum.ApiResponseCode.SUCCESS,
                    data: {
                      metaData: Object.assign(metaData, {fileName: file.name})
                    }
                 });
              });
           },
        saveOptions);


        //promise used to listen to changes
        const previewFilePromise = adobeDCView.previewFile({
                                   			content:{location: {url: URL.createObjectURL(file)}},
                                   			metaData:{fileName: file.name, id: file.name}
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
                                    				} else if (event.type === 'ANNOTATION_UPDATED') {
                                    						console.log("Annotation updated\nAll annotations: ", annotationManager.getAnnotations())
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




	});


/**
    * Fetch the codes from the database when the page loads.
**/
document.addEventListener('DOMContentLoaded', function() {
    fetchCodes();
    loadPassedFile();
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
        const codesContainer = document.getElementById('codes');

        codes.forEach(code => {
            const codeButton = document.createElement('input');
            codeButton.type = 'submit';
            codeButton.className = 'code';
            codeButton.value = code;
            codesContainer.appendChild(codeButton);
        });
    })
    .catch(error => console.error('Error fetching codes: ', error));
}

function loadPassedFile() {
    // getting the file from database
    let sessionFile
    fetch( `http://localhost:8080/submissions/${getName}`)
        .then(response => {
            if(response.ok) {
                //sessionFile = response.json()
                return response.json()
            } else {
                throw new Error('Failed to fetch');
            }
        }).then(sub => {
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

            let newFile = passedFile
            newFile.fileSubmission = base64string
            newFile.lastEdited = new Date()

            var endpoint = `http://localhost:8080/submissions/${submissionEmail}`
            fetch(endpoint, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
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
                            } else if (event.type === 'ANNOTATION_UPDATED') {
                                console.log("Annotation updated\nAll annotations: ", annotationManager.getAnnotations())
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