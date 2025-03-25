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
            // Replace card content with status message
            const statusMessage = isApproved ? "Kitchen Approved!" : "Kitchen Rejected";
            const iconClass = isApproved ? "fa-check-circle" : "fa-times-circle";
            const bgClass = isApproved ? "bg-success" : "bg-danger";
            
            kitchenCard.innerHTML = `
                <div class="d-flex flex-column justify-content-center align-items-center p-4 ${bgClass} text-white" style="height: 100%; border-radius: 0.25rem;">
                    <i class="fas ${iconClass} fa-4x mb-3"></i>
                    <h3>${statusMessage}</h3>
                    <p>This kitchen has been ${isApproved ? 'approved' : 'rejected'}</p>
                </div>
            `;
            
            // Add fade out animation before removing
            setTimeout(() => {
                kitchenCard.style.transition = "opacity 0.5s ease";
                kitchenCard.style.opacity = 0;
                
                // Remove card after animation completes
                setTimeout(() => {
                    kitchenCard.remove();
                    window.location.href = "/dashboard?tab=view-kitchens"
                }, 500);
            }, 2000); // Show message for 2 seconds before fade out
        } else {
            console.error("Failed to update kitchen status");
        }
    })
    .catch(error => console.error("Error updating:", error));
}

function handleViewDocument(kitchenId) {

    let modalBody = document.querySelector("modal-body");
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
    // Select the modal title and body elements
    let modalTitle = document.querySelector(".modal-title"); // Use the correct class selector
    let modalBody = document.querySelector(".modal-body"); // Use the correct class selector

    // Set the modal title
    modalTitle.innerText = "Kitchen Documents"; // You can customize this title as needed

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

    // Optionally, show the modal if it's not already visible
    $('#documentModal').modal('show'); // Assuming you're using Bootstrap for the modal
}     

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".accordion-button").forEach(button => {
        button.addEventListener("click", function () {
            let icon = this.querySelector("i");

            setTimeout(() => { // Ensure Bootstrap toggles `aria-expanded` first
                if (this.getAttribute("aria-expanded") === "true") {
                    icon.classList.remove("fa-plus");
                    icon.classList.add("fa-minus");
                } else {
                    icon.classList.remove("fa-minus");
                    icon.classList.add("fa-plus");
                }
            }, 5);
        });
    });
});