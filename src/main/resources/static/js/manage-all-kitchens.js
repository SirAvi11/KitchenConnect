function updateKitchenStatus(kitchenId, isApproved) {
    const kitchenCard = document.getElementById(`kitchen-card-${kitchenId}`);
    
    fetch('/kitchens/statusUpdate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ kitchenId: kitchenId, isApproved: Boolean(isApproved) })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status == "success") {
            // Replace modal body to show success message before closing the modal
            
        } else {
            // Replace modal body to show rejected message before closing the modal
            console.error("Failed to update kitchen status");
        }
    })
    .catch(error => console.error("Error updating:", error));
}

function getDocuments(kitchenId) {

    let modalBody = document.getElementById("documentSection");
    if(modalBody) modalBody.innerHTML = "<p>Loading documents...</p>";

    fetch('/kitchens/getKitchenDocuments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ kitchenId: Number(kitchenId) })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if(data.status == "success"){
            console.log("Documents retrieved:", data.documents);
            updateDocumentModal(data.documents);
        }else {
            modalBody.innerHTML = "<p>No documents found...</p>"
        }
        
    })
    .catch(error => console.error("Error retrieving kitchen documents:", error));
}

function updateDocumentModal(documents) {

    let modalBody = document.getElementById("documentSection");

    // Clear the existing content in the modal body
    modalBody.innerHTML = ""; // Clear previous content

    // Create a div for each document type and append it to the modal body
    if (documents.kitchenImagePath) {
        let kitchenImageDiv = document.createElement("div");
        kitchenImageDiv.className = "document-item";
        kitchenImageDiv.innerHTML = `
            <h5>Kitchen Image</h5>
            <img src="${documents.kitchenImagePath}" alt="Kitchen Image" style="max-width: 300px; height: 200px;">
        `;
        modalBody.appendChild(kitchenImageDiv);
    }

    if (documents.menuImagePaths && documents.menuImagePaths.length > 0) {
        let menuImagesDiv = document.createElement("div");
        menuImagesDiv.className = "document-item";
        menuImagesDiv.innerHTML = `<h5>Menu Images</h5>`;
        
        documents.menuImagePaths.forEach(path => {
            let img = document.createElement("img");
            img.src = path;
            img.alt = "Menu Image";
            img.style.maxWidth = "300px";
            img.style.height = "200px";
            menuImagesDiv.appendChild(img);
        });
        
        modalBody.appendChild(menuImagesDiv);
    }

    if (documents.fssaiDocumentPath) {
        let fssaiDiv = document.createElement("div");
        fssaiDiv.className = "document-item";
        fssaiDiv.innerHTML = `
            <h5>FSSAI Document</h5>
            <a href="${documents.fssaiDocumentPath}" target="_blank">View FSSAI Document</a>
        `;
        modalBody.appendChild(fssaiDiv);
    }

    if (documents.panDocumentPath) {
        let panDiv = document.createElement("div");
        panDiv.className = "document-item";
        panDiv.innerHTML = `
            <h5>PAN Document</h5>
            <a href="${documents.panDocumentPath}" target="_blank">View PAN Document</a>
        `;
        modalBody.appendChild(panDiv);
    }
}  