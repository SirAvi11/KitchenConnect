function updateKitchenStatus(kitchenId, isApproved) {
    const statusValue = isApproved ? "APPROVED" : "REJECTED";
    let id = kitchenId;

    if(id == null || id == "" || id == undefined){
        id = document.getElementById('hiddenKitchenId').value;
    }

    updateKitchenStatusAPI(id, statusValue);
}

function updateKitchenStatusAPI(id, statusValue) {
    fetch('/kitchens/statusUpdate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ kitchenId: id, status: statusValue })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "success") {
            // Close the modal properly
            const modalElement = document.getElementById('kitchenDetailsModal');
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) {
                modal.hide();
            }
            
            // Refresh the page after a short delay
            setTimeout(() => {
                window.location.href = "/dashboard?tab=manage-all-kitchens";
            }, 500);
        } else {
            console.error("Failed to update kitchen status");
        }
    })
    .catch(error => console.error("Error updating:", error));
}

function markForReview(kitchenId) {
    updateKitchenStatusAPI(kitchenId, "UNDER_VERIFICATION");
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

document.addEventListener('DOMContentLoaded', function () {
    // Get all "View" buttons
    const viewButtons = document.querySelectorAll('.view-kitchen-btn');

    // Add click event listener to each button
    viewButtons.forEach(button => {
        button.addEventListener('click', function () {
            // Extract data from the button's data attributes
            const kitchenId = button.getAttribute('data-kitchen-id');
            const requestedBy = button.getAttribute('data-requested-by');
            const kitchenName = button.getAttribute('data-kitchen-name');
            const status = button.getAttribute('data-status');
            const description = button.getAttribute('data-kitchen-description');
            const deliveryFees = parseFloat(button.getAttribute('data-kitchen-delivery'));
            const address = button.getAttribute('data-address');
            const contactNumber = button.getAttribute('data-contact-number');
            const speciality = button.getAttribute('data-speciality');
            const openDays = button.getAttribute('data-open-days');
            const openTime = button.getAttribute('data-open-time');
            const closeTime = button.getAttribute('data-close-time');
            const fssaiNumber = button.getAttribute('data-fssai-number');
            const fssaiExpiry = button.getAttribute('data-fssai-expiry');
            const panNumber = button.getAttribute('data-pan-number');

            // Populate the modal with the extracted data
            document.getElementById('hiddenKitchenId').value = kitchenId;

            document.getElementById('requestedBy').textContent = requestedBy;
            document.getElementById('kitchenName').textContent = kitchenName;
            document.getElementById('status').textContent = status;
            document.getElementById('description').textContent = description;
            document.getElementById('address').textContent = address;
            document.getElementById('deliveryFees').textContent = deliveryFees;
            document.getElementById('speciality').textContent = speciality;

            document.getElementById('contactNumber').textContent = contactNumber;
            document.getElementById('openDays').textContent = openDays;
            document.getElementById('openTime').textContent = openTime;
            document.getElementById('closeTime').textContent = closeTime;
            document.getElementById('fssaiNumber').textContent = fssaiNumber;
            document.getElementById('expiryData').textContent = fssaiExpiry;
            document.getElementById('panNumber').textContent = panNumber;

            // Update modal footer buttons based on status
            updateModalFooterButtons(kitchenId, status);

            // Show the modal
            const modal = new bootstrap.Modal(document.getElementById('kitchenDetailsModal'));
            modal.show();
        });
    });

    function updateModalFooterButtons(kitchenId, status) {
        const modalFooter = document.getElementById('modalFooterButtons');
        modalFooter.innerHTML = ''; // Clear existing buttons
        
        if (status === 'UNDER_VERIFICATION') {
            modalFooter.innerHTML = `
                <button id="reject-btn" class="btn btn-danger me-2" onclick="updateKitchenStatus(${kitchenId}, false)">Reject</button>
                <button id="approve-btn" class="btn btn-success" onclick="updateKitchenStatus(${kitchenId}, true)">Approve</button>
            `;
        } else {
            modalFooter.innerHTML = `
                <button id="mark-for-review-btn" class="btn btn-warning" onclick="markForReview(${kitchenId})">Mark for Review</button>
            `;
        }
    }

     // Get all status dropdowns
    const statusDropdowns = document.querySelectorAll('.kitchen-status-dropdown');

    // Add change event listener to each dropdown
    statusDropdowns.forEach(dropdown => {
        dropdown.addEventListener('change', function () {
            const kitchenId = dropdown.getAttribute('data-kitchen-id');
            const newStatus = dropdown.value;
            if(newStatus != "ALL"){
                const statusValue = newStatus == "APPROVED" ? true : false;
                updateKitchenStatus(kitchenId,statusValue);
            }
        });
    });

    // Get filter elements
        const kitchenId = document.getElementById('searchKitchenId');
        const searchButton = document.getElementById('searchButton');
        const filterStatus = document.getElementById('filterStatus');
        const startDate = document.getElementById('startDate');
        const endDate = document.getElementById('endDate');
        const refreshButton = document.getElementById('refreshButton');
        const tableRows = document.querySelectorAll('tbody tr');
    
        // Function to filter table rows
        function filterTable() {
            const searchText = kitchenId.value.trim().toLowerCase();
            const selectedStatus = filterStatus.value;
            const startDateValue = startDate.value;
            const endDateValue = endDate.value;
    
            tableRows.forEach(row => {
                const orderNumber = row.querySelector('td:nth-child(2)').textContent.trim().toLowerCase();
                const orderDate = row.querySelector('td:nth-child(4)').textContent.trim();
    
                // Convert order date to a comparable format (dd-MM-yyyy to yyyy-MM-dd)
                const orderDateFormatted = orderDate.split('-').reverse().join('-');
    
                // Get the status from the dropdown or span
                const statusCell = row.querySelector('td:nth-child(6)');
                let status = '';
    
                if (statusCell.querySelector('select')) {
                    // If the status is in a dropdown, get the selected value
                    status = statusCell.querySelector('select').value;
                } else if (statusCell.querySelector('span')) {
                    // If the status is in a span, get the text content
                    status = statusCell.querySelector('span').textContent.trim();
                }
    
                // Check if the row matches the filters
                const matchesSearch = orderNumber.includes(searchText);
                const matchesStatus = selectedStatus === 'ALL' || status === selectedStatus;
                const matchesDateRange = (!startDateValue || orderDateFormatted >= startDateValue) &&
                                        (!endDateValue || orderDateFormatted <= endDateValue);
    
                // Show or hide the row based on the filters
                if (matchesSearch && matchesStatus && matchesDateRange) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    
        // Add event listeners
        searchButton.addEventListener('click', filterTable);
        filterStatus.addEventListener('change', filterTable);
        startDate.addEventListener('change', filterTable);
        endDate.addEventListener('change', filterTable);
    
        // Refresh button to reset filters
        refreshButton.addEventListener('click', function () {
            searchOrderNumber.value = '';
            filterStatus.value = 'ALL';
            startDate.value = '';
            endDate.value = '';
            filterTable(); // Reapply filters (which will show all rows)
        });
    
        // Initial filter application (optional)
        filterTable();

        const kitchenModal = document.getElementById('kitchenDetailsModal');
        kitchenModal.addEventListener('hidden.bs.modal', () => {
            // Absolute fallback
            document.body.classList.remove('modal-open');
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        });

});